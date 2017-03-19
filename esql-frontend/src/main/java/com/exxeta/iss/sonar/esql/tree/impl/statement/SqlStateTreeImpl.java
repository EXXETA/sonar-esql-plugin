package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class SqlStateTreeImpl extends EsqlTree implements SqlStateTree {

	private InternalSyntaxToken sqlstateKeyword;
	private InternalSyntaxToken likeKeyword;
	private LiteralTreeImpl likeText;
	private InternalSyntaxToken escapeKeyword;
	private LiteralTreeImpl escapeText;
	private InternalSyntaxToken valueKeyword;
	private LiteralTreeImpl valueText;

	public SqlStateTreeImpl(InternalSyntaxToken likeKeyword, LiteralTreeImpl likeText,
			InternalSyntaxToken escapeKeyword, LiteralTreeImpl escapeText) {
		super();
		this.likeKeyword = likeKeyword;
		this.likeText = likeText;
		this.escapeKeyword = escapeKeyword;
		this.escapeText = escapeText;
	}

	public SqlStateTreeImpl(InternalSyntaxToken valueKeyword, LiteralTreeImpl valueText) {
		super();
		this.valueKeyword = valueKeyword;
		this.valueText = valueText;
	}

	public SqlStateTreeImpl finish(InternalSyntaxToken sqlstateKeyword) {
		this.sqlstateKeyword = sqlstateKeyword;
		return this;
	}

	@Override
	public InternalSyntaxToken sqlstateKeyword() {
		return sqlstateKeyword;
	}

	@Override
	public InternalSyntaxToken likeKeyword() {
		return likeKeyword;
	}

	@Override
	public LiteralTreeImpl likeText() {
		return likeText;
	}

	@Override
	public InternalSyntaxToken escapeKeyword() {
		return escapeKeyword;
	}

	@Override
	public LiteralTreeImpl escapeText() {
		return escapeText;
	}

	@Override
	public InternalSyntaxToken valueKeyword() {
		return valueKeyword;
	}

	@Override
	public LiteralTreeImpl valueText() {
		return valueText;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSqlState(this);
	}

	@Override
	public Kind getKind() {
		return Kind.SQL_STATE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(sqlstateKeyword, likeKeyword, likeText, escapeKeyword, escapeText, valueKeyword,
				valueText);
	}

}
