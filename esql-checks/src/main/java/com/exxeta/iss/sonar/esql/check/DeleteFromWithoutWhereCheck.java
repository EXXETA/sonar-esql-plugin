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

import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.expression.BinaryExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.CallExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.PassthruFunctionTreeImpl;

@Rule(key = "DeleteFromWithoutWhere")
public class DeleteFromWithoutWhereCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Add a where caluse to this DELETE FROM statement.";

	@Override
	public void visitDeleteFromStatement(DeleteFromStatementTree tree) {
		if (tree.whereExpression() == null) {
			addIssue(tree, MESSAGE);
		}
	}

	@Override
	public void visitPassthruStatement(PassthruStatementTree tree) {
		String statement = null;
		if (SyntacticEquivalence.skipParentheses(tree.expression()) instanceof LiteralTree) {
			statement = ((LiteralTree) SyntacticEquivalence.skipParentheses(tree.expression())).value().trim();
		} else if (tree.expressionList()!=null 
				&& !tree.expressionList().parameters().isEmpty() 
				&& SyntacticEquivalence
				.skipParentheses(tree.expressionList().parameters().get(0)) instanceof LiteralTree) {
			statement = ((LiteralTree) SyntacticEquivalence.skipParentheses(tree.expressionList().parameters().get(0)))
					.value().trim();
		} else {
			// Cannot find statement
		}
		//Very simple check. Think about an SQL parser.
		if (statement != null && statement.startsWith("'DELETE FROM") && !statement.contains(" WHERE ")) {
			addIssue(tree, MESSAGE);
		}
		super.visitPassthruStatement(tree);
	}
	
	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if(tree.expression() instanceof CallExpressionTreeImpl) {
			CallExpressionTreeImpl callExpression = (CallExpressionTreeImpl) tree.expression();
			if(callExpression.function() instanceof PassthruFunctionTreeImpl) {
				PassthruFunctionTreeImpl passthru = (PassthruFunctionTreeImpl) callExpression.function();				
				if(passthru.expression() instanceof BinaryExpressionTreeImpl) {
					BinaryExpressionTreeImpl binaryExpression = (BinaryExpressionTreeImpl) passthru.expression();
					if(!binaryExpression.firstToken().text().contains("WHERE")) {
						addIssue(tree, MESSAGE);
					}			
				} else {
					if (!passthru.expression().firstToken().text().concat(passthru.expression().lastToken().text()).contains("WHERE")) {
						addIssue(tree, MESSAGE);
					}			
				}
			}			
		}
		
		super.visitSetStatement(tree);
	}

}
