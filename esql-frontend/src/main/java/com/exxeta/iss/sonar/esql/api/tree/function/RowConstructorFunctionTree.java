package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface RowConstructorFunctionTree extends ComplexFunctionTree{
	InternalSyntaxToken rowKeyword();
	InternalSyntaxToken openingParenthesis();
	SeparatedList<AliasedExpressionTree> aliasedExpressions();
	InternalSyntaxToken closingParenthesis();
}
