package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface DeleteFromStatementTree extends StatementTree{
	SyntaxToken deleteKeyword(); 
	SyntaxToken fromKeyword();
	FieldReferenceTree tableReference();
	SyntaxToken asKeyword();
	SyntaxToken asCorrelationName();
	SyntaxToken whereKeyword();
	ExpressionTree whereExpression();
	SyntaxToken semi();
}
