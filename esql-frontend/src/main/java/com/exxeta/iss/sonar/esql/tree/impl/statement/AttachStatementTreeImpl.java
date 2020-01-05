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

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.AttachStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class AttachStatementTreeImpl extends EsqlTree implements AttachStatementTree {

	private SyntaxToken attachKeyword;

	private FieldReferenceTree dynamicReference;

	private SyntaxToken toKeyword;

	private FieldReferenceTree fieldReference;

	private SyntaxToken asKeyword;

	private SyntaxToken location;

	private SyntaxToken semi;

	public AttachStatementTreeImpl(SyntaxToken attachKeyword, FieldReferenceTree dynamicReference,
			SyntaxToken toKeyword, FieldReferenceTree fieldReference, SyntaxToken asKeyword, SyntaxToken location,
			SyntaxToken semi) {
		super();
		this.attachKeyword = attachKeyword;
		this.dynamicReference = dynamicReference;
		this.toKeyword = toKeyword;
		this.fieldReference = fieldReference;
		this.asKeyword = asKeyword;
		this.location = location;
		this.semi = semi;
	}

	@Override
	public SyntaxToken attachKeyword() {
		return attachKeyword;
	}

	@Override
	public FieldReferenceTree dynamicReference() {
		return dynamicReference;
	}

	@Override
	public SyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public FieldReferenceTree fieldReference() {
		return fieldReference;
	}

	@Override
	public SyntaxToken asKeyword() {
		return asKeyword;
	}

	@Override
	public SyntaxToken location() {
		return location;
	}

	@Override
	public SyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAttachStatement(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.ATTACH_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(attachKeyword, dynamicReference, toKeyword, fieldReference, asKeyword, location, semi);
	}

	
	
}
