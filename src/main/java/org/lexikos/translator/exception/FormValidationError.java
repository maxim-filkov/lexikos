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

package org.lexikos.translator.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * This class is an exception that is thrown by controller if the validation of added/updated translation entry
 * fails. This class contains a list of FieldError objects (list of errors).
 *
 * @author Maksim Filkov
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FormValidationError extends Exception {

    private List<FieldError> fieldErrors;

}
