package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ForStatementTree extends StatementTree{
	
	SyntaxToken forKeyword();
	SyntaxToken correlationName();
	SyntaxToken asKeyword();
	FieldReferenceTree fieldReference();
	SyntaxToken doKeyword();
	List<StatementTree> statements();
	SyntaxToken forKeyword2();
	SyntaxToken endKeyword();
	SyntaxToken semi();
	
	
}
