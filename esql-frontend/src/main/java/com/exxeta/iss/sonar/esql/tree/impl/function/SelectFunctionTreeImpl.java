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
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class SelectFunctionTreeImpl extends EsqlTree implements SelectFunctionTree{
	private InternalSyntaxToken selectKeyword;
	private SelectClauseTreeImpl selectClause;
	private FromClauseExpressionTreeImpl fromClause;
	private WhereClauseTreeImpl whereClause;
	public SelectFunctionTreeImpl(InternalSyntaxToken selectKeyword, SelectClauseTreeImpl selectClause,
			FromClauseExpressionTreeImpl fromClause, WhereClauseTreeImpl whereClause) {
		super();
		this.selectKeyword = selectKeyword;
		this.selectClause = selectClause;
		this.fromClause = fromClause;
		this.whereClause = whereClause;
	}
	@Override
	public InternalSyntaxToken selectKeyword() {
		return selectKeyword;
	}
	@Override
	public SelectClauseTreeImpl selectClause() {
		return selectClause;
	}
	@Override
	public FromClauseExpressionTreeImpl fromClause() {
		return fromClause;
	}
	@Override
	public WhereClauseTreeImpl whereClause() {
		return whereClause;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSelectFunction(this);
	}
	@Override
	public Kind getKind() {
		return Kind.SELECT_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(selectKeyword, selectClause, fromClause, whereClause);
	}
	
	
}
