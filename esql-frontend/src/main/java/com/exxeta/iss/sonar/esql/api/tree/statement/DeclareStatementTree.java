package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface DeclareStatementTree extends StatementTree {
	SyntaxToken ifKeyword();

	ExpressionTree condition();

	List<StatementTree> statements();

	SyntaxToken thenToken();

	List<ElseifClauseTree> elseifClauses();

	@Nullable
	ElseClauseTree elseClause();

	SyntaxToken endKeyword();

	SyntaxToken ifKeyword2();

}
