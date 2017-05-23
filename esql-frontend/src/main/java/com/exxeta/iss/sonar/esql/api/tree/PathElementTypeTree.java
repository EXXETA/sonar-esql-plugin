package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PathElementTypeTree extends Tree{
	SyntaxToken typeOpenParen();
	ExpressionTree typeExpression();
	SyntaxToken typeCloseParen();

}
