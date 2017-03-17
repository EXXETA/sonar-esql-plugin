package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface NameClausesTree extends Tree{
	SyntaxToken typeKeyword();
	ExpressionTree typeExpression();
	SyntaxToken namespaceKeyword();
	ExpressionTree namespaceExpression();
	SyntaxToken namespaceStar();
	SyntaxToken nameKeyword();
	ExpressionTree nameExpression();
	SyntaxToken identityKeyword();
	PathElementTree pathElement();
	SyntaxToken repeatKeyword(); 
	SyntaxToken repeatTypeKeyword(); 
	SyntaxToken repeatNameKeyword(); 

}
