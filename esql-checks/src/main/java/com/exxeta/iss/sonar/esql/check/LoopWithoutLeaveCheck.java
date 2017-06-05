package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="LoopWithoutLeave")
public class LoopWithoutLeaveCheck extends DoubleDispatchVisitorCheck {

	@Override
	public void visitLoopStatement(LoopStatementTree tree) {
		if (tree.descendants().filter(t -> t.is(Kind.LEAVE_STATEMENT)).count()==0){
			addIssue(tree, "\"LOOP\"-statements without \"LEAVE\" will never terminate.");
			
		}
	};
}
