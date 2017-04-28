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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class LoopStatementTreeImpl extends EsqlTree implements LoopStatementTree {

	private LabelTreeImpl label;
	private InternalSyntaxToken colon;
	private InternalSyntaxToken loopKeyword;
	private List<StatementTree> statements;
	private InternalSyntaxToken endKeyword;
	private InternalSyntaxToken loopKeyword2;
	private LabelTreeImpl label2;
	private InternalSyntaxToken semi;

	public LoopStatementTreeImpl(LabelTreeImpl label, InternalSyntaxToken colon, InternalSyntaxToken loopKeyword,
			List<StatementTree> statements, InternalSyntaxToken endKeyword, InternalSyntaxToken loopKeyword2,
			LabelTreeImpl label2, InternalSyntaxToken semi) {
		super();
		this.label = label;
		this.colon = colon;
		this.loopKeyword = loopKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.loopKeyword2 = loopKeyword2;
		this.label2 = label2;
		this.semi = semi;
	}

	public LoopStatementTreeImpl(InternalSyntaxToken loopKeyword, List<StatementTree> statements,
			InternalSyntaxToken endKeyword, InternalSyntaxToken loopKeyword2, InternalSyntaxToken semi) {
		super();
		this.loopKeyword = loopKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.loopKeyword2 = loopKeyword2;
		this.semi = semi;
	}

	@Override
	public LabelTreeImpl label() {
		return label;
	}

	@Override
	public InternalSyntaxToken colon() {
		return colon;
	}

	@Override
	public InternalSyntaxToken loopKeyword() {
		return loopKeyword;
	}

	@Override
	public List<StatementTree> statements() {
		return statements;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken loopKeyword2() {
		return loopKeyword2;
	}

	@Override
	public LabelTreeImpl label2() {
		return label2;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLoopStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.LOOP_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(label, colon, loopKeyword), statements.iterator(),
				Iterators.forArray(endKeyword, loopKeyword2, semi));
	}

}
