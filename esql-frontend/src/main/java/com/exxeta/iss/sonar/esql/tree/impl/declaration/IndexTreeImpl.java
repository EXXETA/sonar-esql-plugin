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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.IndexTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IndexTreeImpl extends EsqlTree implements IndexTree {

	private InternalSyntaxToken openBracket;
	private InternalSyntaxToken direction;
	private ExpressionTree index;
	private InternalSyntaxToken closeBracket;

	public IndexTreeImpl(InternalSyntaxToken openBracket, InternalSyntaxToken direction, ExpressionTree index,
			InternalSyntaxToken closeBracket) {
		super();
		this.openBracket = openBracket;
		this.direction = direction;
		this.index = index;
		this.closeBracket = closeBracket;
	}

	@Override
	public InternalSyntaxToken openBracket() {
		return openBracket;
	}

	@Override
	public InternalSyntaxToken direction() {
		return direction;
	}

	@Override
	public ExpressionTree index() {
		return index;
	}

	@Override
	public InternalSyntaxToken closeBracket() {
		return closeBracket;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIndex(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.INDEX;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(openBracket, direction, index, closeBracket);
	}
	
	

}
