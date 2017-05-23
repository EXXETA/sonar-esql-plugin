package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PathElementNameTree extends Tree{
	SyntaxToken nameCurlyOpen();
	ExpressionTree nameExpression();
	SyntaxToken nameCurlyClose();
	SyntaxToken name();

	
}
