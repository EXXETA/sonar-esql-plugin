/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import java.util.ArrayList;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.tree.impl.expression.CallExpressionTreeImpl;

@Rule(key = "CardinalityInLoop")
public class CardinalityInLoopCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Avoid using CARDINALITY in loops.";
	
	private List<Tree> reportedStatements = new ArrayList<>();

	@Override
	public void visitProgram(ProgramTree tree) {
		reportedStatements.clear();
		super.visitProgram(tree);
	}
	
	@Override
	public void visitWhileStatement(WhileStatementTree tree) {
		checkCardinalityInDecendants(tree);
		super.visitWhileStatement(tree);
	}

	@Override
	public void visitRepeatStatement(RepeatStatementTree tree) {
		checkCardinalityInDecendants(tree);
		super.visitRepeatStatement(tree);
	}

	@Override
	public void visitLoopStatement(LoopStatementTree tree) {
		checkCardinalityInDecendants(tree);
		super.visitLoopStatement(tree);
	}

	private void checkCardinalityInDecendants(Tree tree) {
		tree.descendants().filter(d -> d.is(Kind.CALL_EXPRESSION))
			.filter(d -> (((CallExpressionTreeImpl) d).functionName()!=null && ((CallExpressionTreeImpl) d).functionName().is(Kind.IDENTIFIER_REFERENCE)))
				.filter(d -> "CARDINALITY".equalsIgnoreCase(
						(((IdentifierTree)((CallExpressionTreeImpl) d).functionName()).name())))
				.forEach(d -> addIssue(d, MESSAGE));
	}

	@Override
	public PreciseIssue addIssue(Tree tree, String message) {
		if (!reportedStatements.contains(tree)){
			reportedStatements.add(tree);
			return super.addIssue(tree, message);
		} else {
			return null;
		}
	}

}
