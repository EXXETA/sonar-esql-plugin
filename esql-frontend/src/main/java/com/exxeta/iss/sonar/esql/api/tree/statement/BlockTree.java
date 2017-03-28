package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface BlockTree extends StatementTree {

	  SyntaxToken beginKeyword();
	  
	  List<StatementTree> statements();

	  SyntaxToken endKeyword();
	}