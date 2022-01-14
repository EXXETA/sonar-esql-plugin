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

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

/**
 * @author Sapna Singh
 *
 */
@Rule(key = "FilterNodeHaveOnlyOneReturn")
public class FilterNodeHaveOnlyOneReturnCheck extends DoubleDispatchVisitorCheck {
	
	
	private static final String MESSAGE = "This filter module always returns the same value";
	private boolean insideFilterModule;
	private int trueCount = 0;
    private int falseCount = 0;
    private int returnOther = 0;
    private int throwsError = 0;
    
	
	
	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		if ("FILTER".equalsIgnoreCase(tree.moduleType().text())) {
			this.insideFilterModule = true;
			falseCount = trueCount = returnOther = throwsError = 0;
		}
		super.visitCreateModuleStatement(tree);
		if (this.insideFilterModule){
			boolean returnViolation = false;
			if (trueCount + falseCount + returnOther + throwsError == 0){   // no return or throw
				returnViolation = true;
			} 
			if (trueCount == 0 && returnOther == 0){                        // only false or throw
				returnViolation = true;
			} 
			if (falseCount == 0 && returnOther == 0 && throwsError == 0){   // only true
				returnViolation = true;
			}
			
			if (returnViolation) {
	
				addIssue(new PreciseIssue(this, new IssueLocation(tree, MESSAGE)));
			}
	
			this.insideFilterModule=false;
		}
	}
	
	@Override
	public void visitReturnStatement(ReturnStatementTree tree) {
		if (insideFilterModule) {
			if (tree.expression().is(Kind.BOOLEAN_LITERAL)) {
				LiteralTree literal = (LiteralTree) tree.expression();
				if ("TRUE".equalsIgnoreCase(literal.value())) {
					trueCount++;
				} else {
					falseCount++;
				}
			} else {
				returnOther++;
			}
		}
		super.visitReturnStatement(tree);
	}

	@Override
	public void visitThrowStatement(ThrowStatementTree tree) {
		if (insideFilterModule) {
			throwsError++;
		}
		super.visitThrowStatement(tree);
	}
	
}
