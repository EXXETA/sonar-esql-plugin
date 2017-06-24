package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="InitializeVariables")
public class InitializeVariablesCheck extends DoubleDispatchVisitorCheck{

	
	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		if (tree.initialValueExpression()==null){
			addIssue(tree, "Add an initial value for this variable");
		}
	}
	
}
