package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class TheFunctionTreeImpl extends EsqlTree implements TheFunctionTree {

	private final InternalSyntaxToken theKeyword;

	private final InternalSyntaxToken openingParenthesis;

	private final ExpressionTree expression;

	private final InternalSyntaxToken closingParenthesis;

	public TheFunctionTreeImpl(InternalSyntaxToken theKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, InternalSyntaxToken closingParenthesis) {
		super();
		this.theKeyword = theKeyword;
		this.openingParenthesis = openingParenthesis;
		this.expression = expression;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public SyntaxToken theKeyword() {
		return theKeyword;
	}

	@Override
	public SyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public SyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public Kind getKind() {
		return Kind.THE_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {

		return Iterators.<Tree> forArray(theKeyword, openingParenthesis, expression, closingParenthesis);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitTheFunction(this);
	}

}
