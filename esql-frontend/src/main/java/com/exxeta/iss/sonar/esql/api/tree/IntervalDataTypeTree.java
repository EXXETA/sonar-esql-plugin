package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.tree.impl.declaration.IntervalQualifierTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface IntervalDataTypeTree extends Tree {
	InternalSyntaxToken intervalKeyword();
	IntervalQualifierTreeImpl qualifier();

}
