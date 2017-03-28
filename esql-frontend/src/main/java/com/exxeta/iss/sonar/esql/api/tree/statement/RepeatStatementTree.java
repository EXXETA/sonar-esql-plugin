package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface RepeatStatementTree extends StatementTree{
	LabelTree label();
	SyntaxToken colon();
	SyntaxToken repeatKeyword();
	List<StatementTree> statements();
	SyntaxToken untilKeyword();
	ExpressionTree condition();
	SyntaxToken endKeyword();
	SyntaxToken repeatKeyword2();
	LabelTree label2(); 
	SyntaxToken semi();
}
