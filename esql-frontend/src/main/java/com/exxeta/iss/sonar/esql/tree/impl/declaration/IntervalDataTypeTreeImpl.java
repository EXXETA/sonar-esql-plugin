package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.IntervalDataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IntervalDataTypeTreeImpl extends EsqlTree implements IntervalDataTypeTree {

	private InternalSyntaxToken intervalKeyword;
	private IntervalQualifierTreeImpl qualifier;
	public IntervalDataTypeTreeImpl(InternalSyntaxToken intervalKeyword,
			IntervalQualifierTreeImpl intervalQualifierTreeImpl) {
		super();
		this.intervalKeyword = intervalKeyword;
		this.qualifier = intervalQualifierTreeImpl;
	}
	public IntervalDataTypeTreeImpl(InternalSyntaxToken intervalKeyword) {
		super();
		this.intervalKeyword = intervalKeyword;
		this.qualifier=null;
	}
	public InternalSyntaxToken intervalKeyword() {
		return intervalKeyword;
	}
	public IntervalQualifierTreeImpl qualifier() {
		return qualifier;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIntervalDataType(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.INTERVAL_DATA_TYPE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(intervalKeyword, qualifier);
	}
	
	
	
	
	

}
