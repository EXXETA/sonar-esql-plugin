package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class SetColumnTreeImpl extends EsqlTree implements SetColumnTree {
	private SyntaxToken columnName;

	private SyntaxToken equal;

	private ExpressionTree expression;

	public SetColumnTreeImpl(SyntaxToken columnName, SyntaxToken equal, ExpressionTree expression) {
		super();
		this.columnName = columnName;
		this.equal = equal;
		this.expression = expression;
	}

	public SyntaxToken columnName() {
		return columnName;
	}

	public SyntaxToken equal() {
		return equal;
	}

	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSetColumn(this);
	}

	@Override
	public Kind getKind() {
		return Kind.SET_COLUMN;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(columnName, equal, expression);
	}
	
	
}
