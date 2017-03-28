package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface LogStatementTree extends StatementTree{
	SyntaxToken logKeyword();
	SyntaxToken eventKeyword();
	SyntaxToken userKeyword();
	SyntaxToken traceKeyword();
	SyntaxToken fullKeyword();
	SyntaxToken exceptionKeyword();
	SyntaxToken severityKeyword();
	ExpressionTree severityExpression();
	SyntaxToken catalogKeyword();
	ExpressionTree catalogExpression();
	SyntaxToken messageKeyword();
	ExpressionTree messageExpression();
	SyntaxToken valuesKeyword();
	ParameterListTree valueExpressions();
	SyntaxToken semi();
}
