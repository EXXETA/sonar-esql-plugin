package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface MoveStatementTree extends StatementTree{

	SyntaxToken moveKeyword();
	SyntaxToken target();
	SyntaxToken toKeyword();
	FieldReferenceTree sourceFieldReference();
	SyntaxToken qualifier();
	NameClausesTree nameClauses();
	SyntaxToken semi();
	
}
