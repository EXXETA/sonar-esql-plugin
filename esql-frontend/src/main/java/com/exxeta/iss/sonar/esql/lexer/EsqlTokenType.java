package com.exxeta.iss.sonar.esql.lexer;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum EsqlTokenType implements TokenType, GrammarRuleKey{
	IDENTIFIER,
	  NUMERIC_LITERAL,
	  EMPTY;

	  @Override
	  public String getName() {
	    return name();
	  }

	  @Override
	  public String getValue() {
	    return name();
	  }

	  @Override
	  public boolean hasToBeSkippedFromAst(AstNode node) {
	    throw new IllegalStateException("AST with AstNode should not be constructed");
	  }
}
