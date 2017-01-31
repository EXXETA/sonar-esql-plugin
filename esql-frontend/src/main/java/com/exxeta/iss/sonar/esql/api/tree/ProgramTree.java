package com.exxeta.iss.sonar.esql.api.tree;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ProgramTree extends Tree {

	@Nullable
	BrokerSchemaStatementTree brokerSchemaStatement();

	@Nullable
	PathClauseTree pathClause();
	
	@Nullable
	SyntaxToken semiToken();
	
	EsqlContentsTree esqlContents();
	
	SyntaxToken EOFToken();

}
