package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TrimFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class TrimFunctionTreeImpl extends EsqlTree implements TrimFunctionTree {
	private InternalSyntaxToken trimKeyword;
	private InternalSyntaxToken openingParenthesis;
	private InternalSyntaxToken qualifier;
	private ExpressionTree trimSingleton;
	private InternalSyntaxToken fromKeyword;
	private ExpressionTree sourceString;
	private InternalSyntaxToken closingParenthesis;

	public TrimFunctionTreeImpl(InternalSyntaxToken trimKeyword, InternalSyntaxToken openingParenthesis,
			InternalSyntaxToken qualifier, ExpressionTree trimSingleton, InternalSyntaxToken fromKeyword,
			ExpressionTree sourceString, InternalSyntaxToken closingParenthesis) {
		super();
		this.trimKeyword = trimKeyword;
		this.openingParenthesis = openingParenthesis;
		this.qualifier = qualifier;
		this.trimSingleton = trimSingleton;
		this.fromKeyword = fromKeyword;
		this.sourceString = sourceString;
		this.closingParenthesis = closingParenthesis;
	}

	public TrimFunctionTreeImpl(InternalSyntaxToken trimKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceString, InternalSyntaxToken closingParenthesis) {
		super();
		this.trimKeyword = trimKeyword;
		this.openingParenthesis = openingParenthesis;
		this.qualifier = null;
		this.trimSingleton = null;
		this.fromKeyword = null;
		this.sourceString = sourceString;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public InternalSyntaxToken trimKeyword() {
		return trimKeyword;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}

	@Override
	public ExpressionTree trimSingleton() {
		return trimSingleton;
	}

	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}

	@Override
	public ExpressionTree sourceString() {
		return sourceString;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitTrimFunction(this);
	}

	@Override
	public Kind getKind() {
		return Kind.TRIM_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(trimKeyword, openingParenthesis, qualifier, trimSingleton, fromKeyword, sourceString, closingParenthesis);
	}
	
	

}
