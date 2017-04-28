/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.annotation.Nullable;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LabelTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.google.common.base.Objects;

@Rule(key = "TooManyIterateOrLeaveInLoop")
public class TooManyIterateOrLeaveInLoopCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Loops should not contain more than a single \"ITERATE\" or \"LEAVE\" statement.";

	private static class JumpTarget {
		private final String label;
		private List<Tree> jumps = new ArrayList<>();

		/**
		 * Creates unlabeled target.
		 */
		public JumpTarget() {
			this.label = null;
		}

		/**
		 * Creates labeled target.
		 */
		public JumpTarget(String label) {
			this.label = label;
		}
	}

	private Deque<JumpTarget> jumpTargets = new ArrayDeque<>();

	@Override
	public void visitProgram(ProgramTree tree) {
		jumpTargets.clear();
		super.visitProgram(tree);
	}

	@Override
	public void visitIterateStatement(IterateStatementTree tree) {
		increaseNumberOfJumpInScopes(tree.iterateKeyword(), tree.label());
		super.visitIterateStatement(tree);
	}

	@Override
	public void visitLeaveStatement(LeaveStatementTree tree) {
		increaseNumberOfJumpInScopes(tree.leaveKeyword(), tree.label());
		super.visitLeaveStatement(tree);
	}

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		enterScope(null);
		super.visitCreateFunctionStatement(tree);
		leaveScope();
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		enterScope(null);
		super.visitCreateProcedureStatement(tree);
		leaveScope();
	}

	@Override
	public void visitWhileStatement(WhileStatementTree tree) {
		if (tree.label() != null) {
			enterScope(tree.label().name().text());
		} else {
			enterScope(null);
		}
		super.visitWhileStatement(tree);
		leaveScopeAndCheckNumberOfJump(tree.whileKeyword());

	}

	@Override
	public void visitRepeatStatement(RepeatStatementTree tree) {
		if (tree.label() != null) {
			enterScope(tree.label().name().text());
		} else {
			enterScope(null);
		}
		super.visitRepeatStatement(tree);
		leaveScopeAndCheckNumberOfJump(tree.repeatKeyword());

	}

	@Override
	public void visitLoopStatement(LoopStatementTree tree) {
		if (tree.label() != null) {
			enterScope(tree.label().name().text());
		} else {
			enterScope(null);
		}
		super.visitLoopStatement(tree);
		leaveScopeAndCheckNumberOfJump(tree.loopKeyword());

	}

	@Override
	public void visitBeginEndStatement(BeginEndStatementTree tree) {
		if (tree.labelName1() != null) {
			enterScope(tree.labelName1().name().text());
		} else {
			enterScope(null);
		}
		super.visitBeginEndStatement(tree);
		leaveScopeAndCheckNumberOfJump(tree.beginKeyword());

	}

	private void enterScope(String label) {
		jumpTargets.push(new JumpTarget(label));
	}

	private void leaveScope() {
		jumpTargets.pop();
	}

	private void increaseNumberOfJumpInScopes(SyntaxToken jump, @Nullable LabelTree label) {
		for (JumpTarget jumpTarget : jumpTargets) {
			String labelName = label == null ? null : label.name().text();
			jumpTarget.jumps.add(jump);

			if (Objects.equal(labelName, jumpTarget.label)) {
				break;
			}
		}
	}

	private void leaveScopeAndCheckNumberOfJump(SyntaxToken loopKeyword) {
		List<Tree> jumps = jumpTargets.pop().jumps;
		int jumpStatementNumber = jumps.size();
		if (jumpStatementNumber > 1) {
			PreciseIssue issue = addIssue(loopKeyword, MESSAGE).cost((double) jumpStatementNumber - 1);
			for (Tree jump : jumps) {
				issue.secondary(new IssueLocation(jump));
			}
		}
	}

	/*
	 * 
	 * @Override public void init() { subscribeTo(EsqlGrammar.beginEndStatement,
	 * EsqlGrammar.whileStatement, EsqlGrammar.repeatStatement,
	 * EsqlGrammar.loopStatement); }
	 * 
	 * 
	 * @Override public void visitNode(AstNode astNode) { if
	 * (astNode.getFirstChild(EsqlGrammar.LABEL)!=null){ String labelName =
	 * astNode.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue(); int
	 * countIterate = 0; int countLeave = 0; for (AstNode statement:
	 * astNode.getDescendants(EsqlGrammar.iterateStatement)){ String innerLabel
	 * = statement.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue(); if
	 * (innerLabel.equals(labelName)){ countIterate++; } } for (AstNode
	 * statement: astNode.getDescendants(EsqlGrammar.leaveStatement)){ String
	 * innerLabel =
	 * statement.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue(); if
	 * (innerLabel.equals(labelName)){ countLeave++; } } if (countIterate>1 ||
	 * countLeave>1){ getContext().createLineViolation(this,
	 * "Loops should not contain more than a single \"ITERATE\" or \"LEAVE\" statement."
	 * , astNode); } } }
	 */
}