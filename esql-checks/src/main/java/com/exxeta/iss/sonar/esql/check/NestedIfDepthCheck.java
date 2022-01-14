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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;

@Rule(key = "NestedIfDepth")
public class NestedIfDepthCheck extends DoubleDispatchVisitorCheck {

	private Deque<SyntaxToken> nestingLevel;

	private static final int DEFAULT_MAXIMUM_NESTING_LEVEL = 5;

	@RuleProperty(key = "maximumNestingLevel", description = "the maxmimum if depth.", defaultValue = ""
			+ DEFAULT_MAXIMUM_NESTING_LEVEL)
	public int maximumNestingLevel = DEFAULT_MAXIMUM_NESTING_LEVEL;

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		SyntaxToken ifKeyword = tree.ifKeyword();
		checkNesting(ifKeyword);
		nestingLevel.push(ifKeyword);
		super.visitIfStatement(tree);
		nestingLevel.pop();
	}
	
	
	private void checkNesting(Tree tree) {
	    int size = nestingLevel.size();
	    if (size == maximumNestingLevel) {
    	PreciseIssue issue = new PreciseIssue(this, new IssueLocation(tree, tree, "This if has a nesting level of "
				+ (nestingLevel.size()+1) + ", which is higher than the maximum allowed " + maximumNestingLevel + "."));
    	
	      
	      for (SyntaxToken element : nestingLevel) {
	    	  issue.secondary(element , "Nesting + 1");
	      }
	      addIssue(issue);
	    }
	  }

	@Override
	public List<Issue> scanFile(TreeVisitorContext context) {
		this.nestingLevel = new ArrayDeque<>();
		return super.scanFile(context);
	}

}
