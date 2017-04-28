package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface FromClauseExpressionTree extends Tree{
	InternalSyntaxToken fromKeyword();
	SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferences();
}
