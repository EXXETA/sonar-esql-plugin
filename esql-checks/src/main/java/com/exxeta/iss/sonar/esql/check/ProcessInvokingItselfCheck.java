/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

/**
 * This Java class is created to implement the logic to check whether process is
 * invoking itself.
	 * 
 * @author C50679
 *
 */
@Rule(key = "ProcessInvokingItself")
public class ProcessInvokingItselfCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "process invoking itself.";
	private String routineName;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		routineName = tree.identifier().name();
		super.visitCreateFunctionStatement(tree);
		routineName=null;
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		routineName = tree.identifier().name();
		super.visitCreateProcedureStatement(tree);
		routineName=null;
	}

	@Override
	public void visitCallExpression(CallExpressionTree tree) {
		//TODO Check if firstToken can be replaced
		if (routineName!=null && routineName.equals(tree.functionName().firstToken().text())){
			addIssue(new LineIssue(this, tree, MESSAGE));
		}
		super.visitCallExpression(tree);
	}
	
	@Override
	public void visitCallStatement(CallStatementTree tree) {
		//TODO Check if firstToken can be replaced
		if (routineName!=null && routineName.equals(tree.routineName().firstToken().text())){
			addIssue(new LineIssue(this, tree, MESSAGE));
		}
		super.visitCallStatement(tree);
	}

}
