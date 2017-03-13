package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.FromClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ParseClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RepeatClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ValuesClauseTreeImpl;

public interface CreateStatementTree extends StatementTree {
	InternalSyntaxToken createKeyword();

	InternalSyntaxToken qualifierName();

	InternalSyntaxToken qualifierOfKeyword();

	FieldReferenceTree target();
	
	InternalSyntaxToken asKeyword();

	FieldReferenceTreeImpl aliasFieldReference();

	InternalSyntaxToken domainKeyword();

	ExpressionTree domainExpression();

	RepeatClauseTreeImpl repeatClause();

	ValuesClauseTreeImpl valuesClause();

	FromClauseTreeImpl fromClause();

	ParseClauseTreeImpl parseClause();

	InternalSyntaxToken semi();
}
