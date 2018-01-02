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

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "CaseAtLeastThreeWhen")
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
