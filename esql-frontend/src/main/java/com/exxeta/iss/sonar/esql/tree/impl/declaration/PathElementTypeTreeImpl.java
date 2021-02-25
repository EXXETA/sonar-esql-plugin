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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.PathElementTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PathElementTypeTreeImpl extends EsqlTree implements PathElementTypeTree {

	private InternalSyntaxToken typeOpenParen;
	private ExpressionTree typeExpression;
	private InternalSyntaxToken typeCloseParen;

	public PathElementTypeTreeImpl(InternalSyntaxToken openParen, ExpressionTree typeExpression,
			InternalSyntaxToken closeParen) {
		this.typeOpenParen = openParen;
		this.typeExpression = typeExpression;
		this.typeCloseParen = closeParen;

	}

	@Override
	public InternalSyntaxToken typeOpenParen() {
		return typeOpenParen;
	}

	@Override
	public ExpressionTree typeExpression() {
		return typeExpression;
	}

	@Override
	public InternalSyntaxToken typeCloseParen() {
		return typeCloseParen;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathElementType(this);
	}

	@Override
	public Kind getKind() {
		return Kind.PATH_ELEMENT_TYPE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(typeOpenParen,typeExpression,	typeCloseParen);
	}


}
