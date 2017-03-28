package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ExternalRoutineBodyTree extends Tree{

	SyntaxToken externalKeyword();
	SyntaxToken nameKeyword();
	SyntaxToken expression();

}
