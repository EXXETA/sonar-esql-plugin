package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ResignalStatementTree extends StatementTree{
	SyntaxToken resignalKeyword();
	SyntaxToken semi();
}
