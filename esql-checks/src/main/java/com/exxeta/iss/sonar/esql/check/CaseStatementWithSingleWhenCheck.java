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

import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * This java class is created to check the case statement, if CASE statement contains single when , then it should be replaced with If statement.
 * @author sapna singh
 *
 */
@Rule(key = "CaseStatementWithSingleWhen")
public class CaseStatementWithSingleWhenCheck extends DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Replace this \"CASE\" statement by \"if\" statements to increase readability";
	
	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		if ((tree.whenClauses().size())==1){
			
			addIssue(new PreciseIssue(this, new IssueLocation(tree,   MESSAGE )));
		}
	}
}
	
	







