package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key="UselessParentheses")
public class UselessParenthesesCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitParenthesisedExpression(ParenthesisedExpressionTree tree) {
		super.visitParenthesisedExpression(tree);
		 addIssue(new PreciseIssue(this, new IssueLocation(tree.openParenthesis(), "Remove these useless parentheses.")));
	}
	
}
