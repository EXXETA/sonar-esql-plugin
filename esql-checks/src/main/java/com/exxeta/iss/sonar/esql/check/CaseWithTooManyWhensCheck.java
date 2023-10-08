/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
