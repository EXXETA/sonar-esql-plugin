package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface DataTypeTree extends Tree{

	IntervalDataTypeTree intervalDataType();
	DecimalDataTypeTree decimalDataType();
	SyntaxToken dataType();
	SyntaxToken toKeyword();
	
}
