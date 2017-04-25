package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FieldFunctionTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface AsbitstreamFunctionTree extends FieldFunctionTree {
	InternalSyntaxToken asbitstreamKeyword();
	InternalSyntaxToken openingParenthesis();
	FieldReferenceTreeImpl fieldReference();
	
	InternalSyntaxToken optionsSeparator();
	ExpressionTree optionsExpression();
	InternalSyntaxToken encodingSeparator();
	ExpressionTree encodingExpression();
	InternalSyntaxToken ccsidSeparator();
	ExpressionTree ccsidExpression();
	InternalSyntaxToken setSeparator();
	ExpressionTree setExpression();
	InternalSyntaxToken typeSeparator();
	ExpressionTree typeExpression();
	InternalSyntaxToken formatSeparator();
	ExpressionTree formatExpression();
	
	InternalSyntaxToken closingParenthesis();

}
