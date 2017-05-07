package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface LikeExpressionTree extends ExpressionTree{
	ExpressionTree source();
	SyntaxToken notKeyword();
	SyntaxToken likeKeyword();
	ExpressionTree pattern();
	SyntaxToken escapeKeyword();
	ExpressionTree escapeChar();
}
