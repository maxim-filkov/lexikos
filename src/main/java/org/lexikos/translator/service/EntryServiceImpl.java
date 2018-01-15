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

package org.lexikos.translator.service;

import org.apache.commons.io.FilenameUtils;
import org.lexikos.translator.constant.LanguagePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.lexikos.translator.constant.PartOfSpeech;
import org.lexikos.translator.dao.entry.EntryRepositoryImpl;
import org.lexikos.translator.dao.morpho.MorphologyRepository;
import org.lexikos.translator.domain.Entry;
import org.lexikos.translator.domain.PageableResult;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This service implements APIs related to working with dictionary entries in repository.
 *
 * @author Maksim Filkov
 */
@Service
public class EntryServiceImpl implements EntryService {

   private static final String DEFAULT_PART_OF_SPEECH = PartOfSpeech.PHRASE.getName();
   @Value("${server.image.upload.dir}")
   private String serverImageUploadDir;
   @Resource
   private EntryRepositoryImpl entryRepository;
   @Resource
   private LanguageDetectionService languageDetectionService;

   @Resource
   private MorphologyRepository morphoRepository;

   public List<Entry> getAll(final String phrase, final LanguagePair languagePair) {
      final List<Entry> entries = entryRepository.findExactlyMatched(phrase, languagePair).getContent();
      return swapPhrases(phrase, entries, languagePair);
   }

   public PageableResult getAll(final String phrase, final LanguagePair languagePair, final Pageable page) {
      final Page<Entry> entries = entryRepository.findPartiallyMatched(phrase, languagePair, page);
      final PageableResult pageableResult = new PageableResult(entries);
      pageableResult.setEntries(swapPhrases(phrase, entries.getContent(), languagePair));
      return pageableResult;
   }

   public Entry get(final String entryId, final LanguagePair languagePair) {
      return entryRepository.get(entryId, languagePair);
   }

   public Entry get(final String sourcePhrase, final String targetPhrase, final String partOfSpeech,
                    final LanguagePair languagePair) {
      return entryRepository.get(sourcePhrase, targetPhrase, partOfSpeech, languagePair);
   }

   public Entry create(final Entry entry, final LanguagePair languagePair) {
      final Entry anEntry = swapPhrases(entry, languagePair);
      anEntry.setPartOfSpeech(getPartOfSpeech(anEntry, languagePair));
      entry.setSourcePhrase(entry.getSourcePhrase().trim());
      entry.setTargetPhrase(entry.getTargetPhrase().trim());
      entryRepository.saveEntry(anEntry, languagePair);
      return get(anEntry.getSourcePhrase(), anEntry.getTargetPhrase(), anEntry.getPartOfSpeech(), languagePair);
   }

   public Entry update(final Entry entry, final LanguagePair languagePair) {
      final Entry originalEntry = get(entry.getId(), languagePair);
      if (!originalEntry.getAuthor().equals(entry.getAuthor())) {
         throw new IllegalArgumentException("Wrong author");
      }
      final Entry anEntry = swapPhrases(entry, languagePair);
      anEntry.setPartOfSpeech(getPartOfSpeech(anEntry, languagePair));
      return create(anEntry.setId(null), languagePair);
   }

   public Entry update(final String entryId, final LanguagePair languagePair, final MultipartFile imageFile) {
      String imageName;
      try {
         imageName = saveEntryImage(imageFile);
      } catch (final IOException e) {
         throw new RuntimeException(e);
      }
      final Entry entry = get(entryId, languagePair);
      entry.setImageName(imageName);
      return update(entry, languagePair);
   }

   public void delete(final String entryId, final LanguagePair languagePair) {
      entryRepository.deleteEntry(entryId, languagePair);
   }

   private String saveEntryImage(final MultipartFile imageFile) throws IOException {
      if (imageFile.isEmpty()) {
         return null;
      }
      final byte[] bytes = imageFile.getBytes();
      final String imageFileExtension = "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
      final File file = File.createTempFile("image", imageFileExtension, new File(serverImageUploadDir));
      try (final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
         stream.write(bytes);
         stream.close();
      }
      return file.getName();
   }

   private String getPartOfSpeech(final Entry entry, final LanguagePair languagePair) {
      final List<String> phrasePartsOfSpeech = morphoRepository.findPartsOfSpeech(entry.getSourcePhrase(),
            languagePair.getSourceLanguage());
      final List<String> translationPartsOfSpeech = morphoRepository.findPartsOfSpeech(entry.getTargetPhrase(),
            languagePair.getTargetLanguage());

      if (phrasePartsOfSpeech.isEmpty()) {
         return (!translationPartsOfSpeech.isEmpty()) ? translationPartsOfSpeech.get(0) : DEFAULT_PART_OF_SPEECH;
      }
      final List<String> intersect = findIntersect(phrasePartsOfSpeech, translationPartsOfSpeech);
      return (!intersect.isEmpty()) ? intersect.get(0) : phrasePartsOfSpeech.get(0);
   }

   private List<String> findIntersect(final List<String> list1, final List<String> list2) {
      final Set<String> first = new HashSet<>(list1);
      final Set<String> second = new HashSet<>(list2);
      first.retainAll(second);
      return new ArrayList<>(first);
   }

   private Entry swapPhrases(final Entry entry, final LanguagePair languagePair) {
      final String sourcePhraseLanguage = languageDetectionService.detect(entry.getSourcePhrase());
      final String targetPhraseLanguage = languageDetectionService.detect(entry.getTargetPhrase());

      if (!sourcePhraseLanguage.equals(languagePair.getSourceLanguage()) &&
            !targetPhraseLanguage.equals(languagePair.getTargetLanguage())) {
         final String tmp = entry.getSourcePhrase();
         entry.setSourcePhrase(entry.getTargetPhrase());
         entry.setTargetPhrase(tmp);
      }
      return entry;
   }

   private List<Entry> swapPhrases(final String phrase, final List<Entry> entries, final LanguagePair languagePair) {
      final String phraseLanguage = languageDetectionService.detect(phrase);

      if (!phraseLanguage.equals(languagePair.getSourceLanguage())) {
         final List<Entry> another = new ArrayList<>();
         for (final Entry entry : entries) {
            final String tmp = entry.getSourcePhrase();
            entry.setSourcePhrase(entry.getTargetPhrase());
            entry.setTargetPhrase(tmp);
            another.add(entry);
         }
         return another;
      }
      return entries;
   }

}
