package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ReturnTypeTreeImpl extends EsqlTree implements ReturnTypeTree {

	
	private InternalSyntaxToken returnsKeyword;
	private DataTypeTreeImpl dataType;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken nullKeyword;
	private InternalSyntaxToken nullableKeyword;

	public ReturnTypeTreeImpl(InternalSyntaxToken returnsKeyword, DataTypeTreeImpl dataType) {
		this.returnsKeyword = returnsKeyword;
		this.dataType=dataType;
		this.notKeyword=null;
		this.nullKeyword=null;
		this.nullableKeyword=null;
	}

	public ReturnTypeTreeImpl(InternalSyntaxToken returnsKeyword, DataTypeTreeImpl dataType,
			InternalSyntaxToken notKeyword, InternalSyntaxToken nullKeyword) {
		this.returnsKeyword = returnsKeyword;
		this.dataType=dataType;
		this.notKeyword=notKeyword;
		this.nullKeyword=nullKeyword;
		this.nullableKeyword=null;
		
	}

	public ReturnTypeTreeImpl(InternalSyntaxToken returnsKeyword, DataTypeTreeImpl dataType,
			InternalSyntaxToken nullableKeyword) {
		this.returnsKeyword = returnsKeyword;
		this.dataType=dataType;
		this.notKeyword=null;
		this.nullKeyword=null;
		this.nullableKeyword=nullableKeyword;
		
	}

	@Override
	public SyntaxToken returnsKeyword() {
		return returnsKeyword;
	}

	@Override
	public DataTypeTreeImpl dataType() {
		return dataType;
	}

	@Override
	public SyntaxToken notKeyword() {
		return notKeyword;
	}

	@Override
	public SyntaxToken nullKeyword() {
		return nullKeyword;
	}

	@Override
	public SyntaxToken nullableKeyword() {
		return nullableKeyword;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitReturnType(this);

	}

	@Override
	public Kind getKind() {
		return Kind.RETURN_TYPE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(returnsKeyword, dataType, notKeyword, nullKeyword, nullableKeyword);
	}



}
