package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface BrokerSchemaStatementTree extends Tree {

	SyntaxToken brokerKeyword();
	SyntaxToken schemaKeyword();
	SchemaNameTree schemaName();
	

	
}
