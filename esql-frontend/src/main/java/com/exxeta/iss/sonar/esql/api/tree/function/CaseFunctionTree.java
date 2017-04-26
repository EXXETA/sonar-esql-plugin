package com.exxeta.iss.sonar.esql.api.tree.function;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.function.WhenClauseExpressionTreeImpl;

public interface CaseFunctionTree extends ComplexFunctionTree{
	SyntaxToken caseKeyword();
	ExpressionTree sourceValue();
	List<WhenClauseExpressionTreeImpl> whenClauses();
	SyntaxToken elseKeyword();
	ExpressionTree elseExpression();
	SyntaxToken endKeyword();
}
