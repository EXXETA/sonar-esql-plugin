package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ReturnStatementTree extends StatementTree{
	SyntaxToken returnKeyword();
	ExpressionTree expression();
	SyntaxToken semi();
}
