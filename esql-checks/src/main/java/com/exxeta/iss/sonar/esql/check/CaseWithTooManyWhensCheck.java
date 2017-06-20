package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "CaseWithTooManyWhens")
public class CaseWithTooManyWhensCheck extends DoubleDispatchVisitorCheck {
	private static final int DEFAULT_MAXIMUM_CASES = 30;

	@RuleProperty(key = "maximum", description = "Maximum number of case", defaultValue = "" + DEFAULT_MAXIMUM_CASES)
	public int maximumCases = DEFAULT_MAXIMUM_CASES;

	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		int size = tree.whenClauses().size();
		if (size > maximumCases) {
			addIssue(tree, "Reduce the number of switch cases from " + size + " to at most " + maximumCases + ".");
		}
		super.visitCaseStatement(tree);
	}
}
