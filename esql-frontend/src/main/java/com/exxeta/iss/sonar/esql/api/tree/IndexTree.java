package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface IndexTree extends Tree{
	SyntaxToken openBracket();
	SyntaxToken direction();
	ExpressionTree index();
	SyntaxToken closeBracket();
}
