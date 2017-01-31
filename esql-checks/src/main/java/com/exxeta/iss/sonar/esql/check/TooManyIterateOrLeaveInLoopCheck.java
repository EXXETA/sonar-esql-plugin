package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.annotation.Nullable;

import org.sonar.check.Rule;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.parser.EsqlGrammar;
import com.google.common.base.Objects;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(key = "TooManyIterateOrLeaveInLoop")
public class TooManyIterateOrLeaveInLoopCheck extends DoubleDispatchVisitorCheck {

	  private static final String MESSAGE = "Reduce the total number of \"ITERATE\" and \"LEAVE\" statements in this loop to use one at most.";

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
	    increaseNumberOfJumpInScopes(tree.continueKeyword(), tree.label());
	    super.visitContinueStatement(tree);
	  }

	  @Override
	  public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
	    enterScope();
	    super.visitCreateFunctionStatement(tree);
	    leaveScope();
	  }

	  @Override
	  public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
	    enterScope();
	    super.visitCreateProcedureStatement(tree);
	    leaveScope();
	  }

	  @Override
	  public void visitForStatement(ForStatementTree tree) {
	    enterScope();
	    super.visitForStatement(tree);
	    leaveScopeAndCheckNumberOfJump(tree.forKeyword());
	  }

	  @Override
	  public void visitWhileStatement(WhileStatementTree tree) {
	    enterScope();
	    super.visitWhileStatement(tree);
	    leaveScopeAndCheckNumberOfJump(tree.whileKeyword());
	  }

	  @Override
	  public void visitDoWhileStatement(DoWhileStatementTree tree) {
	    enterScope();
	    super.visitDoWhileStatement(tree);
	    leaveScopeAndCheckNumberOfJump(tree.doKeyword());
	  }

	  private void enterScope() {
	    jumpTargets.push(new JumpTarget());
	  }

	  private void leaveScope() {
	    jumpTargets.pop();
	  }

	  private void increaseNumberOfJumpInScopes(SyntaxToken jump, @Nullable IdentifierTree label) {
	    for (JumpTarget jumpTarget : jumpTargets) {
	      String labelName = label == null ? null : label.name();
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
	
	@Override
	public void init() {
		subscribeTo(EsqlGrammar.beginEndStatement, EsqlGrammar.whileStatement, EsqlGrammar.repeatStatement,
				EsqlGrammar.loopStatement);
	}


	@Override
	public void visitNode(AstNode astNode) {
		if (astNode.getFirstChild(EsqlGrammar.LABEL)!=null){
			String labelName = astNode.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue();
			int countIterate = 0;
			int countLeave = 0;
			for (AstNode statement: astNode.getDescendants(EsqlGrammar.iterateStatement)){
				String innerLabel = statement.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue();
				if (innerLabel.equals(labelName)){
					countIterate++;
				}
			}
			for (AstNode statement: astNode.getDescendants(EsqlGrammar.leaveStatement)){
				String innerLabel = statement.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue();
				if (innerLabel.equals(labelName)){
					countLeave++;
				}
			}
			if (countIterate>1 || countLeave>1){
				getContext().createLineViolation(this, "Loops should not contain more than a single \"ITERATE\" or \"LEAVE\" statement.", astNode);
			}
		}
	}*/
}