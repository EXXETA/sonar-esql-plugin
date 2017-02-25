package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhenClauseTreeImpl extends EsqlTree implements WhenClauseTree{

	private final InternalSyntaxToken whenKeyword; 
	private final ExpressionTree expression;
	private final InternalSyntaxToken thenKeyword; 
	private final List<StatementTree> statements;
	public WhenClauseTreeImpl(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, List<StatementTree> statements) {
		super();
		this.whenKeyword = whenKeyword;
		this.expression = expression;
		this.thenKeyword = thenKeyword;
		this.statements = statements;
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
	public List<StatementTree> statements() {
		return statements;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhenClause(this);
	}
	@Override
	public Kind getKind() {
		return Kind.WHEN_CLAUSE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(whenKeyword, expression, thenKeyword), statements.iterator());
	}

	
}
