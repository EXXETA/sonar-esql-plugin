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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.VariableReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class SetStatementTreeImpl extends EsqlTree implements SetStatementTree{

	private InternalSyntaxToken setKeyword;
	private VariableReferenceTree variableReference;
	private InternalSyntaxToken type;
	private InternalSyntaxToken equalSign;
	private ExpressionTree expression;
	private final InternalSyntaxToken semiToken;

	public SetStatementTreeImpl(InternalSyntaxToken setKeyword, VariableReferenceTree variableReference,
			InternalSyntaxToken type, InternalSyntaxToken equalSign, ExpressionTree expression, InternalSyntaxToken semiToken) {
		super();
		this.setKeyword = setKeyword;
		this.variableReference = variableReference;
		this.type = type;
		this.equalSign = equalSign;
		this.expression = expression;
		this.semiToken=semiToken;
	}
	@Override
	public InternalSyntaxToken setKeyword() {
		return setKeyword;
	}
	@Override
	public VariableReferenceTree variableReference() {
		return variableReference;
	}
	@Override
	public InternalSyntaxToken type() {
		return type;
	}
	@Override
	public InternalSyntaxToken equalSign() {
		return equalSign;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken semiToken(){
		return semiToken;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSetStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.SET_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(setKeyword, variableReference, type, equalSign, expression, semiToken);
	}
	

}
