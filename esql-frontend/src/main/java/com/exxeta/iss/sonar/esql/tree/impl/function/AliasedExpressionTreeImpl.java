package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class AliasedExpressionTreeImpl extends EsqlTree implements AliasedExpressionTree{
	private ExpressionTree expression;
	private InternalSyntaxToken asKeyword;
	private InternalSyntaxToken alias;
	public AliasedExpressionTreeImpl(ExpressionTree expression, InternalSyntaxToken asKeyword,
			InternalSyntaxToken alias) {
		super();
		this.expression = expression;
		this.asKeyword = asKeyword;
		this.alias = alias;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}
	@Override
	public InternalSyntaxToken alias() {
		return alias;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAliasedExpression(this);
	}
	@Override
	public Kind getKind() {
		return Kind.ALIASED_EXPRESSION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(expression, asKeyword, alias);
	}
	
	
	
	
}
