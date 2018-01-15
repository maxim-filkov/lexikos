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

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is to automatically detect language of a given phrase.
 *
 * @author Maksim Filkov
 */
@Service
public class LanguageDetectionService {

   private static final LanguageDetector languageDetector;

   private static final List<String> languages = new ArrayList<String>() {{
      add("en");
      add("ru");
   }};

   static {
      final List<LanguageProfile> languageProfiles;
      try {
         languageProfiles = new LanguageProfileReader().read(languages);
      } catch (final IOException e) {
         throw new RuntimeException("Unable to read LanguageProfileReader languages: " + e.getMessage());
      }
      languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
            .withProfiles(languageProfiles).build();
   }

   public String detect(final String phrase) {
      final TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingShortCleanText();
      final TextObject phraseObj = textObjectFactory.forText(phrase);
      final Optional<LdLocale> ldLocale = languageDetector.detect(phraseObj);
      return ldLocale.isPresent() ? ldLocale.get().toString() : "";
   }

}
