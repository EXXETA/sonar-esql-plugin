package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface UpdateStatementTree extends StatementTree {
	SyntaxToken updateKeyword();

	FieldReferenceTree tableReference();

	SyntaxToken asKeyword();

	SyntaxToken alias();

	SyntaxToken setKeyword();

	SeparatedList<SetColumnTree> setColumns();

	SyntaxToken whereKeyword();

	ExpressionTree whereExpression();

	SyntaxToken semi();
}
