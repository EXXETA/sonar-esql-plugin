package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PropagateStatementTreeImpl extends EsqlTree implements PropagateStatementTree {

	private InternalSyntaxToken propagateKeyword;
	private InternalSyntaxToken toKeyword;
	private InternalSyntaxToken targetType;
	private InternalSyntaxToken targetName;
	private MessageSourceTreeImpl messageSource;
	private ControlsTreeImpl controls;

	public PropagateStatementTreeImpl(InternalSyntaxToken propagateKeyword, MessageSourceTreeImpl messageSource,
			ControlsTreeImpl controls) {
		this.propagateKeyword = propagateKeyword;
		this.toKeyword = null;
		this.targetType = null;
		this.targetName = null;
		this.messageSource = messageSource;
		this.controls = controls;
	}

	public PropagateStatementTreeImpl(InternalSyntaxToken propagateKeyword, InternalSyntaxToken toKeyword,
			InternalSyntaxToken targetType, InternalSyntaxToken targetName, MessageSourceTreeImpl messageSource,
			ControlsTreeImpl controls) {
		this.propagateKeyword = propagateKeyword;

		this.toKeyword = toKeyword;
		this.toKeyword = toKeyword;
		this.targetType = targetType;
		this.targetName = targetName;
		this.messageSource = messageSource;
		this.controls = controls;
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

	public InternalSyntaxToken targetName() {
		return targetName;
	}

	public MessageSourceTreeImpl messageSource() {
		return messageSource;
	}

	public ControlsTreeImpl controls() {
		return controls;
	}

	  @Override
	  public Kind getKind() {
	    return Kind.PROPAGATE_STATEMENT;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
		  return Iterators.forArray(propagateKeyword, toKeyword, targetType, targetName, messageSource, controls);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitPropagateStatement(this);
	  }

}
