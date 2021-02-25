/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.Map;
import java.util.Set;

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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@Rule(key="BooleanInversion")
public class BooleanInversionCheck extends SubscriptionVisitorCheck {

	private static final Map<String, String> OPERATORS = ImmutableMap.<String, String>builder()
			.put("<", ">=")
			.put(">", "<=")
			.put("<=", ">")
			.put(">=", "<")
			.build();

	@Override
	public Set<Kind> nodesToVisit() {
		return ImmutableSet.of(Tree.Kind.LOGICAL_COMPLEMENT);
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
