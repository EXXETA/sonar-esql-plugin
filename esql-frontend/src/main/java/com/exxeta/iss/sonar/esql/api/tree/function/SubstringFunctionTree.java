package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface SubstringFunctionTree extends StringManipulationFunctionTree{
	SyntaxToken substringKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree sourceExpression();
	SyntaxToken qualifier(); 
	ExpressionTree location();
	SyntaxToken forKeyword();
	ExpressionTree stringLength();
	SyntaxToken closingParenthesis();

}
