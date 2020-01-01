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

import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "UnreachableCode")
public class UnreachableCodeCheck extends DoubleDispatchVisitorCheck{

	  private static final String MESSAGE = "Remove the code after this statement.";

	
	@Override
	public void visitStatements(StatementsTree tree) {
		boolean unreachable = false;
		for (int i=0;i<tree.statements().size()-1;i++){ 
			StatementTree currentStatement = tree.statements().get(i);
			if (!unreachable && (currentStatement instanceof ThrowStatementTree || isJumpStatement(currentStatement))){
				unreachable = true;
				addIssue(currentStatement, MESSAGE);
			}
		}
		super.visitStatements(tree);
	}


	private boolean isJumpStatement(StatementTree tree) {
		return tree instanceof ReturnStatementTree 
				|| tree instanceof LeaveStatementTree
				|| tree instanceof IterateStatementTree;
	}
	
}
