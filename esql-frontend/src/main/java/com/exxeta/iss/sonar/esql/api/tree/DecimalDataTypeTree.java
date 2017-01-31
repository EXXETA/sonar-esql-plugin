package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface DecimalDataTypeTree extends Tree {

	SyntaxToken decimalKeyword();

	SyntaxToken openParen();

	SyntaxToken precision();

	SyntaxToken comma();

	SyntaxToken scale();

	SyntaxToken closeParen();

}
