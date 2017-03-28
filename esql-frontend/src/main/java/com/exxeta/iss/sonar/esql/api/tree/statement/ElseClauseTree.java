package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ElseClauseTree extends Tree{
	  SyntaxToken elseKeyword();

	  List<StatementTree> statements();
}
