package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface DeclareHandlerStatementTree extends StatementTree{
	SyntaxToken declareKeyword();
	SyntaxToken handlerType();
	SyntaxToken handlerKeyword();
	SyntaxToken forKeyword();
	SeparatedList<SqlStateTree> sqlStates();
	StatementTree statement();

}
