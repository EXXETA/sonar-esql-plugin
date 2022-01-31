/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

/**
 * This Java class is created to implement the logic to check whether process is
 * invoking itself.
 * 
 * @author Sapna Singh
 *
 */
@Rule(key = "Recursion")
public class RecursionCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "routine invoking itself.";
	private String routineName;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		routineName = tree.identifier().name();
		super.visitCreateFunctionStatement(tree);
		routineName=null;
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		routineName = tree.identifier().name();
		super.visitCreateProcedureStatement(tree);
		routineName=null;
	}

	@Override
	public void visitCallExpression(CallExpressionTree tree) {
		//TODO Check if firstToken can be replaced
		if (routineName!=null && tree.functionName()!=null && tree.functionName().firstToken()!=null &&  routineName.equals(tree.functionName().firstToken().text())){
			addIssue(new LineIssue(this, tree, MESSAGE));
		}
		super.visitCallExpression(tree);
	}
	
	@Override
	public void visitCallStatement(CallStatementTree tree) {
		//TODO Check if firstToken can be replaced
		if (routineName!=null && routineName.equals(tree.routineName().firstToken().text())){
			addIssue(new LineIssue(this, tree, MESSAGE));
		}
		super.visitCallStatement(tree);
	}

}
