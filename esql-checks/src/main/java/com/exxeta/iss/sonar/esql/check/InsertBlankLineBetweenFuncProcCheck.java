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

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateRoutineTreeImpl;

/**
 * This java class is created to implement the logic for checking there should be one blank line between procedure and function.
 * @author C50679
 *
 */
@Rule(key = "InsertBlankLineBetweenFuncProc")
public class InsertBlankLineBetweenFuncProcCheck extends DoubleDispatchVisitorCheck{

	private static final String MESSAGE = "Insert one blank line between functions and procedures.";


	
	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		StatementTree previousStatement = null;
		for (StatementTree statement : tree.moduleStatementsList().statements()) {
			if (statement instanceof CreateRoutineTreeImpl && previousStatement instanceof CreateRoutineTreeImpl) {
				if ((previousStatement.lastToken().endLine()>=statement.firstToken().line()-1)) {
					addIssue(new LineIssue(this, previousStatement.lastToken(), MESSAGE));
				}
			}
			previousStatement=statement;
		}
		if (previousStatement instanceof CreateRoutineTreeImpl && (previousStatement.lastToken().endLine() >= tree.endKeyword().line()-1)) {
			addIssue(new LineIssue(this, previousStatement.lastToken(), MESSAGE));
		}
		
		super.visitCreateModuleStatement(tree);
	}

}
