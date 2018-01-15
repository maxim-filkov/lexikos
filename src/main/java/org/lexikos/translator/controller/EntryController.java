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

package org.lexikos.translator.controller;

import org.hibernate.validator.constraints.NotEmpty;
import org.lexikos.translator.constant.LanguagePair;
import org.lexikos.translator.domain.Entry;
import org.lexikos.translator.domain.PageableResult;
import org.lexikos.translator.service.EntryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * This controller serves all requests related to translation entries.
 *
 * @author Maksim Filkov
 */
@Validated
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class EntryController {

   @Resource
   private EntryService entryService;

   /**
    * Returns all found entries which are partially (case insensitive) matched to the given phrase, e.g. for the phrase
    * "dog" the partially matched phrases are: "dog", "a dog".
    *
    * @param phrase       Phrase to search translation entries.
    * @param languagePair Language pair.
    * @return All found partially matched entries.
    */
   @RequestMapping(path = "/search/v1/translations/{lang}/fuzzy/", method = RequestMethod.GET)
   public PageableResult getAll(
         @RequestParam @NotEmpty @Size(max = 255) final String phrase,
         @PathVariable("lang") final LanguagePair languagePair,
         final Pageable page) {
      return entryService.getAll(phrase, languagePair, page);
   }

   /**
    * Returns all found entries which are exactly (case insensitive) matched to the given phrase, e.g. for the phrase
    * "dog" the exactly matched phrases are: "dog", "Dog", "DOG".
    *
    * @param phrase       Phrase to search translation entries.
    * @param languagePair Language pair.
    * @return All found exactly matched entries.
    */
   @RequestMapping(path = "/search/v1/translations/{lang}/", method = RequestMethod.GET)
   public List<Entry> getAll(
         @RequestParam("phrase") @NotEmpty @Size(max = 255) final String phrase,
         @PathVariable("lang") final LanguagePair languagePair) {
      return entryService.getAll(phrase, languagePair);
   }

   /**
    * Returns a single (exact) translation entry defined by its identifier.
    *
    * @param entryId      Entry unique identifier.
    * @param languagePair Language pair.
    * @return A single translation entry matching the identifier.
    */
   @RequestMapping(path = "/search/v1/translation/{lang}/", method = RequestMethod.GET)
   public Entry get(
         @RequestParam("entryId") @NotEmpty @Size(max = 255) final String entryId,
         @PathVariable("lang") final LanguagePair languagePair) {
      return entryService.get(entryId, languagePair);
   }

   /**
    * Creates a new dictionary entry.
    *
    * @param languagePair Language pair.
    * @param entry        Dictionary entry to save.
    */
   @RequestMapping(path = "/search/v1/translations/{lang}/", method = RequestMethod.POST,
         consumes = MediaType.APPLICATION_JSON_VALUE)
   public Entry create(@PathVariable("lang") final LanguagePair languagePair,
                       @RequestBody final Entry entry) {
      return entryService.create(entry, languagePair);
   }

   /**
    * Updates an existing translation entry.
    *
    * @param languagePair Language pair.
    * @param entry        Dictionary entry to save.
    */
   @RequestMapping(path = "/search/v1/translations/{lang}/", method = RequestMethod.PUT,
         consumes = MediaType.APPLICATION_JSON_VALUE)
   public Entry update(@PathVariable("lang") final LanguagePair languagePair,
                       @RequestBody final Entry entry) {
      return entryService.update(entry, languagePair);
   }

   /**
    * Updates an existing translation entry with saving image file associated with the entry.
    *
    * @param languagePair Language pair.
    * @param imageFile    Image file for dictionary entry.
    */
   @RequestMapping(path = "/search/v1/translations/{lang}/file", method = RequestMethod.POST)
   public Entry update(@RequestParam("entryId") final String entryId,
                       @PathVariable("lang") final LanguagePair languagePair,
                       @RequestParam("imageFile") final MultipartFile imageFile) {
      return entryService.update(entryId, languagePair, imageFile);
   }

   /**
    * Deletes an existing dictionary entry defined by its identifier.
    *
    * @param entryId      Entry identifier.
    * @param languagePair Language pair.
    */
   @RequestMapping(path = "/search/v1/translations/{lang}/{entryId}", method = RequestMethod.DELETE)
   public void delete(@PathVariable("entryId") final String entryId,
                      @PathVariable("lang") final LanguagePair languagePair) {
      entryService.delete(entryId, languagePair);
   }

}
