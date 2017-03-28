package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface InExpressionTree extends ExpressionTree{

	FieldReferenceTree fieldReference();
	SyntaxToken inKeyword();
	SeparatedList<Tree> argumentList();
}
