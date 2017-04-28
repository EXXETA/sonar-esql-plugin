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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class PathElementTreeImpl extends EsqlTree implements PathElementTree {

	private InternalSyntaxToken typeOpenParen;
	private SeparatedList<InternalSyntaxToken> typeExpressionList;
	private InternalSyntaxToken typeCloseParen;
	private NamespaceTree namespace;
	private InternalSyntaxToken namespaceCurlyOpen;
	private ExpressionTree namespaceExpression;
	private InternalSyntaxToken namespaceCurlyClose;
	private InternalSyntaxToken namespaceStar;
	private InternalSyntaxToken colon;
	private InternalSyntaxToken nameCurlyOpen;
	private ExpressionTree nameExpression;
	private InternalSyntaxToken nameCurlyClose;
	private InternalSyntaxToken name;
	private IndexTreeImpl index;

	public void setType(InternalSyntaxToken openParen, SeparatedList<InternalSyntaxToken> expressionList,
			InternalSyntaxToken closeParen) {
		this.typeOpenParen = openParen;
		this.typeExpressionList = expressionList;
		this.typeCloseParen = closeParen;

	}

	public void name(InternalSyntaxToken nameCurlyOpen, ExpressionTree nameExpression,
			InternalSyntaxToken nameCurlyClose) {
		this.nameCurlyOpen = nameCurlyOpen;
		this.nameExpression = nameExpression;
		this.nameCurlyClose = nameCurlyClose;

	}

	public void name(InternalSyntaxToken name) {
		this.name = name;

	}

	public void index(IndexTreeImpl index) {
		this.index = index;

	}

	public void namespace(InternalSyntaxToken namespaceCurlyOpen, ExpressionTree namespaceExpression,
			InternalSyntaxToken namespaceCurlyClose, InternalSyntaxToken colon) {
		this.namespaceCurlyOpen = namespaceCurlyOpen;
		this.namespaceExpression = namespaceExpression;
		this.namespaceCurlyClose = namespaceCurlyClose;
		this.colon = colon;
	}

	public void namespace(NamespaceTree namespace, InternalSyntaxToken colon) {
		this.namespace = namespace;
		this.colon = colon;

	}

	public void namespace(InternalSyntaxToken colon) {
		this.colon = colon;

	}

	public void namesapce(InternalSyntaxToken namespaceStar, InternalSyntaxToken second) {
		this.namespaceStar = namespaceStar;

	}

	@Override
	public InternalSyntaxToken typeOpenParen() {
		return typeOpenParen;
	}

	@Override
	public SeparatedList<InternalSyntaxToken> typeExpressionList() {
		return typeExpressionList;
	}

	@Override
	public InternalSyntaxToken typeCloseParen() {
		return typeCloseParen;
	}

	@Override
	public NamespaceTree namespace() {
		return namespace;
	}

	@Override
	public InternalSyntaxToken namespaceCurlyOpen() {
		return namespaceCurlyOpen;
	}

	@Override
	public ExpressionTree namespaceExpression() {
		return namespaceExpression;
	}

	@Override
	public InternalSyntaxToken namespaceCurlyClose() {
		return namespaceCurlyClose;
	}

	@Override
	public InternalSyntaxToken namespaceStar() {
		return namespaceStar;
	}

	@Override
	public InternalSyntaxToken colon() {
		return colon;
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
	public InternalSyntaxToken name() {
		return name;
	}

	@Override
	public IndexTreeImpl index() {
		return index;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathElement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.PATH_ELEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.singletonIterator(typeOpenParen),
				typeExpressionList==null?Iterators.emptyIterator():
				typeExpressionList.elementsAndSeparators(Functions.<InternalSyntaxToken>identity()),
				Iterators.forArray(typeCloseParen, namespace, namespaceCurlyOpen, namespaceExpression,
						namespaceCurlyClose, namespaceStar, colon, nameCurlyOpen, nameExpression, nameCurlyClose, name,
						index));
	}

}
