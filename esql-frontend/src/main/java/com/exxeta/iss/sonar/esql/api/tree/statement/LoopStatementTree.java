package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface LoopStatementTree extends StatementTree{
	LabelTree label();
	SyntaxToken colon();
	SyntaxToken loopKeyword();
	List<StatementTree> statements();
	SyntaxToken endKeyword();
	SyntaxToken loopKeyword2();
	LabelTree label2();
	SyntaxToken semi();
}
