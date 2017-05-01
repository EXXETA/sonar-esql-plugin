package com.exxeta.iss.sonar.esql.api.tree.statement;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;

public interface StatementsTree extends Tree{
	List<StatementTree> statements();
}
