package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.NullableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class NullableTreeImpl extends EsqlTree implements NullableTree {

	private InternalSyntaxToken nullableKeyword;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken nullKeyword;
	
	
	public NullableTreeImpl(InternalSyntaxToken nullableKeyword) {
		this.nullableKeyword = nullableKeyword;
	}
	
	

	public NullableTreeImpl(InternalSyntaxToken notKeyword, InternalSyntaxToken nullKeyword) {
		super();
		this.notKeyword = notKeyword;
		this.nullKeyword = nullKeyword;
	}



	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitNullable(this);

	}

	@Override
	public InternalSyntaxToken nullableKeyword() {
		return nullableKeyword;
	}

	@Override
	public InternalSyntaxToken notKeyword() {
		return notKeyword;
	}

	@Override
	public InternalSyntaxToken nullKeyword() {
		return nullKeyword;
	}

	@Override
	public Kind getKind() {
		return Kind.NULLABLE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(nullableKeyword, notKeyword, nullKeyword);
	}

}
