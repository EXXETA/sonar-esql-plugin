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

import com.exxeta.iss.sonar.esql.api.tree.PathElementNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IdentifierTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PathElementNameTreeImpl extends EsqlTree implements PathElementNameTree{
	private InternalSyntaxToken nameCurlyOpen;
	private ExpressionTree nameExpression;
	private InternalSyntaxToken nameCurlyClose;
	private IdentifierTreeImpl name;
	private InternalSyntaxToken star;

	public PathElementNameTreeImpl(InternalSyntaxToken nameCurlyOpen, ExpressionTree nameExpression,
			InternalSyntaxToken nameCurlyClose) {
		this.nameCurlyOpen = nameCurlyOpen;
		this.nameExpression = nameExpression;
		this.nameCurlyClose = nameCurlyClose;

	}

	public PathElementNameTreeImpl(IdentifierTreeImpl name) {
		this.name = name;

	}
	public PathElementNameTreeImpl(InternalSyntaxToken star) {
		this.star = star;

	}
	@Override
	public InternalSyntaxToken nameCurlyOpen() {
		return nameCurlyOpen;
	}

	@Override
	public ExpressionTree nameExpression() {
		return nameExpression;
	}

	@Override
	public InternalSyntaxToken nameCurlyClose() {
		return nameCurlyClose;
	}

	@Override
	public IdentifierTreeImpl name() {
		return name;
	}

	@Override
	public InternalSyntaxToken star() {
		return star;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathElementName(this);
	}

	@Override
	public Kind getKind() {
		return Kind.PATH_ELEMENT_NAME;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(nameCurlyOpen, nameExpression, nameCurlyClose, name, star);
	}

}
