package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhereClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhereClauseTreeImpl extends EsqlTree implements WhereClauseTree {

	private InternalSyntaxToken whereKeyword;
	private ExpressionTree expression;
	public WhereClauseTreeImpl(InternalSyntaxToken whereKeyword, ExpressionTree expression) {
		super();
		this.whereKeyword = whereKeyword;
		this.expression = expression;
	}
	@Override
	public InternalSyntaxToken whereKeyword() {
		return whereKeyword;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhereClause(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.WHERE_CLAUSE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(whereKeyword, expression);
	}
	
	
	
}
