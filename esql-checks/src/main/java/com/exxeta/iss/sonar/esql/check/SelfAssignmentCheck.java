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

import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;

@Rule(key = "SelfAssignment")
public class SelfAssignmentCheck extends DoubleDispatchVisitorCheck {

  private static final String MESSAGE = "Remove or correct this useless self-assignment.";

  @Override
	public void visitSetStatement(SetStatementTree tree) {

	    if ( SyntacticEquivalence.areEquivalent( tree.variableReference(), tree.expression())) {
	      addIssue(tree, MESSAGE);
	    }

	super.visitSetStatement(tree);
	}

}
