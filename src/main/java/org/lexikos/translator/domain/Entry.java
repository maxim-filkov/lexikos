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

package org.lexikos.translator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.lexikos.translator.constant.SolrEntryFieldNames;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * This domain class describes a typical dictionary entry.
 *
 * @author Maksim Filkov
 */
@Data
@SolrDocument
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {

   private static final int MAX_PHRASE_LENGTH = 256;
   private static final int MAX_COMMENTARY_LENGTH = 256;

   @Id
   @Size(max = MAX_PHRASE_LENGTH)
   @Field(SolrEntryFieldNames.ID_FIELD_NAME)
   private String id;

   @NotEmpty
   @Size(max = MAX_PHRASE_LENGTH)
   @Field(SolrEntryFieldNames.SOURCE_PHRASE_FIELD_NAME)
   private String sourcePhrase;

   @Field(SolrEntryFieldNames.PART_OF_SPEECH_FIELD_NAME)
   private String partOfSpeech;

   @NotEmpty
   @Size(max = MAX_PHRASE_LENGTH)
   @Field(SolrEntryFieldNames.TARGET_PHRASE_FIELD_NAME)
   private String targetPhrase;

   @Field(SolrEntryFieldNames.DICTIONARY_FIELD_NAME)
   private String dictionary;

   @Field(SolrEntryFieldNames.AUTHOR_FIELD_NAME)
   private String author;

   @NotEmpty
   @Size(max = MAX_COMMENTARY_LENGTH)
   @Field(SolrEntryFieldNames.COMMENTARY_FIELD_NAME)
   private String commentary;

   @Field(SolrEntryFieldNames.IMAGE_NAME_FIELD_NAME)
   private String imageName;

   @Field(SolrEntryFieldNames.DATE_FIELD_NAME)
   private Date date;

   @Field(SolrEntryFieldNames.PRONUNCIATION_FIELD_NAME)
   private String pronunciation;

}
