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

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class DeleteStatementTreeImpl extends EsqlTree implements DeleteStatementTree{

	private InternalSyntaxToken deleteKeyword;
	private InternalSyntaxToken qualifier;
	private InternalSyntaxToken ofKeyword;
	private FieldReferenceTree fieldReference;
	private InternalSyntaxToken semi;
	public DeleteStatementTreeImpl(InternalSyntaxToken deleteKeyword, InternalSyntaxToken qualifier,
			InternalSyntaxToken ofKeyword, FieldReferenceTree fieldReference, InternalSyntaxToken semi) {
		super();
		this.deleteKeyword = deleteKeyword;
		this.qualifier = qualifier;
		this.ofKeyword = ofKeyword;
		this.fieldReference = fieldReference;
		this.semi=semi;
	}
	@Override
	public InternalSyntaxToken deleteKeyword() {
		return deleteKeyword;
	}
	@Override
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}
	@Override
	public InternalSyntaxToken ofKeyword() {
		return ofKeyword;
	}
	@Override
	public FieldReferenceTree fieldReference() {
		return fieldReference;
	}
	@Override
	public InternalSyntaxToken semi(){
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDeleteStatement(this);
	}
	@Override
	public Kind getKind() {
		return Kind.DELETE_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(deleteKeyword, qualifier, ofKeyword, fieldReference, semi);
	}
	
	
}
