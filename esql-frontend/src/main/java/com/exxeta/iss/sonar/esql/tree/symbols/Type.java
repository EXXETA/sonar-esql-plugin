/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.tree.symbols;

import com.google.common.annotations.Beta;

/**
 * Interface to represent the different type of Symbols.
 */
@Beta
public interface Type {

  Kind kind();

  Callability callability();

  enum Callability {
    CALLABLE,
    NON_CALLABLE,
    UNKNOWN
  }

  enum Kind {
    UNKNOWN,

    // PRIMITIVE
    CHARACTER,
    NUMBER,
    BOOLEAN,

    // OBJECT
    OBJECT,
    FUNCTION,
    PROCEDURE,
    ARRAY,
    DATE,
    CLASS,

    // JQUERY
    JQUERY_OBJECT,
    JQUERY_SELECTOR_OBJECT,

    // BACKBONE
    BACKBONE_MODEL,
    BACKBONE_MODEL_OBJECT,

    // WEB API
    WINDOW,
    DOCUMENT,
    DOM_ELEMENT,

    // ANGULAR JS
    ANGULAR_MODULE
  }


}
