package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.IntervalQualifierTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IntervalQualifierTreeImpl extends EsqlTree implements IntervalQualifierTree{

	private InternalSyntaxToken from;
	private InternalSyntaxToken toKeyword;
	private InternalSyntaxToken to;
	
	public IntervalQualifierTreeImpl(InternalSyntaxToken from) {
		this.from = from;
	}

	public IntervalQualifierTreeImpl(InternalSyntaxToken from, InternalSyntaxToken toKeyword, InternalSyntaxToken to) {
		this.from = from;
		this.toKeyword = toKeyword;
		this.to = to;
	}

	public InternalSyntaxToken from() {
		return from;
	}

	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	public InternalSyntaxToken to() {
		return to;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIntervalQualifier(this);
	}

	@Override
	public Kind getKind() {
		return Kind.INTERVAL_QUALIFIER;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(from, toKeyword, to);
	}
	
	
	
	

}
