package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface DetachStatementTree extends StatementTree{
	SyntaxToken detachKeyword();
	FieldReferenceTree fieldReference();
	SyntaxToken semi();
}
