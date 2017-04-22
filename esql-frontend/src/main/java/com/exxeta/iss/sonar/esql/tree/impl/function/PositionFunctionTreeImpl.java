package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.OverlayFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.PositionFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class PositionFunctionTreeImpl extends EsqlTree implements PositionFunctionTree {
	private InternalSyntaxToken positionKeyword;
	private InternalSyntaxToken openingParenthesis;
	private	ExpressionTree searchExpression;
	private InternalSyntaxToken inKeyword;
	private ExpressionTree sourceExpression;
	private InternalSyntaxToken fromKeyword;
	private ExpressionTree fromExpression;
	private InternalSyntaxToken repeatKeyword;
	private ExpressionTree repeatExpression;
	private InternalSyntaxToken closingParenthesis;
	public PositionFunctionTreeImpl(InternalSyntaxToken positionKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree searchExpression, InternalSyntaxToken inKeyword, ExpressionTree sourceExpression,
			InternalSyntaxToken fromKeyword, ExpressionTree fromExpression, InternalSyntaxToken repeatKeyword,
			ExpressionTree repeatExpression, InternalSyntaxToken closingParenthesis) {
		super();
		this.positionKeyword = positionKeyword;
		this.openingParenthesis = openingParenthesis;
		this.searchExpression = searchExpression;
		this.inKeyword = inKeyword;
		this.sourceExpression = sourceExpression;
		this.fromKeyword = fromKeyword;
		this.fromExpression = fromExpression;
		this.repeatKeyword = repeatKeyword;
		this.repeatExpression = repeatExpression;
		this.closingParenthesis = closingParenthesis;
	}
	
	@Override
	public InternalSyntaxToken positionKeyword() {
		return positionKeyword;
	}
	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	@Override
	public ExpressionTree searchExpression() {
		return searchExpression;
	}
	@Override
	public InternalSyntaxToken inKeyword() {
		return inKeyword;
	}
	@Override
	public ExpressionTree sourceExpression() {
		return sourceExpression;
	}
	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}
	@Override
	public ExpressionTree fromExpression() {
		return fromExpression;
	}
	@Override
	public InternalSyntaxToken repeatKeyword() {
		return repeatKeyword;
	}
	@Override
	public ExpressionTree repeatExpression() {
		return repeatExpression;
	}
	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPositionFunction(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.POSITION_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(positionKeyword,  openingParenthesis,
					 searchExpression,  inKeyword,  sourceExpression,
					 fromKeyword,  fromExpression,
					 repeatKeyword, repeatExpression, closingParenthesis);
	}
	
	
}
