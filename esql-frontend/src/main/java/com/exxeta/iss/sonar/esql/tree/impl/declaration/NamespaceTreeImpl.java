package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class NamespaceTreeImpl extends EsqlTree implements NamespaceTree{

	private InternalSyntaxToken identifier;

	public NamespaceTreeImpl(InternalSyntaxToken identifier) {
		this.identifier=identifier;
	}
	
	@Override
	public SyntaxToken identifier() {
		return identifier;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitNamespace(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.NAMESPACE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.singletonIterator(identifier);
	}
	
	

}
