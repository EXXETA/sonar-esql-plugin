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
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class SetColumnTreeImpl extends EsqlTree implements SetColumnTree {
	private SyntaxToken columnName;

	private SyntaxToken equalSign;

	private ExpressionTree expression;

	public SetColumnTreeImpl(SyntaxToken columnName, SyntaxToken equalSign, ExpressionTree expression) {
		super();
		this.columnName = columnName;
		this.equalSign = equalSign;
		this.expression = expression;
	}

	@Override
	public SyntaxToken columnName() {
		return columnName;
	}

	@Override
	public SyntaxToken equalSign() {
		return equalSign;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSetColumn(this);
	}

	@Override
	public Kind getKind() {
		return Kind.SET_COLUMN;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(columnName, equalSign, expression);
	}
	
	
}
