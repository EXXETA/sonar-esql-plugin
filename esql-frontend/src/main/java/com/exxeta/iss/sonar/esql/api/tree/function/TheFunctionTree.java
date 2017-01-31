package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface TheFunctionTree extends ListFunctionTree {

	SyntaxToken theKeyword();
	
	SyntaxToken openingParenthesis();
	
	SyntaxToken expression();
	
	SyntaxToken closingParenthesis();
}
