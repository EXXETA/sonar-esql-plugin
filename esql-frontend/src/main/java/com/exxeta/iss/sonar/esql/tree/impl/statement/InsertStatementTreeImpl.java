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

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.InsertStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class InsertStatementTreeImpl extends EsqlTree implements InsertStatementTree {
	private InternalSyntaxToken insertKeyword;
	private InternalSyntaxToken intoKeyword;
	private FieldReferenceTreeImpl tableReference;

	private ParameterListTreeImpl columns;

	private InternalSyntaxToken valuesKeyword;

	private SyntaxToken openParenthesis;

	private SeparatedList<ExpressionTree> expressions;

	private InternalSyntaxToken closeParenthesis;

	private InternalSyntaxToken semi;

	public InsertStatementTreeImpl(InternalSyntaxToken insertKeyword, InternalSyntaxToken intoKeyword,
			FieldReferenceTreeImpl tableReference, ParameterListTreeImpl columns, InternalSyntaxToken valuesKeyword,
			SyntaxToken openParenthesis, SeparatedList<ExpressionTree> expressions,
			InternalSyntaxToken closeParenthesis, InternalSyntaxToken semi) {
		super();
		this.insertKeyword = insertKeyword;
		this.intoKeyword = intoKeyword;
		this.tableReference = tableReference;
		this.columns = columns;
		this.valuesKeyword = valuesKeyword;
		this.openParenthesis = openParenthesis;
		this.expressions = expressions;
		this.closeParenthesis = closeParenthesis;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken insertKeyword() {
		return insertKeyword;
	}

	@Override
	public InternalSyntaxToken intoKeyword() {
		return intoKeyword;
	}

	@Override
	public FieldReferenceTreeImpl tableReference() {
		return tableReference;
	}

	@Override
	public ParameterListTreeImpl columns() {
		return columns;
	}

	@Override
	public InternalSyntaxToken valuesKeyword() {
		return valuesKeyword;
	}

	@Override
	public SyntaxToken openParenthesis() {
		return openParenthesis;
	}

	@Override
	public SeparatedList<ExpressionTree> expressions() {
		return expressions;
	}

	@Override
	public InternalSyntaxToken closeParenthesis() {
		return closeParenthesis;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitInsertStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.INSERT_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(insertKeyword, intoKeyword, tableReference, columns, valuesKeyword, openParenthesis), expressions.elementsAndSeparators(Functions.<ExpressionTree>identity()), Iterators.forArray(closeParenthesis, semi));
	}

}
