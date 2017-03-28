package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PassthruStatementTree extends StatementTree{

	SyntaxToken passthruKeyword();
	ExpressionTree expression();
	SyntaxToken toKeyword();
	FieldReferenceTree databaseReference();
	SyntaxToken valuesKeyword();
	ParameterListTree expressionList();
	SyntaxToken semi();
}
