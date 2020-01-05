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

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class RepeatStatementTreeImpl extends EsqlTree implements RepeatStatementTree {
	LabelTreeImpl label;
	InternalSyntaxToken colon;
	InternalSyntaxToken repeatKeyword;
	StatementsTreeImpl statements;
	InternalSyntaxToken untilKeyword;
	ExpressionTree condition;
	InternalSyntaxToken endKeyword;
	InternalSyntaxToken repeatKeyword2;
	LabelTreeImpl label2;
	InternalSyntaxToken semi;

	public RepeatStatementTreeImpl(LabelTreeImpl label, InternalSyntaxToken colon, InternalSyntaxToken repeatKeyword,
			StatementsTreeImpl statements, InternalSyntaxToken untilKeyword, ExpressionTree condition,
			InternalSyntaxToken endKeyword, InternalSyntaxToken repeatKeyword2, LabelTreeImpl label2,
			InternalSyntaxToken semi) {
		super();
		this.label = label;
		this.colon = colon;
		this.repeatKeyword = repeatKeyword;
		this.statements = statements;
		this.untilKeyword = untilKeyword;
		this.condition = condition;
		this.endKeyword = endKeyword;
		this.repeatKeyword2 = repeatKeyword2;
		this.label2 = label2;
		this.semi = semi;
	}

	public RepeatStatementTreeImpl(InternalSyntaxToken repeatKeyword, StatementsTreeImpl statements,
			InternalSyntaxToken untilKeyword, ExpressionTree condition, InternalSyntaxToken endKeyword,
			InternalSyntaxToken repeatKeyword2, InternalSyntaxToken semi) {
		super();
		this.repeatKeyword = repeatKeyword;
		this.statements = statements;
		this.untilKeyword = untilKeyword;
		this.condition = condition;
		this.endKeyword = endKeyword;
		this.repeatKeyword2 = repeatKeyword2;
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
	public InternalSyntaxToken repeatKeyword() {
		return repeatKeyword;
	}

	@Override
	public StatementsTreeImpl statements() {
		return statements;
	}

	@Override
	public InternalSyntaxToken untilKeyword() {
		return untilKeyword;
	}

	@Override
	public ExpressionTree condition() {
		return condition;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken repeatKeyword2() {
		return repeatKeyword2;
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
		visitor.visitRepeatStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.REPEAT_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(label, colon, repeatKeyword, statements,
				untilKeyword, condition, endKeyword, repeatKeyword2, label2, semi);
	}

}
