package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;

public interface CallExpressionTree extends ExpressionTree{

	FunctionTree function();
	FieldReferenceTree functionName();
	ParameterListTree parameters();
	
}
