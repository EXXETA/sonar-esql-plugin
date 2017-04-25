package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ForFunctionTree extends FieldFunctionTree {
	SyntaxToken forKeyword();
	SyntaxToken qualifier();
	FieldReferenceTree fieldReference();
	SyntaxToken asKeyword();
	SyntaxToken asIdentifier();
	SyntaxToken openingParenthesis();
	ExpressionTree expression();
	SyntaxToken closingParenthesis();

}
