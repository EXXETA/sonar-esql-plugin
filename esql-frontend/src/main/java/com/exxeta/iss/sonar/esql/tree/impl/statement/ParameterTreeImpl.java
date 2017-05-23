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
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ParameterTreeImpl extends EsqlTree implements ParameterTree {

	private InternalSyntaxToken directionIndicator;
	private ExpressionTree expression;
	private InternalSyntaxToken constantKeyword;
	private InternalSyntaxToken nameOrNamesapceKeyword;
	private DataTypeTreeImpl dataType;

	public ParameterTreeImpl(InternalSyntaxToken directionIndicator, ExpressionTree expression) {
		this.directionIndicator = directionIndicator;
		this.expression = expression;

	}

	public ParameterTreeImpl(InternalSyntaxToken directionIndicator, ExpressionTree expression, InternalSyntaxToken nameOrNamesapceKeyword) {
		this.directionIndicator = directionIndicator;
		this.expression=expression;
		this.nameOrNamesapceKeyword = nameOrNamesapceKeyword;
				
	}

	public ParameterTreeImpl(InternalSyntaxToken directionIndicator, ExpressionTree expression,
			InternalSyntaxToken constantKeyword, DataTypeTreeImpl dataType) {
		this.directionIndicator = directionIndicator;
		this.expression = expression;
		this.nameOrNamesapceKeyword = null;
		this.constantKeyword = constantKeyword;
		this.dataType = dataType;
	}

	
	
	@Override
	public InternalSyntaxToken directionIndicator() {
		return directionIndicator;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public InternalSyntaxToken constantKeyword() {
		return constantKeyword;
	}

	@Override
	public InternalSyntaxToken nameOrNamesapceKeyword() {
		return nameOrNamesapceKeyword;
	}

	@Override
	public DataTypeTreeImpl dataType() {
		return dataType;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitParameter(this);

	}

	@Override
	public Kind getKind() {
		return Kind.PARAMETER;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(directionIndicator, expression, constantKeyword, nameOrNamesapceKeyword, dataType);
	}

}
