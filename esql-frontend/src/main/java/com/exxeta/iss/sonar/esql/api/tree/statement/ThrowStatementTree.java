package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ThrowStatementTree extends StatementTree {

	SyntaxToken throwKeyword();

	SyntaxToken userKeyword();

	SyntaxToken exceptionKeyword();

	SyntaxToken severityKeyword();

	ExpressionTree severity();

	SyntaxToken catalogKeyword();

	ExpressionTree catalog();

	SyntaxToken messageKeyword();

	ExpressionTree message();

	SyntaxToken valuesKeyword();

	ParameterListTree values();
	
	SyntaxToken semi();

}
