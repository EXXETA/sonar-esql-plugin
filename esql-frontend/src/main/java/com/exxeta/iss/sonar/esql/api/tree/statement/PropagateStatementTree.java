package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PropagateStatementTree extends StatementTree{

	SyntaxToken propagateKeyword();
	SyntaxToken toKeyword();
	SyntaxToken targetType();
	ExpressionTree target();
	MessageSourceTree messageSource();
	ControlsTree controls();
	SyntaxToken semi();
}
