package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="DeleteFromWithoutWhere")
public class DeleteFromWithoutWhereCheck extends DoubleDispatchVisitorCheck {

	@Override
	public void visitDeleteFromStatement(DeleteFromStatementTree tree) {
		if (tree.whereExpression()==null){
			addIssue(tree, "Add a where caluse to this DELETE FROM statement.");
		}
	}
	
}
