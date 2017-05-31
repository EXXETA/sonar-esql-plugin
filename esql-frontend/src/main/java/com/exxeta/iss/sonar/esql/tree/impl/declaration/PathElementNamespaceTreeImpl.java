package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementNamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class PathElementNamespaceTreeImpl extends EsqlTree implements PathElementNamespaceTree{

	private NamespaceTree namespace;
	private InternalSyntaxToken namespaceCurlyOpen;
	private ExpressionTree namespaceExpression;
	private InternalSyntaxToken namespaceCurlyClose;
	private InternalSyntaxToken namespaceStar;
	private InternalSyntaxToken colon;
	
	public PathElementNamespaceTreeImpl(InternalSyntaxToken namespaceCurlyOpen, ExpressionTree namespaceExpression,
			InternalSyntaxToken namespaceCurlyClose, InternalSyntaxToken colon) {
		this.namespaceCurlyOpen = namespaceCurlyOpen;
		this.namespaceExpression = namespaceExpression;
		this.namespaceCurlyClose = namespaceCurlyClose;
		this.colon = colon;
	}

	public PathElementNamespaceTreeImpl(NamespaceTree namespace, InternalSyntaxToken colon) {
		this.namespace = namespace;
		this.colon = colon;

	}

	public PathElementNamespaceTreeImpl(InternalSyntaxToken colon) {
		this.colon = colon;

	}

	public PathElementNamespaceTreeImpl(InternalSyntaxToken namespaceStar, InternalSyntaxToken colon) {
		this.namespaceStar = namespaceStar;
		this.colon=colon;
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
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathElementNamespace(this);
		
	}

	@Override
	public Kind getKind() {
		return Tree.Kind.PATH_ELEMENT_NAMESPACE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(namespace, namespaceCurlyOpen, namespaceExpression,
						namespaceCurlyClose, namespaceStar, colon);
	}

}
