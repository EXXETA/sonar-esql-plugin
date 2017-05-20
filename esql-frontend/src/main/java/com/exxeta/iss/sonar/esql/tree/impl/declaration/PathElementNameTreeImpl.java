package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.PathElementNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PathElementNameTreeImpl extends EsqlTree implements PathElementNameTree{
	private InternalSyntaxToken nameCurlyOpen;
	private ExpressionTree nameExpression;
	private InternalSyntaxToken nameCurlyClose;
	private InternalSyntaxToken name;

	public PathElementNameTreeImpl(InternalSyntaxToken nameCurlyOpen, ExpressionTree nameExpression,
			InternalSyntaxToken nameCurlyClose) {
		this.nameCurlyOpen = nameCurlyOpen;
		this.nameExpression = nameExpression;
		this.nameCurlyClose = nameCurlyClose;

	}

	public PathElementNameTreeImpl(InternalSyntaxToken name) {
		this.name = name;

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
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathElementName(this);
	}

	@Override
	public Kind getKind() {
		return Kind.PATH_ELEMENT_NAME;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(nameCurlyOpen, nameExpression, nameCurlyClose, name);
	}

}
