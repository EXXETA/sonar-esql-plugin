/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhereClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhereClauseTreeImpl extends EsqlTree implements WhereClauseTree {

	private InternalSyntaxToken whereKeyword;
	private ExpressionTree expression;
	public WhereClauseTreeImpl(InternalSyntaxToken whereKeyword, ExpressionTree expression) {
		super();
		this.whereKeyword = whereKeyword;
		this.expression = expression;
	}
	@Override
	public InternalSyntaxToken whereKeyword() {
		return whereKeyword;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhereClause(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.WHERE_CLAUSE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(whereKeyword, expression);
	}
	
	
	
}
