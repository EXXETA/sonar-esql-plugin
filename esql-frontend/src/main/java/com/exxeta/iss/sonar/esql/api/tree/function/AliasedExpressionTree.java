package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface AliasedExpressionTree extends Tree{
	ExpressionTree expression();
	InternalSyntaxToken asKeyword();
	InternalSyntaxToken alias();

}
