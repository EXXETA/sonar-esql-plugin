package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PropagateStatementTreeImpl extends EsqlTree implements PropagateStatementTree {

	private InternalSyntaxToken propagateKeyword;
	private InternalSyntaxToken toKeyword;
	private InternalSyntaxToken targetType;
	private ExpressionTree target;
	private MessageSourceTreeImpl messageSource;
	private ControlsTreeImpl controls;
	private InternalSyntaxToken semi;

	public PropagateStatementTreeImpl(InternalSyntaxToken propagateKeyword, MessageSourceTreeImpl messageSource,
			ControlsTreeImpl controls, InternalSyntaxToken semi) {
		this.propagateKeyword = propagateKeyword;
		this.toKeyword = null;
		this.targetType = null;
		this.target = null;
		this.messageSource = messageSource;
		this.controls = controls;
		this.semi=semi;
	}

	public PropagateStatementTreeImpl(InternalSyntaxToken propagateKeyword, InternalSyntaxToken toKeyword,
			InternalSyntaxToken targetType, ExpressionTree target, MessageSourceTreeImpl messageSource,
			ControlsTreeImpl controls, InternalSyntaxToken semi) {
		this.propagateKeyword = propagateKeyword;

		this.toKeyword = toKeyword;
		this.toKeyword = toKeyword;
		this.targetType = targetType;
		this.target = target;
		this.messageSource = messageSource;
		this.controls = controls;
		this.semi=semi;
	}

	public InternalSyntaxToken propagateKeyword() {
		return propagateKeyword;
	}

	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	public InternalSyntaxToken targetType() {
		return targetType;
	}

	public ExpressionTree target() {
		return target;
	}

	public MessageSourceTreeImpl messageSource() {
		return messageSource;
	}

	public ControlsTreeImpl controls() {
		return controls;
	}
	
	public InternalSyntaxToken semi() {
		return semi;
	}

	  @Override
	  public Kind getKind() {
	    return Kind.PROPAGATE_STATEMENT;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
		  return Iterators.forArray(propagateKeyword, toKeyword, targetType, target, messageSource, controls, semi);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitPropagateStatement(this);
	  }

}
