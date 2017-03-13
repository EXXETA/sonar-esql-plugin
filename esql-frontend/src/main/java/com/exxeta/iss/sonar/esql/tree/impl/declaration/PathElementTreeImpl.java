package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
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

	public InternalSyntaxToken typeOpenParen() {
		return typeOpenParen;
	}

	public SeparatedList<InternalSyntaxToken> typeExpressionList() {
		return typeExpressionList;
	}

	public InternalSyntaxToken typeCloseParen() {
		return typeCloseParen;
	}

	public NamespaceTree namespace() {
		return namespace;
	}

	public InternalSyntaxToken namespaceCurlyOpen() {
		return namespaceCurlyOpen;
	}

	public ExpressionTree namespaceExpression() {
		return namespaceExpression;
	}

	public InternalSyntaxToken namespaceCurlyClose() {
		return namespaceCurlyClose;
	}

	public InternalSyntaxToken namespaceStar() {
		return namespaceStar;
	}

	public InternalSyntaxToken colon() {
		return colon;
	}

	public InternalSyntaxToken nameCurlyOpen() {
		return nameCurlyOpen;
	}

	public ExpressionTree nameExpression() {
		return nameExpression;
	}

	public InternalSyntaxToken nameCurlyClose() {
		return nameCurlyClose;
	}

	public InternalSyntaxToken name() {
		return name;
	}

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
