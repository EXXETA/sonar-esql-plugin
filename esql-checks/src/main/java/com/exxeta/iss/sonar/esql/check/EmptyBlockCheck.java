/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ForStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "EmptyBlock")
public class EmptyBlockCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Either remove or fill this block of code.";

	@Override
	public void visitBeginEndStatement(BeginEndStatementTree tree) {
		checkBlock(tree.statements(), tree.endKeyword());
		super.visitBeginEndStatement(tree);
	}

	@Override
	public void visitElseClause(ElseClauseTree tree) {
		checkBlock(tree.statements(), ((IfStatementTree) tree.parent()).endKeyword());
		super.visitElseClause(tree);
	}

	@Override
	public void visitElseifClause(ElseifClauseTree tree) {
		IfStatementTree ifStatement = (IfStatementTree) tree.parent();
		int elseIfIndex = ifStatement.elseifClauses().indexOf(tree);
		if (ifStatement.elseifClauses().size() > elseIfIndex + 1) {
			// this tree is not the last elseif clause
			checkBlock(tree.statements(), ifStatement.elseifClauses().get(elseIfIndex + 1).elseifKeyword());
		} else if (ifStatement.elseClause() != null) {
			checkBlock(tree.statements(), ifStatement.elseClause().elseKeyword());
		} else {
			checkBlock(tree.statements(), ifStatement.endKeyword());
		}

		super.visitElseifClause(tree);
	}

	@Override
	public void visitForStatement(ForStatementTree tree) {
		checkBlock(tree.statements(), tree.endKeyword());
		super.visitForStatement(tree);
	}

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		SyntaxToken endToken = null;
		if (!tree.elseifClauses().isEmpty()) {
			endToken = tree.elseifClauses().get(0).elseifKeyword();
		} else if (tree.elseClause() != null) {
			endToken = tree.elseClause().elseKeyword();
		} else {
			endToken = tree.endKeyword();
		}
		checkBlock(tree.statements(), endToken);
		super.visitIfStatement(tree);
	}

	@Override
	public void visitLoopStatement(LoopStatementTree tree) {
		checkBlock(tree.statements(), tree.endKeyword());
		super.visitLoopStatement(tree);
	}

	@Override
	public void visitRepeatStatement(RepeatStatementTree tree) {
		checkBlock(tree.statements(), tree.untilKeyword());
		super.visitRepeatStatement(tree);
	}

	@Override
	public void visitWhenClause(WhenClauseTree tree) {
		CaseStatementTree caseStatement = (CaseStatementTree) tree.parent();
		int whenIndex = caseStatement.whenClauses().indexOf(tree);
		if (caseStatement.whenClauses().size()>whenIndex+1){
			//Not the last when clause
			checkBlock(tree.statements(), caseStatement.whenClauses().get(whenIndex+1).whenKeyword());
		} else if (caseStatement.elseKeyword()!=null) {
			checkBlock(tree.statements(), caseStatement.elseKeyword());
		} else {
			checkBlock(tree.statements(), caseStatement.endKeyword());
		}
		super.visitWhenClause(tree);
	}

	@Override
	public void visitWhileStatement(WhileStatementTree tree) {
		checkBlock(tree.statements(), tree.endKeyword());
		super.visitWhileStatement(tree);
	}

	private void checkBlock(StatementsTree statements, SyntaxToken endToken) {
		if (statements.statements().isEmpty() && !hasComment(endToken)) {
			addIssue(new PreciseIssue(this, new IssueLocation(statements.parent(), MESSAGE)));
		}

	}

	private static boolean hasComment(SyntaxToken endToken) {
		return !endToken.trivias().isEmpty();
	}
}
