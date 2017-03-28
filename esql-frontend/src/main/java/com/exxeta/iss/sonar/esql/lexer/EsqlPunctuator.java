package com.exxeta.iss.sonar.esql.lexer;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum EsqlPunctuator implements TokenType, GrammarRuleKey {

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




	
	  private final String value;

	  EsqlPunctuator(String word) {
	    this.value = word;
	  }

	  @Override
	  public String getName() {
	    return name();
	  }

	  @Override
	  public String getValue() {
	    return value;
	  }

	  @Override
	  public boolean hasToBeSkippedFromAst(AstNode node) {
	    throw new IllegalStateException("AST with AstNode should not be constructed");
	  }

}
