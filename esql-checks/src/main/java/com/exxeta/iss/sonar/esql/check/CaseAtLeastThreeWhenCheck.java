package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "CaseAtLeastThreeWhenCheck")
public class CaseAtLeastThreeWhenCheck extends DoubleDispatchVisitorCheck {

	@Override
	public void visitCaseFunction(CaseFunctionTree tree) {
		if (tree.whenClauses().size() < 3) {
			addIssue(new PreciseIssue(this, new IssueLocation(tree,
					"Replace this \"case\" function by \"if\" statements to increase readability.")));

		}
	}

	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		if (tree.whenClauses().size()<3){
			addIssue(new PreciseIssue(this, new IssueLocation(tree,
					"Replace this \"case\" statement by \"if\" statements to increase readability.")));
		}
	}
}
