package com.exxeta.iss.sonar.esql.api.tree.statement;


import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface ParameterListTree extends Tree{
	SyntaxToken openParenthesis();

	  SeparatedList<Tree> parameters();

	  SyntaxToken closeParenthesis();
}
