package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LabelTreeImpl;

public interface LeaveStatementTree extends StatementTree{
	InternalSyntaxToken leaveKeyword();
	LabelTreeImpl label();
	InternalSyntaxToken semi();

}
