package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.WhenClauseTreeImpl;

public interface CaseStatementTree extends StatementTree{
	InternalSyntaxToken caseKeyword();
	ExpressionTree mainExpression();
	List<WhenClauseTreeImpl> whenClauses();
	InternalSyntaxToken elseKeyword();
	List<StatementTree> elseSatements();
	InternalSyntaxToken endKeyword();
	InternalSyntaxToken caseKeyword2();
	InternalSyntaxToken semi();

}
