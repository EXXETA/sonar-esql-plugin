package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.OverlayFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class OverlayFunctionTreeImpl extends EsqlTree implements OverlayFunctionTree {
	private InternalSyntaxToken overlayKeyword;
	private InternalSyntaxToken openingParenthesis;
	private	ExpressionTree sourceString;
	private InternalSyntaxToken placingKeyword;
	private ExpressionTree sourceString2;
	private InternalSyntaxToken fromKeyword;
	private ExpressionTree startPosition;
	private InternalSyntaxToken forKeyword;
	private ExpressionTree stringLength;
	private InternalSyntaxToken closingParenthesis;
	public OverlayFunctionTreeImpl(InternalSyntaxToken overlayKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceString, InternalSyntaxToken placingKeyword, ExpressionTree sourceString2,
			InternalSyntaxToken fromKeyword, ExpressionTree startPosition, InternalSyntaxToken forKeyword,
			ExpressionTree stringLength, InternalSyntaxToken closingParenthesis) {
		super();
		this.overlayKeyword = overlayKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceString = sourceString;
		this.placingKeyword = placingKeyword;
		this.sourceString2 = sourceString2;
		this.fromKeyword = fromKeyword;
		this.startPosition = startPosition;
		this.forKeyword = forKeyword;
		this.stringLength = stringLength;
		this.closingParenthesis = closingParenthesis;
	}
	public OverlayFunctionTreeImpl(InternalSyntaxToken overlayKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceString, InternalSyntaxToken placingKeyword, ExpressionTree sourceString2,
			InternalSyntaxToken fromKeyword, ExpressionTree startPosition, InternalSyntaxToken closingParenthesis) {
		super();
		this.overlayKeyword = overlayKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceString = sourceString;
		this.placingKeyword = placingKeyword;
		this.sourceString2 = sourceString2;
		this.fromKeyword = fromKeyword;
		this.startPosition = startPosition;
		this.forKeyword = null;
		this.stringLength = null;
		this.closingParenthesis = closingParenthesis;
	}
	@Override
	public InternalSyntaxToken overlayKeyword() {
		return overlayKeyword;
	}
	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	@Override
	public ExpressionTree sourceString() {
		return sourceString;
	}
	@Override
	public InternalSyntaxToken placingKeyword() {
		return placingKeyword;
	}
	@Override
	public ExpressionTree sourceString2() {
		return sourceString2;
	}
	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}
	@Override
	public ExpressionTree startPosition() {
		return startPosition;
	}
	@Override
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}
	@Override
	public ExpressionTree stringLength() {
		return stringLength;
	}
	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitOverlayFunction(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.OVERLAY_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(overlayKeyword,  openingParenthesis,
					 sourceString,  placingKeyword,  sourceString2,
					 fromKeyword,  startPosition,
					 forKeyword, stringLength, closingParenthesis);
	}
	
	
}
