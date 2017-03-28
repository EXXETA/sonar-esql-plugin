package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface LabelTree extends Tree {
	InternalSyntaxToken name();
}
