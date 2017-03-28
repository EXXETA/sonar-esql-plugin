package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class RepeatClauseTreeImpl extends EsqlTree implements RepeatClauseTree {
	private InternalSyntaxToken repeatKeyword;
	private InternalSyntaxToken valueKeyword;
	private ExpressionTree expression;

	public RepeatClauseTreeImpl(InternalSyntaxToken repeatKeyword, InternalSyntaxToken valueKeyword,
			ExpressionTree expression) {
		super();
		this.repeatKeyword = repeatKeyword;
		this.valueKeyword = valueKeyword;
		this.expression = expression;
	}

	public InternalSyntaxToken repeatKeyword() {
		return repeatKeyword;
	}

	public InternalSyntaxToken valueKeyword() {
		return valueKeyword;
	}

	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitRepeatClause(this);
	}

	@Override
	public Kind getKind() {
		return Kind.REPEAT_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(repeatKeyword, valueKeyword, expression);
	}

}
