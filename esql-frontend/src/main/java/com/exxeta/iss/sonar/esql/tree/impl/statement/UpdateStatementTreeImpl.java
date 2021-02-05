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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.UpdateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class UpdateStatementTreeImpl extends EsqlTree implements UpdateStatementTree {
	private InternalSyntaxToken updateKeyword;

	private FieldReferenceTreeImpl tableReference;

	private InternalSyntaxToken asKeyword;

	private InternalSyntaxToken alias;

	private InternalSyntaxToken setKeyword;

	private SeparatedList<SetColumnTree> setColumns;

	private InternalSyntaxToken whereKeyword;

	private ExpressionTree whereExpression;

	private InternalSyntaxToken semi;

	public UpdateStatementTreeImpl(InternalSyntaxToken updateKeyword, FieldReferenceTreeImpl tableReference,
			InternalSyntaxToken asKeyword, InternalSyntaxToken alias, InternalSyntaxToken setKeyword,
			SeparatedList<SetColumnTree> setColumns, InternalSyntaxToken whereKeyword, ExpressionTree whereExpression,
			InternalSyntaxToken semi) {
		super();
		this.updateKeyword = updateKeyword;
		this.tableReference = tableReference;
		this.asKeyword = asKeyword;
		this.alias = alias;
		this.setKeyword = setKeyword;
		this.setColumns = setColumns;
		this.whereKeyword = whereKeyword;
		this.whereExpression = whereExpression;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken updateKeyword() {
		return updateKeyword;
	}

	@Override
	public FieldReferenceTreeImpl tableReference() {
		return tableReference;
	}

	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	@Override
	public InternalSyntaxToken alias() {
		return alias;
	}

	@Override
	public InternalSyntaxToken setKeyword() {
		return setKeyword;
	}

	@Override
	public SeparatedList<SetColumnTree> setColumns() {
		return setColumns;
	}

	@Override
	public InternalSyntaxToken whereKeyword() {
		return whereKeyword;
	}

	@Override
	public ExpressionTree whereExpression() {
		return whereExpression;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitUpdateStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.UPDATE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(updateKeyword, tableReference, asKeyword, alias, setKeyword),
				setColumns.elementsAndSeparators(Functions.<SetColumnTree>identity()),
				Iterators.forArray(whereKeyword, whereExpression, semi));
	}

}
