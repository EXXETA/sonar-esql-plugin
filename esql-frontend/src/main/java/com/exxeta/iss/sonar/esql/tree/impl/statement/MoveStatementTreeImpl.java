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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MoveStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class MoveStatementTreeImpl extends EsqlTree implements MoveStatementTree{

	private final InternalSyntaxToken moveKeyword;
	private final InternalSyntaxToken target;
	private final InternalSyntaxToken toKeyword;
	private final FieldReferenceTreeImpl sourceFieldReference;
	private final InternalSyntaxToken qualifier;
	private final NameClausesTreeImpl nameClauses;
	private final InternalSyntaxToken semi;
	
	public MoveStatementTreeImpl(InternalSyntaxToken moveKeyword, InternalSyntaxToken target,
			InternalSyntaxToken toKeyword, FieldReferenceTreeImpl sourceFieldReference, InternalSyntaxToken semi) {
		super();
		this.moveKeyword = moveKeyword;
		this.target = target;
		this.toKeyword = toKeyword;
		this.sourceFieldReference = sourceFieldReference;
		this.qualifier = null;
		this.nameClauses = null;
		this.semi = semi;
	}

	public MoveStatementTreeImpl(InternalSyntaxToken moveKeyword, InternalSyntaxToken target,
			InternalSyntaxToken qualifier, InternalSyntaxToken semi) {
		super();
		this.moveKeyword = moveKeyword;
		this.target = target;
		this.toKeyword = null;
		this.sourceFieldReference = null;
		this.qualifier = qualifier;
		this.nameClauses = null;
		this.semi = semi;
	}

	public MoveStatementTreeImpl(InternalSyntaxToken moveKeyword, InternalSyntaxToken target,
			InternalSyntaxToken qualifier, NameClausesTreeImpl nameClauses, InternalSyntaxToken semi) {
		super();
		this.moveKeyword = moveKeyword;
		this.target = target;
		this.toKeyword = null;
		this.sourceFieldReference = null;
		this.qualifier = qualifier;
		this.nameClauses = nameClauses;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken moveKeyword() {
		return moveKeyword;
	}

	@Override
	public InternalSyntaxToken target() {
		return target;
	}

	@Override
	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public FieldReferenceTreeImpl sourceFieldReference() {
		return sourceFieldReference;
	}

	@Override
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}

	@Override
	public NameClausesTreeImpl nameClauses() {
		return nameClauses;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitMoveStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.MOVE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(moveKeyword, target, toKeyword, sourceFieldReference, qualifier, nameClauses, semi);
	}
	
	
	

	
	
	
	
	
	
}
