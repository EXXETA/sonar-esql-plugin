package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;

public interface SetStatementTree extends StatementTree {
	SyntaxToken setKeyword();
	FieldReferenceTreeImpl fieldReference();
	SyntaxToken type();
	SyntaxToken equal();
	ExpressionTree expression();
	SyntaxToken semiToken();

}
