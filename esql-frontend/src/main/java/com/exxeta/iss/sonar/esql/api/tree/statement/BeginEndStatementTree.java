package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface BeginEndStatementTree extends StatementTree{
	InternalSyntaxToken labelName1();
	InternalSyntaxToken colon();
	InternalSyntaxToken beginKeyword();
	InternalSyntaxToken notKeyword();
	InternalSyntaxToken atomicKeyword();
	List<StatementTree> statements();
	InternalSyntaxToken endKeyword();
	InternalSyntaxToken labelName2();
	InternalSyntaxToken semiToken();
}
