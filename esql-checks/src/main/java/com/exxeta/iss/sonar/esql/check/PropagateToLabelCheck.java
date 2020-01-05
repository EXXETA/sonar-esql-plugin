/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "PropagateToLabel")
public class PropagateToLabelCheck extends DoubleDispatchVisitorCheck {

	
	private static final String MESSAGE = "Do not use PROPAGATE TO LABEL.";

	@Override
	public void visitPropagateStatement(PropagateStatementTree tree) {
		super.visitPropagateStatement(tree);
		if (tree.targetType()!=null && "LABEL".equalsIgnoreCase(tree.targetType().text())){
			addIssue(new PreciseIssue(this, new IssueLocation(tree, tree, MESSAGE)));
		}
	}

}
