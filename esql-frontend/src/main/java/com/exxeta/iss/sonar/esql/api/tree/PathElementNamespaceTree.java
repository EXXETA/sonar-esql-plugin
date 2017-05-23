package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PathElementNamespaceTree extends Tree{
	NamespaceTree namespace();
	SyntaxToken namespaceCurlyOpen();
	ExpressionTree namespaceExpression();
	SyntaxToken namespaceCurlyClose();
	SyntaxToken namespaceStar();
	SyntaxToken colon();

}
