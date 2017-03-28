package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ExtractFunctionTree extends DateTimeFunctionTree{
	SyntaxToken extractKeyword();
	SyntaxToken openingParenthesis();
	SyntaxToken type();
	SyntaxToken fromKeyword();
	ExpressionTree sourceDate();
	SyntaxToken closingParenthesis();
}
