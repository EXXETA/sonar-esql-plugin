package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "UnreachableCode")
public class UnreachableCodeCheck extends DoubleDispatchVisitorCheck{

	  private static final String MESSAGE = "Remove the code after this statement.";

	
	@Override
	public void visitStatements(StatementsTree tree) {
		boolean unreachable = false;
		for (int i=0;i<tree.statements().size()-1;i++){ 
			StatementTree currentStatement = tree.statements().get(i);
			if (!unreachable && (currentStatement instanceof ThrowStatementTree || isJumpStatement(currentStatement))){
				unreachable = true;
				addIssue(currentStatement, MESSAGE);
			}
		}
		super.visitStatements(tree);
	}


	private boolean isJumpStatement(StatementTree tree) {
		return tree instanceof ReturnStatementTree 
				|| tree instanceof LeaveStatementTree
				|| tree instanceof IterateStatementTree;
	}
	
}
