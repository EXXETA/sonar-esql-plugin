package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface FieldReferenceTree extends Tree{
	ExpressionTree primaryExpression();
	SyntaxToken variable();
	PathElementTree pathElement();
	SeparatedList<PathElementTree> pathElements();

}
