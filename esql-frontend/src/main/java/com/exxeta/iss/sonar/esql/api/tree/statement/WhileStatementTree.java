package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface WhileStatementTree extends StatementTree{
	LabelTree label();
	SyntaxToken colon();
	SyntaxToken whileKeyword();
	ExpressionTree condition();
	SyntaxToken doKeyword();
	List<StatementTree> statements();
	SyntaxToken endKeyword();
	SyntaxToken whileKeyword2();
	LabelTree label2(); 
	SyntaxToken semi();

}
