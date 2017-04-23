package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface TrimFunctionTree extends StringManipulationFunctionTree {
	SyntaxToken trimKeyword();
	SyntaxToken openingParenthesis();
	SyntaxToken qualifier();
	ExpressionTree trimSingleton();
	SyntaxToken fromKeyword();
	ExpressionTree sourceString();
	SyntaxToken closingParenthesis();
}
