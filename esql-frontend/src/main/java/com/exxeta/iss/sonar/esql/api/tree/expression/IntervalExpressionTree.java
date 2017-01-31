package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.IntervalQualifierTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface IntervalExpressionTree extends ExpressionTree{
	InternalSyntaxToken openParenToken();
	ExpressionTree additiveExpression();
	InternalSyntaxToken closeParenToken();
	IntervalQualifierTree intervalQualifier();
}
