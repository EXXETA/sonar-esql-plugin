/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="RoutineWithExcessiveReturns")
public class RoutineWithExcessiveReturnsCheck extends DoubleDispatchVisitorCheck{
	private static final int DEFAULT_MAX = 3;

	  @RuleProperty(description = "Maximum allowed return statements per method", defaultValue = "" + DEFAULT_MAX)
	  public int max = DEFAULT_MAX;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		long count = tree.descendants().filter(c->c.is(Kind.RETURN_STATEMENT)).count();
		if (count>max){
			addIssue(tree, "Reduce the number of returns of this function " + count + ", down to the maximum allowed " + max + ".");
		}
		super.visitCreateFunctionStatement(tree);
	}
	
	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		long count = tree.descendants().filter(c->c.is(Kind.RETURN_STATEMENT)).count();
		if (count>max){
			addIssue(tree, "Reduce the number of returns of this procedure " + count + ", down to the maximum allowed " + max + ".");
		}
		super.visitCreateProcedureStatement(tree);
	}
	
}
