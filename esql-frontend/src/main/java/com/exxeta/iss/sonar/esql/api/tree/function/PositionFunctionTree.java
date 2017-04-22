package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface PositionFunctionTree extends StringManipulationFunctionTree {
	SyntaxToken positionKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree searchExpression();
	SyntaxToken inKeyword();
	ExpressionTree sourceExpression();
	SyntaxToken fromKeyword();
	ExpressionTree fromExpression();
	SyntaxToken repeatKeyword();
	ExpressionTree repeatExpression();
	SyntaxToken closingParenthesis();
}
