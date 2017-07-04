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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ForStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ForStatementTreeImpl extends EsqlTree implements ForStatementTree {
	private final InternalSyntaxToken forKeyword;
	private final InternalSyntaxToken correlationName;
	private final InternalSyntaxToken asKeyword;
	private final FieldReferenceTreeImpl fieldReference;
	private final InternalSyntaxToken doKeyword;
	private final StatementsTreeImpl statements;
	private final InternalSyntaxToken forKeyword2;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken semi;

	public ForStatementTreeImpl(InternalSyntaxToken forKeyword, InternalSyntaxToken correlationName,
			InternalSyntaxToken asKeyword, FieldReferenceTreeImpl fieldReference, InternalSyntaxToken doKeyword, StatementsTreeImpl statements,
			InternalSyntaxToken forKeyword2, InternalSyntaxToken endKeyword, InternalSyntaxToken semi) {
		super();
		this.forKeyword = forKeyword;
		this.correlationName = correlationName;
		this.asKeyword = asKeyword;
		this.fieldReference = fieldReference;
		this.doKeyword=doKeyword;
		this.statements = statements;
		this.forKeyword2 = forKeyword2;
		this.endKeyword = endKeyword;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}

	@Override
	public InternalSyntaxToken correlationName() {
		return correlationName;
	}

	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	@Override
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
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
	public InternalSyntaxToken forKeyword2() {
		return forKeyword2;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitForStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.FOR_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(forKeyword, correlationName, asKeyword, fieldReference,
				doKeyword, statements, endKeyword, forKeyword2, semi);
	}

}
