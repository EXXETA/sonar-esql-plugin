package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhenClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhenClauseExpressionTreeImpl extends EsqlTree implements WhenClauseExpressionTree{
	private final InternalSyntaxToken whenKeyword; 
	private final ExpressionTree expression;
	private final InternalSyntaxToken thenKeyword; 
	private final ExpressionTree resultValue;
	public WhenClauseExpressionTreeImpl(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, ExpressionTree resultValue) {
		super();
		this.whenKeyword = whenKeyword;
		this.expression = expression;
		this.thenKeyword = thenKeyword;
		this.resultValue = resultValue;
	}
	public InternalSyntaxToken whenKeyword() {
		return whenKeyword;
	}
	public ExpressionTree expression() {
		return expression;
	}
	public InternalSyntaxToken thenKeyword() {
		return thenKeyword;
	}
	public ExpressionTree resultValue() {
		return resultValue;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhenClauseExpression(this);
	}
	@Override
	public Kind getKind() {
		return Kind.WHEN_CLAUSE_EXPRESSION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(whenKeyword, expression, thenKeyword, resultValue);
	}

	
}
