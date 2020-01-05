/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
		this.targetType = targetType;
		this.target = target;
		this.messageSource = messageSource;
		this.controls = controls;
		this.semi=semi;
	}

	@Override
	public InternalSyntaxToken propagateKeyword() {
		return propagateKeyword;
	}

	@Override
	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public InternalSyntaxToken targetType() {
		return targetType;
	}

	@Override
	public ExpressionTree target() {
		return target;
	}

	@Override
	public MessageSourceTreeImpl messageSource() {
		return messageSource;
	}

	@Override
	public ControlsTreeImpl controls() {
		return controls;
	}
	
	@Override
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
