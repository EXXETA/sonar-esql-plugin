/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.metrics.ComplexityVisitor;

/**
 * This java class is created to implement the logic for checking cyclomatic
 * complexity of the code. cyclomatic complexity should be less than the
 * threshold value
 * 
 * @author sapna singh
 *
 */
@Rule(key = "CyclomaticComplexity")
public class CyclomaticComplexityCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Refactor this %s to reduce its Cognitive Complexity from %s to the %s allowed.";

	private static final int DEFAULT_COMPLEXITY_THRESHOLD = 10;

	@RuleProperty(key = "maximumCyclomaticComplexity", description = "The maximum authorized cyclomatic complexity.", defaultValue = ""
			+ DEFAULT_COMPLEXITY_THRESHOLD)
	public int maximumCyclomaticComplexity = DEFAULT_COMPLEXITY_THRESHOLD;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		visitRoutine(tree);

	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		visitRoutine(tree);
	}

	private void visitRoutine(RoutineDeclarationTree tree) {
		int complexity = new ComplexityVisitor().getComplexity(tree);
		if (complexity > maximumCyclomaticComplexity) {
			raiseIssue(complexity, tree);
		}
	}

	private void raiseIssue(int complexity, Tree routine) {
		String message = String.format(MESSAGE,
				routine instanceof CreateFunctionStatementTree ? "function" : "procedure", complexity,
				maximumCyclomaticComplexity);

		SyntaxToken primaryLocation = routine.firstToken();

		addIssue(primaryLocation, message).cost(complexity - (double) maximumCyclomaticComplexity);

	}

}
