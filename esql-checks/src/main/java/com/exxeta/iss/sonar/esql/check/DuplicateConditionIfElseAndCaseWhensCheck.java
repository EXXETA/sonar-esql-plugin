/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;

@Rule(key = "DuplicateConditionIfElseAndCaseWhens")
public class DuplicateConditionIfElseAndCaseWhensCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "This %s duplicates the one on line %s.";

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		ExpressionTree condition = tree.condition();

		for (int i = 0; i < tree.elseifClauses().size(); i++) {
			if (SyntacticEquivalence.areEquivalent(condition, tree.elseifClauses().get(i).condition())) {
				addIssue(condition, tree.elseifClauses().get(i).condition(), "branch");
			}
			for (int j = 0; j < i; j++) {
				if (SyntacticEquivalence.areEquivalent(tree.elseifClauses().get(j).condition(),
						tree.elseifClauses().get(i).condition())) {
					addIssue(tree.elseifClauses().get(j).condition(), tree.elseifClauses().get(i).condition(), "branch");
				}
			}
		}

		super.visitIfStatement(tree);
	}

	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		for (int i = 0; i < tree.whenClauses().size(); i++) {
			for (int j = i + 1; j < tree.whenClauses().size(); j++) {
				ExpressionTree condition = tree.whenClauses().get(i).expression();
				ExpressionTree conditionToCompare = tree.whenClauses().get(j).expression();

				if (SyntacticEquivalence.areEquivalent(condition, conditionToCompare)) {
					addIssue(condition, conditionToCompare, "WHEN");
				}
			}
		}
	}

	@Override
	public void visitCaseFunction(CaseFunctionTree tree) {
		for (int i = 0; i < tree.whenClauses().size(); i++) {
			for (int j = i + 1; j < tree.whenClauses().size(); j++) {
				ExpressionTree condition = tree.whenClauses().get(i).expression();
				ExpressionTree conditionToCompare = tree.whenClauses().get(j).expression();

				if (SyntacticEquivalence.areEquivalent(condition, conditionToCompare)) {
					addIssue(condition, conditionToCompare, "when");
				}
			}
		}
	}

	private void addIssue(Tree original, Tree duplicate, String type) {
		IssueLocation secondaryLocation = new IssueLocation(original, "Original");
		String message = String.format(MESSAGE, type, secondaryLocation.startLine());
		addIssue(duplicate, message).secondary(secondaryLocation);
	}

}
