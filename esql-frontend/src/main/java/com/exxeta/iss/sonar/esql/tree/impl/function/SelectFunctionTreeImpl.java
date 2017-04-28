package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class SelectFunctionTreeImpl extends EsqlTree implements SelectFunctionTree{
	private InternalSyntaxToken selectKeyword;
	private SelectClauseTreeImpl selectClause;
	private FromClauseExpressionTreeImpl fromClause;
	private WhereClauseTreeImpl whereClause;
	public SelectFunctionTreeImpl(InternalSyntaxToken selectKeyword, SelectClauseTreeImpl selectClause,
			FromClauseExpressionTreeImpl fromClause, WhereClauseTreeImpl whereClause) {
		super();
		this.selectKeyword = selectKeyword;
		this.selectClause = selectClause;
		this.fromClause = fromClause;
		this.whereClause = whereClause;
	}
	@Override
	public InternalSyntaxToken selectKeyword() {
		return selectKeyword;
	}
	@Override
	public SelectClauseTreeImpl selectClause() {
		return selectClause;
	}
	@Override
	public FromClauseExpressionTreeImpl fromClause() {
		return fromClause;
	}
	@Override
	public WhereClauseTreeImpl whereClause() {
		return whereClause;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSelectFunction(this);
	}
	@Override
	public Kind getKind() {
		return Kind.SELECT_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(selectKeyword, selectClause, fromClause, whereClause);
	}
	
	
}
