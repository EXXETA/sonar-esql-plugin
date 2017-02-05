package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LabelTreeImpl;

public interface BeginEndStatementTree extends StatementTree{
	LabelTreeImpl labelName1();
	InternalSyntaxToken colon();
	InternalSyntaxToken beginKeyword();
	InternalSyntaxToken notKeyword();
	InternalSyntaxToken atomicKeyword();
	List<StatementTree> statements();
	InternalSyntaxToken endKeyword();
	LabelTreeImpl labelName2();
	InternalSyntaxToken semiToken();
}
