package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "CaseWithTooManyWhens")
public class CaseWithTooManyWhensCheck extends DoubleDispatchVisitorCheck {
	private static final int DEFAULT_MAXIMUM_WHENS = 30;

	@RuleProperty(key = "maximum", description = "Maximum number of when", defaultValue = "" + DEFAULT_MAXIMUM_WHENS)
	public int maximumWhens = DEFAULT_MAXIMUM_WHENS;

	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		int size = tree.whenClauses().size();
		if (size > maximumWhens) {
			addIssue(tree, "Reduce the number of whens from " + size + " to at most " + maximumWhens + ".");
		}
		super.visitCaseStatement(tree);
	}
	
	@Override
	public void visitCaseFunction(CaseFunctionTree tree) {
		int size = tree.whenClauses().size();
		if (size > maximumWhens) {
			addIssue(tree, "Reduce the number of whens from " + size + " to at most " + maximumWhens + ".");
		}
		super.visitCaseFunction(tree);
		
	}
}
