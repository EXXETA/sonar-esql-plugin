/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LinesOfCodeVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * This java class is created to implement the logic for checking the length of
 * the function or procedure
 * 
 * @author Sapna Singh
 *
 */
@Rule(key = "FunctionProcedureLength")
public class FunctionProcedureLengthCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "This routine has %s lines, which is greater than the %s lines authorized.";

	private static final int DEFAULT_LENGTH_THRESHOLD = 150;
	 @RuleProperty(
			    key = "maximumMethodProcedureLength",
			    description = "The maximum authorized method/procedure length.",
			    defaultValue = "" + DEFAULT_LENGTH_THRESHOLD)
	 public int maximumMethodProcedureLength = DEFAULT_LENGTH_THRESHOLD;
	 
	 
	 
	 @Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		checkRoutineLength(tree);
		super.visitCreateFunctionStatement(tree);
	}
	 
	 @Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		checkRoutineLength(tree);

		 
		super.visitCreateProcedureStatement(tree);
	}

	private void checkRoutineLength(RoutineDeclarationTree routineDeclarationTree) {
		if (routineDeclarationTree.routineBody()!=null && routineDeclarationTree.routineBody().statement() instanceof BeginEndStatementTree){
			
		 BeginEndStatementTree body = (BeginEndStatementTree) routineDeclarationTree.routineBody().statement();
		 
		 int nbLines = new LinesOfCodeVisitor().linesOfCode(body);
		    if (nbLines > maximumMethodProcedureLength) {
		      String message = String.format(MESSAGE, nbLines, maximumMethodProcedureLength);
		      IssueLocation primaryLocation = new IssueLocation(routineDeclarationTree, message);
		      addIssue(new PreciseIssue(this, primaryLocation));
		    }
		}
		
	}

}
