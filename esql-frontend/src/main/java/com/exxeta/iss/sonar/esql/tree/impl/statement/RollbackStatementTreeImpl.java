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
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RollbackStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;
import java.util.Iterator;

public class RollbackStatementTreeImpl extends EsqlTree implements RollbackStatementTree {

	private InternalSyntaxToken rollbackKeyword;
	private FieldReferenceTreeImpl databaseReference;
	private InternalSyntaxToken semi;

	public RollbackStatementTreeImpl(InternalSyntaxToken rollbackKeyword,
                                     FieldReferenceTreeImpl databaseReference, InternalSyntaxToken semi) {
		super();
		this.rollbackKeyword=rollbackKeyword;
		this.databaseReference = databaseReference;
		this.semi=semi;
	}

	@Override
	public InternalSyntaxToken rollbackKeyword() {
		return rollbackKeyword;
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
		visitor.visitRollbackStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.ROLLBACK_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(rollbackKeyword, databaseReference, semi);
	}
	
	

}
