package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.DataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ReturnTypeTree extends Tree {

	SyntaxToken returnsKeyword();
	DataTypeTree dataType();
	SyntaxToken notKeyword();
	SyntaxToken nullKeyword();
	SyntaxToken nullableKeyword();

}
