package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.DecimalDataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class DecimalDataTypeTreeImpl extends EsqlTree implements DecimalDataTypeTree{

	InternalSyntaxToken decimalKeyword;
	InternalSyntaxToken openParen;
	InternalSyntaxToken precision; 
	InternalSyntaxToken comma;
	InternalSyntaxToken scale; 
	InternalSyntaxToken closeParen;
	public DecimalDataTypeTreeImpl(InternalSyntaxToken openParen, InternalSyntaxToken precision,
			InternalSyntaxToken comma, InternalSyntaxToken scale, InternalSyntaxToken closeParen) {
		super();
		this.openParen = openParen;
		this.precision = precision;
		this.comma = comma;
		this.scale = scale;
		this.closeParen = closeParen;
	}
	
	
	public DecimalDataTypeTreeImpl(InternalSyntaxToken decimalKeyword) {
		super();
		this.decimalKeyword = decimalKeyword;
	}


	public DecimalDataTypeTreeImpl complete(InternalSyntaxToken decimalKeyword){
		this.decimalKeyword=decimalKeyword;
		return this;
	}


	public InternalSyntaxToken decimalKeyword() {
		return decimalKeyword;
	}


	public InternalSyntaxToken openParen() {
		return openParen;
	}


	public InternalSyntaxToken precision() {
		return precision;
	}


	public InternalSyntaxToken comma() {
		return comma;
	}


	public InternalSyntaxToken scale() {
		return scale;
	}


	public InternalSyntaxToken closeParen() {
		return closeParen;
	}


	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDecimalDataType(this);
		
	}


	@Override
	public Kind getKind() {
		return Kind.DECIMAL_DATA_TYPE;
	}


	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(decimalKeyword, openParen, precision, comma, scale, closeParen);
	}
	
	
	
}
