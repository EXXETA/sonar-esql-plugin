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
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PassthruStatementTreeImpl extends EsqlTree implements PassthruStatementTree{

	private InternalSyntaxToken passthruKeyword;
	private ExpressionTree expression;
	private InternalSyntaxToken toKeyword;
	private FieldReferenceTreeImpl databaseReference;
	private InternalSyntaxToken valuesKeyword;
	private ParameterListTreeImpl expressionList;
	private InternalSyntaxToken semi;
	
	public PassthruStatementTreeImpl(ParameterListTreeImpl expressionList) {
		this.expressionList=expressionList;
	}

	public PassthruStatementTreeImpl(ExpressionTree expression, InternalSyntaxToken toKeyword,
			FieldReferenceTreeImpl databaseReference, InternalSyntaxToken valuesKeyword,
			ParameterListTreeImpl expressionList) {
		super();
		this.expression = expression;
		this.toKeyword = toKeyword;
		this.databaseReference = databaseReference;
		this.valuesKeyword = valuesKeyword;
		this.expressionList = expressionList;
	}

	public PassthruStatementTreeImpl complete(InternalSyntaxToken passthruKeyword, InternalSyntaxToken semi){
		this.passthruKeyword=passthruKeyword;
		this.semi=semi;
		return this;
	}

	@Override
	public InternalSyntaxToken passthruKeyword() {
		return passthruKeyword;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public FieldReferenceTreeImpl databaseReference() {
		return databaseReference;
	}

	@Override
	public InternalSyntaxToken valuesKeyword() {
		return valuesKeyword;
	}

	@Override
	public ParameterListTreeImpl expressionList() {
		return expressionList;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPassthruStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.PASSTHRU_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(passthruKeyword, expression, toKeyword, databaseReference, valuesKeyword, expressionList, semi);
	}
	
	

}
