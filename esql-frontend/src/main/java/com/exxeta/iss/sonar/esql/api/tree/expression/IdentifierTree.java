package com.exxeta.iss.sonar.esql.api.tree.expression;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.symbols.Scope;
import com.exxeta.iss.sonar.esql.tree.declaration.BindingElementTree;


public interface IdentifierTree extends ExpressionTree, BindingElementTree {

	  SyntaxToken identifierToken();

	  String name();

	  @Nullable
	  Symbol symbol();

	  Scope scope();
	}