package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface NullableTree extends Tree{

	InternalSyntaxToken nullableKeyword();
	InternalSyntaxToken notKeyword();
	InternalSyntaxToken nullKeyword();
	
}
