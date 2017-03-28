package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface ParseClauseTree extends Tree {

	
	SyntaxToken parseKeyword();
	SyntaxToken openingParenthesis();
	SeparatedList<Tree> options();
	SyntaxToken encodingKeyword();
	ExpressionTree encoding();
	SyntaxToken ccsidKeyword();
	ExpressionTree ccsid();
	SyntaxToken setKeyword();
	ExpressionTree set();
	SyntaxToken typeKeyword();
	ExpressionTree type();
	SyntaxToken formatKeyword();
	ExpressionTree format();
	SyntaxToken closingParenthesis();
}
