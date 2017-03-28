package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LabelTreeImpl;

public interface IterateStatementTree extends StatementTree{
	InternalSyntaxToken iterateKeyword();
	LabelTreeImpl label();
	InternalSyntaxToken semi();

}
