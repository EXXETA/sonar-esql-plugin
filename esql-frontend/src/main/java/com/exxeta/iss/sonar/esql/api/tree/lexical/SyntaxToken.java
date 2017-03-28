package com.exxeta.iss.sonar.esql.api.tree.lexical;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;


public interface SyntaxToken extends Tree {

	  String text();

	  List<SyntaxTrivia> trivias();

	  int line();

	  int column();

	  int endLine();

	  int endColumn();
	}
