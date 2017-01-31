package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ResultSetTree extends Tree{
	SyntaxToken dynamicKeyword(); 
	SyntaxToken resultKeyword();
	SyntaxToken setsKeyword();
	SyntaxToken integer();

}
