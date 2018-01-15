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

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Describes typical language pair.
 *
 * @author Maksim Filkov
 */
@Data
public class PageableResult {

   private List<Entry> entries;

   private int totalPages;

   private long totalEntries;

   private int entriesOnPage;

   private boolean isFirst;

   private boolean isLast;

   private boolean hasNext;

   private boolean hasPrevious;

   private boolean hasContent;

   public PageableResult(final Page<Entry> pages) {
      entries = pages.getContent();
      totalPages = pages.getTotalPages();
      totalEntries = pages.getTotalElements();
      entriesOnPage = pages.getNumberOfElements();
      isFirst = pages.isFirst();
      isLast = pages.isLast();
      hasNext = pages.hasNext();
      hasPrevious = pages.hasPrevious();
      hasContent = pages.hasContent();
   }

}
