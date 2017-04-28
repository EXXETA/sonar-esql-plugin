package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface PassthruFunctionTree extends MiscellaneousFunctionTree{
	SyntaxToken passthruKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree expression();
	SyntaxToken toKeyword();
	FieldReferenceTree databaseReference();
	SyntaxToken valuesKeyword();
	ParameterListTree values();
	SeparatedList<Tree> argumentList();
	SyntaxToken closingParenthesis();
}
