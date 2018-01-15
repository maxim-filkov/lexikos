/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lexikos.translator.dao.entry;

import org.lexikos.translator.constant.LanguagePair;
import org.lexikos.translator.constant.SolrEntryFieldNames;
import org.lexikos.translator.domain.Entry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Repository containing dictionary entries.
 *
 * @author Maksim Filkov
 */
@Repository
public class EntryRepositoryImpl implements EntryRepositoryCustom {

   private static final String ESCAPE_CHAR = "\\";
   private static final char[] SOLR_SPECIAL_CHARS = { '+', '-', '&', '|', '!', '(', ')', '{', '}', '[', ']', '^', '"',
      '~', '*', '?', ':', '\\' };
   private static final String PHRASE_PLACEHOLDER = "{PHRASE}";
   private final String EXACT_MATCH_SEARCH_EXPRESSION_PATTERN = "\"_prefix_ " + PHRASE_PLACEHOLDER
      + " _suffix_\"";

   @Resource(name = "solrTemplates")
   private List<SolrTemplate> solars;

   @Value("${search.partial.max_distance_between_words}")
   private int maxDistanceBetweenWordsForPartialMatch;

   @Value("${search.partial.max_results_per_page}")
   private int maxResultsPerPageForPartialMatch;

   @Override
   public Page<Entry> findExactlyMatched(final String phrase, final LanguagePair languagePair) {
      final String escapedPhrase = escapePhraseForSolr(phrase);
      Criteria criteria = new Criteria(SolrEntryFieldNames.SOURCE_PHRASE_EXACT_FIELD_NAME)
         .expression(EXACT_MATCH_SEARCH_EXPRESSION_PATTERN.replace(PHRASE_PLACEHOLDER, escapedPhrase));
      final Page<Entry> results = findAllExact(criteria, languagePair);
      if (results.getTotalElements() > 0) {
         return results;
      }
      criteria = new Criteria(SolrEntryFieldNames.TARGET_PHRASE_EXACT_FIELD_NAME)
         .expression(EXACT_MATCH_SEARCH_EXPRESSION_PATTERN.replace(PHRASE_PLACEHOLDER, escapedPhrase));
      return findAllExact(criteria, languagePair);
   }

   @Override
   public Page<Entry> findPartiallyMatched(final String phrase, final LanguagePair languagePair, final Pageable page) {
      final String escapedPhrase = escapePhraseForSolr(phrase);
      Criteria criteria = new Criteria(SolrEntryFieldNames.SOURCE_PHRASE_FIELD_NAME)
         .expression("\"" + escapedPhrase + "\"" + "~" + maxDistanceBetweenWordsForPartialMatch);
      final Page<Entry> results = findAllPartial(criteria, languagePair, page);
      if (results.getTotalElements() > 0) {
         return results;
      }
      criteria = new Criteria(SolrEntryFieldNames.TARGET_PHRASE_FIELD_NAME)
         .expression("\"" + escapedPhrase + "\"" + "~" + maxDistanceBetweenWordsForPartialMatch);
      return findAllPartial(criteria, languagePair, page);
   }

   public Entry get(final String entryId, final LanguagePair languagePair) {
      final SolrTemplate solr = getSolr(languagePair);
      return solr.getById(entryId, Entry.class);
   }

   public Entry get(final String sourcePhrase, final String targetPhrase, final String partOfSpeech,
                    final LanguagePair languagePair) {
      final SolrTemplate solr = getSolr(languagePair);
      final Query query = new SimpleQuery(new Criteria("sourcePhrase").is(sourcePhrase));
      query.addCriteria(new Criteria("targetPhrase").is(targetPhrase));
      query.addCriteria(new Criteria("partOfSpeech").is(partOfSpeech));
      return solr.queryForObject(query, Entry.class);
   }

   public void saveEntry(final Entry entry, final LanguagePair languagePair) {
      final SolrTemplate solr = getSolr(languagePair);
      solr.saveBean(entry);
      solr.commit();
   }

   public void updateEntry(final Entry entry, final LanguagePair languagePair) {
      final SolrTemplate solr = getSolr(languagePair);
      PartialUpdate update = new PartialUpdate(SolrEntryFieldNames.ID_FIELD_NAME, entry.getId());
      update.add(SolrEntryFieldNames.SOURCE_PHRASE_FIELD_NAME, "");
      entry.setId(null);
      solr.saveBean(entry);
      solr.commit();
   }

   public void deleteEntry(final String entryId, final LanguagePair languagePair) {
      final SolrTemplate solr = getSolr(languagePair);
      solr.deleteById(entryId);
      solr.commit();
   }

   private Page<Entry> findAllPartial(final Criteria searchCriteria, final LanguagePair languagePair,
                                      final Pageable page) {
      final SimpleQuery searchQuery = new SimpleQuery(searchCriteria);
      final Pageable pageable = new PageRequest(page.getPageNumber(), maxResultsPerPageForPartialMatch);
      searchQuery.setPageRequest(pageable);
      return getSolr(languagePair).queryForPage(searchQuery, Entry.class);
   }

   private Page<Entry> findAllExact(final Criteria searchCriteria, final LanguagePair languagePair) {
      final SimpleQuery searchQuery = new SimpleQuery(searchCriteria);
      return getSolr(languagePair).queryForPage(searchQuery, Entry.class);
   }

   private String escapePhraseForSolr(final String phrase) {
      String result = "";
      for (int i = 0; i < phrase.length(); i++) {
         final String currentChar = Character.toString(phrase.charAt(i));
         final boolean shouldBeEscaped = Arrays.asList(SOLR_SPECIAL_CHARS).contains(currentChar);
         result += shouldBeEscaped ? ESCAPE_CHAR + currentChar : currentChar;
      }
      return result;
   }

   private SolrTemplate getSolr(final LanguagePair languagePair) {
      for (final SolrTemplate solr : solars) {
         final String reverted =  languagePair.getTargetLanguage() + languagePair.getSourceLanguage();
         if (solr.getSolrCore().equals(languagePair.getAbbreviation()) || solr.getSolrCore().equals(reverted)) {
            return solr;
         }
      }
      return null;
   }

}
