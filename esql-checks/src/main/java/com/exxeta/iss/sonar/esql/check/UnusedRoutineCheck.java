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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "UnusedRoutine")
public class UnusedRoutineCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Remove the unused %s \"%s\".";

	private Set<String> calledRoutines = new HashSet<>();
	private HashMap<String, CreateProcedureStatementTree> declaredProcedures = new HashMap<>();
	private HashMap<String, CreateFunctionStatementTree> declaredFunctions = new HashMap<>();

	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		calledRoutines.clear();
		declaredFunctions.clear();
		declaredProcedures.clear();
		super.visitCreateModuleStatement(tree);
		for (String function : calledRoutines) {
			declaredFunctions.remove(function);
		}
		for (String procedure : calledRoutines) {
			declaredProcedures.remove(procedure);
		}
		
		for (Entry<String, CreateFunctionStatementTree> function : declaredFunctions.entrySet()){
			if (!"Main".equalsIgnoreCase(function.getKey())) {
				addIssue(new PreciseIssue(this, new IssueLocation(function.getValue(),
						function.getValue(), String.format(MESSAGE, "function", function.getKey()))));
			}
		}
		for (Entry<String, CreateProcedureStatementTree> procedure : declaredProcedures.entrySet()) {
			addIssue(new PreciseIssue(this, new IssueLocation(procedure.getValue(),
					procedure.getValue(), String.format(MESSAGE, "procedure", procedure.getKey()))));
		}
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		declaredProcedures.put(tree.identifier().text(), tree);
		super.visitCreateProcedureStatement(tree);
	}

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		declaredFunctions.put(tree.identifier().text(), tree);
		super.visitCreateFunctionStatement(tree);
	}

	@Override
	public void visitCallExpression(CallExpressionTree tree) {
		if (tree.functionName() != null) {
			calledRoutines.add(tree.functionName().pathElement().name().text());
		}
		super.visitCallExpression(tree);
	}

	@Override
	public void visitCallStatement(CallStatementTree tree) {
		if (tree.routineName() != null) {
			calledRoutines.add(tree.routineName().text());
		}
		super.visitCallStatement(tree);
	}
}
