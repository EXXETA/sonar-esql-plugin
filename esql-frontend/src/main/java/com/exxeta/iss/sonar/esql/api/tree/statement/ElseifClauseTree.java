package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ElseifClauseTree extends Tree{
	  SyntaxToken elseifKeyword();
	  ExpressionTree expression();
	  SyntaxToken thenKeyword();
	  List<StatementTree> statements();
}
