package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface OverlayFunctionTree extends StringManipulationFunctionTree {
	SyntaxToken overlayKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree sourceString();
	SyntaxToken placingKeyword();
	ExpressionTree sourceString2();
	SyntaxToken fromKeyword();
	ExpressionTree startPosition();
	SyntaxToken forKeyword();
	ExpressionTree stringLength();
	SyntaxToken closingParenthesis();
}
