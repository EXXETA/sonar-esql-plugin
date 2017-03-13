package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface DeleteStatementTree extends StatementTree{
	SyntaxToken deleteKeyword();
	SyntaxToken qualifier();
	SyntaxToken ofKeyword();
	FieldReferenceTree fieldReference();
	SyntaxToken semi();
}
