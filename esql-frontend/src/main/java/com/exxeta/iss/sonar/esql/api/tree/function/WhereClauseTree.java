package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface WhereClauseTree extends Tree{
	SyntaxToken whereKeyword();
	ExpressionTree expression();
}
