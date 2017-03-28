package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface CreateModuleStatementTree extends StatementTree {

	SyntaxToken createKeyword();

	SyntaxToken moduleType();

	SyntaxToken moduleKeyword();

	SyntaxToken moduleName();

	List<StatementTree> moduleStatementsList();

	SyntaxToken endKeyword();

	SyntaxToken moduleKeyword2();

}
