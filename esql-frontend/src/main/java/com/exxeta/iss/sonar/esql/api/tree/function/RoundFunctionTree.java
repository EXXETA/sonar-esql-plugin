package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface RoundFunctionTree extends NumericFunctionTree{

	SyntaxToken roundKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree sourceNumber();
//	SyntaxToken comma();
//	ExpressionTree precision();
	/*SyntaxToken modeKeyword();
	SyntaxToken roundingMode();*/
	SyntaxToken closingParenthesis();
}
