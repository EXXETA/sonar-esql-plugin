/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhenClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.tree.impl.function.WhenClauseExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.WhenClauseTreeImpl;

@Rule(key = "CaseWithoutElse")
public class CaseWithoutElseCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Add the missing \"ELSE\" clause.";

	@Override
	public void visitCaseFunction(CaseFunctionTree tree) {

		if (tree.elseKeyword() == null) {
			List<WhenClauseExpressionTreeImpl> whens = tree.whenClauses();
			WhenClauseExpressionTree lastWhen = whens.get(whens.size() - 1);
			addIssue(new PreciseIssue(this, new IssueLocation(tree.caseKeyword(), lastWhen, MESSAGE)));
		}

		super.visitCaseFunction(tree);
	}

	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		if (tree.elseKeyword() == null) {
			List<WhenClauseTreeImpl> whens = tree.whenClauses();
			WhenClauseTree lastWhen = whens.get(whens.size() - 1);
			addIssue(new PreciseIssue(this, new IssueLocation(tree.caseKeyword(), lastWhen, MESSAGE)));
		}
		super.visitCaseStatement(tree);
	}

}
