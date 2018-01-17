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
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "TooManyParameters")
public class TooManyParametersCheck extends DoubleDispatchVisitorCheck {

	private static final int DEFAULT_MAXIMUM = 7;

	@RuleProperty(key = "max", description = "Maximum authorized number of parameters", defaultValue = ""
			+ DEFAULT_MAXIMUM)
	public int maximum = DEFAULT_MAXIMUM;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {

		int size = tree.parameterList().size();
		if (size > maximum) {
			addIssue(new PreciseIssue(this, new IssueLocation(tree.openingParenthesis(), tree.closingParenthesis(),  "Function has " + size + " parameters, which is greater than " + maximum + " authorized.")));
		}
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		int size = tree.parameterList().size();
		if (size > maximum) {
			addIssue(new PreciseIssue(this, new IssueLocation(tree.openingParenthesis(), tree.closingParenthesis(), "Procedure has " + size + " parameters, which is greater than " + maximum + " authorized.")));
		}
	}

}
