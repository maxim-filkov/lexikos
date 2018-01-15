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

package org.lexikos.translator.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Solr dictionary entries field names.
 *
 * @author Maksim Filkov
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SolrEntryFieldNames {

   /**
    * Source phrase used when searching partially matched in direct translation direction.
    */
   public static final String SOURCE_PHRASE_FIELD_NAME = "sourcePhrase";

   /**
    * Source phrase used when searching exactly matched in direct translation direction.
    */
   public static final String SOURCE_PHRASE_EXACT_FIELD_NAME = "sourcePhraseExact";

   /**
    * Target phrase used when searching exactly matched in reverse translation direction.
    */
   public static final String TARGET_PHRASE_FIELD_NAME = "targetPhrase";

   /**
    * Target phrase used when searching exactly matched in reverse translation direction.
    */
   public static final String TARGET_PHRASE_EXACT_FIELD_NAME = "targetPhraseExact";

   /**
    * Unique identifier for dictionary which contains entry.
    */
   public static final String DICTIONARY_FIELD_NAME = "dictionary";

   /**
    * Unique identifier for used who created entry.
    */
   public static final String AUTHOR_FIELD_NAME = "author";

   /**
    * Part of speech for source and target phrase.
    */
   public static final String PART_OF_SPEECH_FIELD_NAME = "partOfSpeech";

   /**
    * Commentary for dictionary entry.
    */
   public static final String COMMENTARY_FIELD_NAME = "commentary";

   /**
    * Image for dictionary entry.
    */
   public static final String IMAGE_NAME_FIELD_NAME = "image";

   /**
    * Entry last update date.
    */
   public static final String DATE_FIELD_NAME = "date";

   /**
    * Unique auto generated identifier for dictionary entry.
    */
   public static final String ID_FIELD_NAME = "id";

   /**
    * Pronunciation for dictionary entry.
    */
   public static final String PRONUNCIATION_FIELD_NAME = "pronunciation";

}
