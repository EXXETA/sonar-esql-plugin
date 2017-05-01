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

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhileStatementTreeImpl extends EsqlTree implements WhileStatementTree{
	LabelTreeImpl label;
	InternalSyntaxToken colon;
	InternalSyntaxToken whileKeyword;
	ExpressionTree condition;
	InternalSyntaxToken doKeyword;
	StatementsTreeImpl statements;
	InternalSyntaxToken endKeyword;
	InternalSyntaxToken whileKeyword2;
	LabelTreeImpl label2;
	InternalSyntaxToken semi;
	public WhileStatementTreeImpl(LabelTreeImpl label, InternalSyntaxToken colon, InternalSyntaxToken whileKeyword,
			ExpressionTree condition, InternalSyntaxToken doKeyword, StatementsTreeImpl statements,
			InternalSyntaxToken endKeyword, InternalSyntaxToken whileKeyword2, LabelTreeImpl label2,
			InternalSyntaxToken semi) {
		super();
		this.label = label;
		this.colon = colon;
		this.whileKeyword = whileKeyword;
		this.condition = condition;
		this.doKeyword = doKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.whileKeyword2 = whileKeyword2;
		this.label2 = label2;
		this.semi = semi;
	}
	public WhileStatementTreeImpl(InternalSyntaxToken whileKeyword, ExpressionTree condition,
			InternalSyntaxToken doKeyword, StatementsTreeImpl statements, InternalSyntaxToken endKeyword,
			InternalSyntaxToken whileKeyword2, InternalSyntaxToken semi) {
		super();
		this.whileKeyword = whileKeyword;
		this.condition = condition;
		this.doKeyword = doKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.whileKeyword2 = whileKeyword2;
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
	public InternalSyntaxToken whileKeyword() {
		return whileKeyword;
	}

	@Override
	public ExpressionTree condition() {
		return condition;
	}
	
	@Override
	public InternalSyntaxToken doKeyword() {
		return doKeyword;
	}

	@Override
	public StatementsTreeImpl statements() {
		return statements;
	}
	
	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken whileKeyword2() {
		return whileKeyword2;
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
		visitor.visitWhileStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.WHILE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(label, colon, whileKeyword, condition, doKeyword, statements,
				endKeyword, whileKeyword2, label2, semi);
	}


}
