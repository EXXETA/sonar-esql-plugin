package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface NamespaceTree extends Tree{
	SyntaxToken identifier();
}
