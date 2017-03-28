package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface InsertStatementTree extends StatementTree {
	SyntaxToken insertKeyword();

	SyntaxToken intoKeyword();

	FieldReferenceTree tableReference();

	ParameterListTree columns();

	SyntaxToken valuesKeyword();

	SyntaxToken openParenthesis();

	SeparatedList<ExpressionTree> expressions();

	SyntaxToken closeParenthesis();

	SyntaxToken semi();
}
