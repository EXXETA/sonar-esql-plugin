package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface MessageSourceTree extends Tree {

	SyntaxToken environmentKeyword();
	SyntaxToken environment();
	SyntaxToken messageKeyword(); 
	SyntaxToken message(); 
	SyntaxToken exceptionKeyword();
	SyntaxToken exception();
}
