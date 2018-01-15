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

package org.lexikos.translator.dao.morpho;

import java.util.List;

/**
 * This repository provides a list of morphological APIs.
 *
 * @author Maksim Filkov
 */
public interface MorphologyRepository {

   /**
    * Returns all found parts of speech as an array for given phrase and language, e.g. [noun,verb].
    *
    * @param phrase   Some phrase to search parts of speech for.
    * @param language Phrase language.
    * @return Array of all found parts of speech.
    */
   List<String> findPartsOfSpeech(final String phrase, final String language);

}
