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
import org.lexikos.translator.domain.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository containing dictionary entries.
 *
 * @author Maksim Filkov
 */
public interface EntryRepositoryCustom {

   /**
    * Returns all exactly matched entries for direct translation direction, i.e. the source phrase field is used when
    * searching.
    *
    * @param phrase       The phrase to be used when searching.
    * @param languagePair Language pair.
    * @return List of all exactly matched entries found for given phrase.
    */
   Page<Entry> findExactlyMatched(final String phrase, final LanguagePair languagePair);

   /**
    * Returns all partially matched entries for direct translation direction, i.e. the source phrase field is used when
    * searching. The searching is performed regarding the pagination parameters.
    *
    * @param phrase       The phrase to be used when searching.
    * @param languagePair Language pair.
    * @param page         Pagination parameters to be used for searching.
    * @return List of all partially matched entries found for given phrase regarding pagination parameters.
    */
   Page<Entry> findPartiallyMatched(final String phrase, final LanguagePair languagePair, final Pageable page);

   Entry get(final String entryId, final LanguagePair languagePair);

   Entry get(final String sourcePhrase, final String targetPhrase, final String partOfSpeech,
             final LanguagePair languagePair);

   /**
    * Saves entry in repository.
    *
    * @param entry Entry to save
    */
   void saveEntry(final Entry entry, final LanguagePair languagePair);

   void deleteEntry(final String entryId, final LanguagePair languagePair);

   void updateEntry(final Entry entry, final LanguagePair languagePair);

}
