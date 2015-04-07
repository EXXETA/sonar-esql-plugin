/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum EsqlPunctuator implements TokenType {
	      LPARENTHESIS("("),
		  RPARENTHESIS(")"),
		  LT("<"),
		  GT(">"),
		  LE("<="),
		  GE(">="),
		  EQUAL("="),
		  NOTEQUAL("<>"),
		  PLUS("+"),
		  MINUS("-"),
		  STAR("*"),
		  DIV("/"),
		  CONCAT("||"),
		  COMMA(","),
		  COLON(":"),
		  SEMI(";"),
		  LCURLYBRACE("{"),
		  RCURLYBRACE("}"),
		  LBRACKET("["),
		  RBRACKET("]"),
		  DOT(".");
	  /*
	  LPARENTHESIS("("),
	  RPARENTHESIS(")"),
	  LBRACKET("["),
	  RBRACKET("]"),
	  SEMI(";"),//TODO prüfen
	  COMMA(","),
	  LT("<"),
	  GT(">"),
	  LE("<="),
	  GE(">="),
	  EQUAL("=="),
	  NOTEQUAL("!="),
	  EQUAL2("==="),
	  NOTEQUAL2("!=="),
	  PLUS("+"),
	  MINUS("-"),
	  STAR("*"),
	  MOD("%"),
	  DIV("/"),
	  INC("++"),
	  DEC("--"),
	  SL("<<"),
	  SR(">>"),
	  SR2(">>>"),
	  AND("&"),
	  OR("|"),
	  XOR("^"),
	  BANG("!"),
	  TILDA("~"),
	  ANDAND("&&"),
	  OROR("||"),
	  QUERY("?"),
	  COLON(":"),
	  EQU("="),
	  PLUS_EQU("+="),
	  MINUS_EQU("-="),
	  DIV_EQU("/="),
	  STAR_EQU("*="),
	  MOD_EQU("%="),
	  SL_EQU("<<="),
	  SR_EQU(">>="),
	  SR_EQU2(">>>="),
	  AND_EQU("&="),
	  OR_EQU("|="),
	  XOR_EQU("^=");*/

	  private final String value;

	  private EsqlPunctuator(String word) {
	    this.value = word;
	  }

	  public String getName() {
	    return name();
	  }

	  public String getValue() {
	    return value;
	  }

	  public boolean hasToBeSkippedFromAst(AstNode node) {
	    return false;
	  }

	}
