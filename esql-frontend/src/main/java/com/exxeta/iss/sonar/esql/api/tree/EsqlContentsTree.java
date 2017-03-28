package com.exxeta.iss.sonar.esql.api.tree;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;

public interface EsqlContentsTree extends Tree{

	List<StatementTree> items();

}
