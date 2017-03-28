/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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

import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "NestedIfDepth")
public class NestedIfDepthCheck extends DoubleDispatchVisitorCheck {

	private int nestingLevel = 0;

	private static final int DEFAULT_MAXIMUM_NESTING_LEVEL = 5;

	@RuleProperty(key = "maximumNestingLevel", description = "the maxmimum if depth.", defaultValue = ""
			+ DEFAULT_MAXIMUM_NESTING_LEVEL)
	public int maximumNestingLevel = DEFAULT_MAXIMUM_NESTING_LEVEL;

	public int getMaximumNestingLevel() {
		return maximumNestingLevel;
	}

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		nestingLevel++;
		if (nestingLevel == getMaximumNestingLevel() + 1) {
			addIssue(new PreciseIssue(this, new IssueLocation(tree, tree, "This if has a nesting level of "
					+ nestingLevel + ", which is higher than the maximum allowed " + maximumNestingLevel + ".")));
		}
		super.visitIfStatement(tree);
		nestingLevel--;
	}

}
