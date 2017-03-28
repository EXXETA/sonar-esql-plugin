package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ExternalRoutineBodyTreeImpl;

public interface RoutineBodyTree extends Tree{
	StatementTree statement();
	ExternalRoutineBodyTreeImpl externalRoutineBody();

}
