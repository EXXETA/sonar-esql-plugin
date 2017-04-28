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

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class ReturnStatementTreeImpl extends EsqlTree implements ReturnStatementTree{
	private InternalSyntaxToken returnKeyword;
	private ExpressionTree expression;
	private InternalSyntaxToken semi;
	public ReturnStatementTreeImpl(InternalSyntaxToken returnKeyword, ExpressionTree expression,
			InternalSyntaxToken semi) {
		super();
		this.returnKeyword = returnKeyword;
		this.expression = expression;
		this.semi = semi;
	}
	@Override
	public InternalSyntaxToken returnKeyword() {
		return returnKeyword;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitReturnStatement(this);
	}
	@Override
	public Kind getKind() {
		return Kind.RETURN_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(returnKeyword, expression, semi);
	}
	
}
