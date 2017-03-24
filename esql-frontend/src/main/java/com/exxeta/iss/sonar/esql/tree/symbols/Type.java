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
