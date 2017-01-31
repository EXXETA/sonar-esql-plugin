package com.exxeta.iss.sonar.esql.api.tree;

import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface PathClauseTree extends Tree {

	SeparatedList<SchemaNameTree> schemaNames();

	InternalSyntaxToken pathToken();

}
