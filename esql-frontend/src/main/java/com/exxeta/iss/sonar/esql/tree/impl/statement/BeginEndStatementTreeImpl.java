/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class BeginEndStatementTreeImpl extends EsqlTree implements BeginEndStatementTree {

	private LabelTreeImpl labelName1;
	private InternalSyntaxToken colon;
	private InternalSyntaxToken beginKeyword;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken atomicKeyword;
	private StatementsTreeImpl statements;
	private InternalSyntaxToken endKeyword;
	private LabelTreeImpl labelName2;
	private InternalSyntaxToken semiToken;

	public BeginEndStatementTreeImpl(LabelTreeImpl labelName1, InternalSyntaxToken colon,
			InternalSyntaxToken beginKeyword, InternalSyntaxToken notKeyword, InternalSyntaxToken atomicKeyword,
			StatementsTreeImpl statements, InternalSyntaxToken endKeyword, LabelTreeImpl labelName2, InternalSyntaxToken semiToken) {
		super();
		this.labelName1 = labelName1;
		this.colon = colon;
		this.beginKeyword = beginKeyword;
		this.notKeyword = notKeyword;
		this.atomicKeyword = atomicKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.labelName2 = labelName2;
		this.semiToken=semiToken;
	}

	public BeginEndStatementTreeImpl(InternalSyntaxToken beginKeyword, InternalSyntaxToken notKeyword,
			InternalSyntaxToken atomicKeyword, StatementsTreeImpl statements, InternalSyntaxToken endKeyword, InternalSyntaxToken semiToken) {
		super();
		this.beginKeyword = beginKeyword;
		this.notKeyword = notKeyword;
		this.atomicKeyword = atomicKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.semiToken=semiToken;
	}

	@Override
	public LabelTreeImpl labelName1() {
		return labelName1;
	}

	@Override
	public InternalSyntaxToken colon() {
		return colon;
	}

	@Override
	public InternalSyntaxToken beginKeyword() {
		return beginKeyword;
	}

	@Override
	public InternalSyntaxToken notKeyword() {
		return notKeyword;
	}

	@Override
	public InternalSyntaxToken atomicKeyword() {
		return atomicKeyword;
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
	public LabelTreeImpl labelName2() {
		return labelName2;
	}

	@Override
	public InternalSyntaxToken semiToken() {
		return semiToken;
	}
	
	@Override
	public Kind getKind() {
		return Kind.BEGIN_END_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(labelName1, colon, beginKeyword, notKeyword, atomicKeyword,
				statements, endKeyword, labelName2, semiToken);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitBeginEndStatement(this);
	}

}
