package com.exxeta.iss.sonar.esql.api.symbols;


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
	    STRING,
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

	    }

}