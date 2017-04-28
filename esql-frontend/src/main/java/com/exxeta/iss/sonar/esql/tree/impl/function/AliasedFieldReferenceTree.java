package com.exxeta.iss.sonar.esql.tree.impl.function;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface AliasedFieldReferenceTree extends Tree{
	FieldReferenceTreeImpl fieldRefernce();
	InternalSyntaxToken asKeyword();
	InternalSyntaxToken alias();

}
