package com.exxeta.iss.sonar.esql.tree.impl.expression;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface AssignmentExpressionTree extends ExpressionTree {

	  ExpressionTree variable();

	  SyntaxToken operator();

	  ExpressionTree expression();

	}

