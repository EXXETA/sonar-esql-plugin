package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface AttachStatementTree extends StatementTree {
	SyntaxToken attachKeyword();

	FieldReferenceTree dynamicReference();

	SyntaxToken toKeyword();

	FieldReferenceTree fieldReference();

	SyntaxToken asKeyword();

	SyntaxToken location();

	SyntaxToken semi();
}
