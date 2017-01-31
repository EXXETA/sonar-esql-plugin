package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PropagateStatementTree extends StatementTree{

	SyntaxToken propagateKeyword();
	SyntaxToken toKeyword();
	SyntaxToken targetType();
	SyntaxToken targetName();
	MessageSourceTree messageSource();
	ControlsTree controls();
}
