package com.exxeta.iss.sonar.esql.check;

import java.util.List;
import java.util.Map;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@Rule(key="BooleanInversion")
public class BooleanInversionCheck extends SubscriptionVisitorCheck {

	private static final Map<String, String> OPERATORS = ImmutableMap.<String, String>builder()
			.put("<", ">=")
			.put(">", "<=")
			.put("<=", ">")
			.put(">=", "<")
			.build();

	@Override
	public List<Kind> nodesToVisit() {
		return ImmutableList.of(Tree.Kind.LOGICAL_COMPLEMENT);
	}
	
	@Override
	  public void visitNode(Tree tree) {
	    ExpressionTree expression = SyntacticEquivalence.skipParentheses(((UnaryExpressionTree) tree).expression());
	    if (expression.is(
	        Tree.Kind.EQUAL_TO, Tree.Kind.NOT_EQUAL_TO,
	        Tree.Kind.LESS_THAN, Tree.Kind.GREATER_THAN,
	        Tree.Kind.LESS_THAN_OR_EQUAL_TO, Tree.Kind.GREATER_THAN_OR_EQUAL_TO)) {
	    	addIssue(new PreciseIssue(this, new IssueLocation(tree, "Use the opposite operator (\"" + OPERATORS.get(((BinaryExpressionTree) expression).operator().text()) + "\") instead.")));
	    }
	  }

}
