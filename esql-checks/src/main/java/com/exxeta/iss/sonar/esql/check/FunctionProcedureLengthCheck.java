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

import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.metrics.LineVisitor;

/**
 * This java class is created to implement the logic for checking the length of
 * the function or procedure
 * 
 * @author Sapna Singh
 *
 */
@Rule(key = "FunctionProcedureLength")
public class FunctionProcedureLengthCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "This routine has %s lines, which is greater than the %s lines authorized.";

	private static final int DEFAULT_LENGTH_THRESHOLD = 150;
	 @RuleProperty(
			    key = "maximumMethodProcedureLength",
			    description = "The maximum authorized method/procedure length.",
			    defaultValue = "" + DEFAULT_LENGTH_THRESHOLD)
	 public int maximumMethodProcedureLength = DEFAULT_LENGTH_THRESHOLD;
	 
	 
	 
	 @Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		checkRoutineLength(tree);
		super.visitCreateFunctionStatement(tree);
	}
	 
	 @Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		checkRoutineLength(tree);

		 
		super.visitCreateProcedureStatement(tree);
	}

	private void checkRoutineLength(RoutineDeclarationTree routineDeclarationTree) {
		if (routineDeclarationTree.routineBody()!=null && routineDeclarationTree.routineBody().statement() instanceof BeginEndStatementTree){
			
		 BeginEndStatementTree body = (BeginEndStatementTree) routineDeclarationTree.routineBody().statement();
		 
		 int nbLines = new LineVisitor(body).getLinesOfCodeNumber();
		    if (nbLines > maximumMethodProcedureLength) {
		      String message = String.format(MESSAGE, nbLines, maximumMethodProcedureLength);
		      IssueLocation primaryLocation = new IssueLocation(routineDeclarationTree, message);
		      addIssue(new PreciseIssue(this, primaryLocation));
		    }
		}
		
	}

}
