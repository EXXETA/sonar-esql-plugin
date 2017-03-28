package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

@Rule(key = "IterateStatement")
public class IterateStatementCheck extends DoubleDispatchVisitorCheck {

	@Override
	public void visitIterateStatement	(IterateStatementTree tree){
		addIssue(new LineIssue(this, tree, "Avoid using ITERATE statement."));
	}
	
}