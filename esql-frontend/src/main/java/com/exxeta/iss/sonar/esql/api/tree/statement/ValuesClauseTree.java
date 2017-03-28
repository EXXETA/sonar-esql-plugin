package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface ValuesClauseTree extends Tree {

	SyntaxToken identityKeyword();
	FieldReferenceTree identity();
	SyntaxToken typeKeyword();
	ExpressionTree type();
	SyntaxToken namespaceKeyword();
	ExpressionTree namespace();
	SyntaxToken nameKeyword();
	ExpressionTree name();
	SyntaxToken valueKeyword();
	ExpressionTree value();
	
}
