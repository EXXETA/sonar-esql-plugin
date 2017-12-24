/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

/**
 * @author C50679
 *
 */
@Rule(key = "FilterNodeHaveOnlyOneReturn")
public class FilterNodeHaveOnlyOneReturnCheck extends DoubleDispatchVisitorCheck {
	
	
	private static final String MESSAGE = "The filter module always returns the same value";
	private boolean insideFilterModule;
	private int trueCount = 0;
    private int falseCount = 0;
    private int returnOther = 0;
    private int throwsError = 0;
    
	
	
	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		if ("FILTER".equalsIgnoreCase(tree.moduleType().text())) {
			this.insideFilterModule = true;
			falseCount = trueCount = returnOther = throwsError = 0;
		}
		super.visitCreateModuleStatement(tree);
		boolean returnViolation = false;
		if (trueCount + falseCount + returnOther + throwsError == 0)
			returnViolation = true;
		if (trueCount == 0 && returnOther == 0)
			returnViolation = true;
		if (falseCount == 0 && returnOther == 0 && throwsError == 0)
			returnViolation = true;
		if (returnViolation) {

			addIssue(new PreciseIssue(this, new IssueLocation(tree, MESSAGE)));
		}

		this.insideFilterModule=false;
	}
	
	@Override
	public void visitReturnStatement(ReturnStatementTree tree) {
		if (insideFilterModule) {
			if (tree.expression().is(Kind.BOOLEAN_LITERAL)) {
				LiteralTree literal = (LiteralTree) tree.expression();
				if ("TRUE".equalsIgnoreCase(literal.value())) {
					trueCount++;
				} else {
					falseCount++;
				}
			} else {
				returnOther++;
			}
		}
		super.visitReturnStatement(tree);
	}

	@Override
	public void visitThrowStatement(ThrowStatementTree tree) {
		if (insideFilterModule) {
			throwsError++;
		}
		super.visitThrowStatement(tree);
	}
	
}
