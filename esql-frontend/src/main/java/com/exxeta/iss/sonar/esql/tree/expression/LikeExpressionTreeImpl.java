package com.exxeta.iss.sonar.esql.tree.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.LikeExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class LikeExpressionTreeImpl extends EsqlTree implements LikeExpressionTree, TypableTree{
	private ExpressionTree source;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken likeKeyword;
	private ExpressionTree pattern;
	private InternalSyntaxToken escapeKeyword;
	private ExpressionTree escapeChar;
	
	private TypeSet types = TypeSet.emptyTypeSet();

	
	public LikeExpressionTreeImpl(ExpressionTree source, InternalSyntaxToken notKeyword,
			InternalSyntaxToken likeKeyword, ExpressionTree pattern, InternalSyntaxToken escapeKeyword,
			ExpressionTree escapeChar) {
		super();
		this.source = source;
		this.notKeyword = notKeyword;
		this.likeKeyword = likeKeyword;
		this.pattern = pattern;
		this.escapeKeyword = escapeKeyword;
		this.escapeChar = escapeChar;
	}
	@Override
	public ExpressionTree source() {
		return source;
	}
	@Override
	public InternalSyntaxToken notKeyword() {
		return notKeyword;
	}
	@Override
	public InternalSyntaxToken likeKeyword() {
		return likeKeyword;
	}
	@Override
	public ExpressionTree pattern() {
		return pattern;
	}
	@Override
	public InternalSyntaxToken escapeKeyword() {
		return escapeKeyword;
	}
	@Override
	public ExpressionTree escapeChar() {
		return escapeChar;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLikeExpression(this);
	}
	@Override
	public Kind getKind() {
		return Kind.LIKE_EXPRESSION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(source, notKeyword, likeKeyword, pattern, escapeKeyword, escapeChar);
	}
	@Override
	public TypeSet types() {
		return types;
	}
	

	@Override
	public void add(Type type) {
		types.add(type);
	}	
}
