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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Describes typical language pair.
 *
 * @author Maksim Filkov
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LanguagePair {

   /**
    * English-Russian language pair.
    */
   enru("enru"),

   /**
    * German-Russian language pair.
    */
   deru("deru");

   @Getter
   private final String abbreviation;

   public String getSourceLanguage() {
      return abbreviation.substring(0, 2);
   }

   public String getTargetLanguage() {
      return abbreviation.substring(2);
   }

}
