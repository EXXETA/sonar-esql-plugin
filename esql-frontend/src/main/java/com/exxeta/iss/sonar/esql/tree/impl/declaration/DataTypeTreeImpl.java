package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.DataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class DataTypeTreeImpl extends EsqlTree implements DataTypeTree{

	private IntervalDataTypeTreeImpl intervalDataType;
	private DecimalDataTypeTreeImpl decimalDataType;
	private InternalSyntaxToken dataType;
	private InternalSyntaxToken toKeyword;

	public DataTypeTreeImpl(IntervalDataTypeTreeImpl firstOf) {
		this.intervalDataType = (IntervalDataTypeTreeImpl)firstOf;
	}

	public DataTypeTreeImpl(DecimalDataTypeTreeImpl firstOf) {
		this.decimalDataType = (DecimalDataTypeTreeImpl)firstOf;
	}

	public DataTypeTreeImpl(InternalSyntaxToken referenceKeyword, InternalSyntaxToken toKeyword) {
		this.dataType = referenceKeyword;
		this.toKeyword = toKeyword;
	}

	public DataTypeTreeImpl(InternalSyntaxToken dataType) {
		this.dataType = dataType;
	}

	public IntervalDataTypeTreeImpl intervalDataType() {
		return intervalDataType;
	}

	public DecimalDataTypeTreeImpl decimalDataType() {
		return decimalDataType;
	}

	public InternalSyntaxToken dataType() {
		return dataType;
	}

	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDataType(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.Data_TYPE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(intervalDataType, decimalDataType, dataType, toKeyword);
	}
	
	

}
