package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface EvalStatementTree extends StatementTree{

	SyntaxToken evalKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree expression();
	SyntaxToken closingParenthesis();
	SyntaxToken semi();

}
