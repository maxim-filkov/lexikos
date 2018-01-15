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

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all supported parts of speech.
 *
 * @author Maksim Filkov
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PartOfSpeech {

    /**
     * Noun.
     */
    NOUN("noun"),

    /**
     * Adjective.
     */
    ADJECTIVE("adjective"),

    /**
     * Verb.
     */
    VERB("verb"),

    /**
     * Adverb-participle.
     */
    ADVERB_PARTICIPLE("adverb-participle"),

    /**
     * Participle.
     */
    PARTICIPLE("participle"),

    /**
     * Numeral.
     */
    NUMERAL("numeral"),

    /**
     * Pronoun.
     */
    PRONOUN("pronoun"),

    /**
     * Adverb.
     */
    ADVERB("adverb"),

    /**
     * Preposition.
     */
    PREPOSITION("preposition"),

    /**
     * Conjunctive.
     */
    CONJUNCTIVE("conjunctive"),

    /**
     * Interjection.
     */
    INTERJECTION("interjection"),

    /**
     * Particle.
     */
    PARTICLE("particle"),

    /**
     * Parenthesis.
     */
    PARENTHESIS("parenthesis"),

    /**
     * Conjunction.
     */
    CONJUNCTION("conjunction"),

    /**
     * Phrase.
     */
    PHRASE("phrase");

    private static final Map<String, PartOfSpeech> LOOKUP = new HashMap<>();

    static {
        for (final PartOfSpeech partOfSpeech : PartOfSpeech.values()) {
            LOOKUP.put(partOfSpeech.getName(), partOfSpeech);
        }
    }

    @Getter
    private final String name;

    /**
     * Returns part of speech constant by its name.
     *
     * @param name
     *            Part of speech name, e.g. "noun".
     *
     * @return Image content type.
     */
    public static PartOfSpeech get(final String name) {
        return LOOKUP.get(name);
    }

}
