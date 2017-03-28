package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface WhenClauseTree extends Tree{
	InternalSyntaxToken whenKeyword(); 
	ExpressionTree expression();
	InternalSyntaxToken thenKeyword(); 
	List<StatementTree> statements();

}
