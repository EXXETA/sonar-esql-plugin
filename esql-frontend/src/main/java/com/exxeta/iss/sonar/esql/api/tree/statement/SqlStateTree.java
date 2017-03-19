package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

public interface SqlStateTree extends Tree{
	 SyntaxToken sqlstateKeyword();
	 SyntaxToken likeKeyword();
	 LiteralTree likeText();
	 SyntaxToken escapeKeyword();
	 LiteralTree escapeText();
	 SyntaxToken valueKeyword(); 
	 LiteralTree valueText();
}
