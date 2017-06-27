package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="RoutineWithExcessiveReturns")
public class RoutineWithExcessiveReturnsCheck extends DoubleDispatchVisitorCheck{
	private static final int DEFAULT_MAX = 3;

	  @RuleProperty(description = "Maximum allowed return statements per method", defaultValue = "" + DEFAULT_MAX)
	  public int max = DEFAULT_MAX;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		long count = tree.descendants().filter(c->c.is(Kind.RETURN_STATEMENT)).count();
		if (count>max){
			addIssue(tree, "Reduce the number of returns of this function " + count + ", down to the maximum allowed " + max + ".");
		}
		super.visitCreateFunctionStatement(tree);
	}
	
	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		long count = tree.descendants().filter(c->c.is(Kind.RETURN_STATEMENT)).count();
		if (count>max){
			addIssue(tree, "Reduce the number of returns of this procedure " + count + ", down to the maximum allowed " + max + ".");
		}
		super.visitCreateProcedureStatement(tree);
	}
	
}
