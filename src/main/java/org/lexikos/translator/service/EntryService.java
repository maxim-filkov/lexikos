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

import org.lexikos.translator.constant.LanguagePair;
import org.lexikos.translator.domain.Entry;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.lexikos.translator.domain.PageableResult;

import java.util.List;

/**
 * Provides APIs to work with dictionary entries.
 *
 * @author Maksim Filkov
 */
@Service
public interface EntryService {

   /**
    * @param phrase       Source phrase to use when searching.
    * @param languagePair Language pair.
    * @param page         Pagination information.
    * @return All partially matched entries found for the given phrase split by dictionaries.
    */
   PageableResult getAll(final String phrase, final LanguagePair languagePair, final Pageable page);

   /**
    * @param phrase       Source phrase to use when searching.
    * @param languagePair Language pair.
    * @return All exactly matched entries found for the given phrase split by dictionaries.
    */
   List<Entry> getAll(final String phrase, final LanguagePair languagePair);

   Entry get(final String entryId, final LanguagePair languagePair);

   Entry get(final String sourcePhrase, final String targetPhrase, final String partOfSpeech,
             final LanguagePair languagePair);

   /**
    * Saves a new entry in the repository.
    *
    * @param entry Entry to save.
    */
   Entry create(final Entry entry, final LanguagePair languagePair);

   /**
    * Updates an existing entry in the repository.
    *
    * @param entry Entry to update.
    */
   Entry update(final Entry entry, final LanguagePair languagePair);

   Entry update(final String entry, final LanguagePair languagePair, MultipartFile imageFile);

   /**
    * Deletes an existing entry found by its unique identifier.
    *
    * @param entryId Unique entry identifier.
    */
   void delete(final String entryId, final LanguagePair languagePair);

}
