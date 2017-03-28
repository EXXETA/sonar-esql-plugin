package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface RepeatClauseTree extends Tree{

	SyntaxToken repeatKeyword();
	SyntaxToken valueKeyword();
	ExpressionTree expression();
	
}
