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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;

/**
 * This java class is created to check IF Statement.In Case of multiple IF Statement use ELSEIF or CASE WHEN clauses to get quicker drop-out.
 * @author sapna singh
 *
 */
@Rule(key="AvoidNestedIf")
public class AvoidNestedIfCheck extends  DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Avoid nested IF statements: use ELSEIF or CASE WHEN clauses to get quicker drop-out.";
	
	private Deque<SyntaxToken> ifNesting;

	private static final int MAXIMUM_NESTING_ALLOWED = 3;

	
	@Override
	public void visitIfStatement(IfStatementTree tree) {
		SyntaxToken ifKeyword = tree.ifKeyword();
		checkNestesdIf(ifKeyword);
		ifNesting.push(ifKeyword);
		super.visitIfStatement(tree);
		ifNesting.pop();
	}
	
	
	private void checkNestesdIf(Tree tree) {
	    int size = ifNesting.size();
	    if (size == MAXIMUM_NESTING_ALLOWED) {
    	PreciseIssue issue = new PreciseIssue(this, new IssueLocation(tree, tree, "This if has a nesting level of "
				+ (ifNesting.size()+1) + " which is higher than the maximum allowed " + MAXIMUM_NESTING_ALLOWED +  MESSAGE ));
    	
	      
	      for (SyntaxToken element : ifNesting) {
	    	  issue.secondary(element , "Nesting + 1");
	      }
	      addIssue(issue);
	    }
	  }

	@Override
	public List<Issue> scanFile(TreeVisitorContext context) {
		this.ifNesting = new ArrayDeque<>();
		return super.scanFile(context);
	}

}
	
	
	








