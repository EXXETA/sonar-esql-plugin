/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CommitStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;
import java.util.Iterator;

public class CommitStatementTreeImpl extends EsqlTree implements CommitStatementTree {

	private InternalSyntaxToken commitKeyword;
	private FieldReferenceTreeImpl databaseReference;
	private InternalSyntaxToken semi;

	public CommitStatementTreeImpl(InternalSyntaxToken commitKeyword,
									 FieldReferenceTreeImpl databaseReference, InternalSyntaxToken semi) {
		super();
		this.commitKeyword=commitKeyword;
		this.databaseReference = databaseReference;
		this.semi=semi;
	}

	@Override
	public InternalSyntaxToken commitKeyword() {
		return commitKeyword;
	}

	@Override
	public FieldReferenceTreeImpl databaseReference() {
		return databaseReference;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCommitStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.COMMIT_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(commitKeyword, databaseReference, semi);
	}
	

}
