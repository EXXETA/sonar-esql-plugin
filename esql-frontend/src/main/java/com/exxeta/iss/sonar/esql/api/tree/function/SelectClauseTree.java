package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface SelectClauseTree extends Tree{
	 SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferenceList();
	 InternalSyntaxToken itemKeyword();
	 ExpressionTree itemExpression();
	 InternalSyntaxToken aggregationType();
	 InternalSyntaxToken openingParenthesis();
	 ExpressionTree aggregationExpression();
	 InternalSyntaxToken closingParenthesis();
}
