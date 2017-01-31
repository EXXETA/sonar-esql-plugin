package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface IntervalQualifierTree extends Tree {
	SyntaxToken from();

	SyntaxToken toKeyword();

	SyntaxToken to();

}
