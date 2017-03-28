package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class DeleteFromStatementTreeImpl extends EsqlTree implements DeleteFromStatementTree {
	private InternalSyntaxToken deleteKeyword;
	private InternalSyntaxToken fromKeyword;
	private FieldReferenceTreeImpl tableReference;
	private InternalSyntaxToken asKeyword;
	private InternalSyntaxToken asCorrelationName;
	private InternalSyntaxToken whereKeyword;
	private ExpressionTree whereExpression;
	private InternalSyntaxToken semi;

	public DeleteFromStatementTreeImpl(InternalSyntaxToken deleteKeyword, InternalSyntaxToken fromKeyword,
			FieldReferenceTreeImpl tableReference, InternalSyntaxToken asKeyword, InternalSyntaxToken asCorrelationName,
			InternalSyntaxToken whereKeyword, ExpressionTree whereExpression, InternalSyntaxToken semi) {
		super();
		this.deleteKeyword = deleteKeyword;
		this.fromKeyword = fromKeyword;
		this.tableReference = tableReference;
		this.asKeyword = asKeyword;
		this.asCorrelationName = asCorrelationName;
		this.whereKeyword = whereKeyword;
		this.whereExpression = whereExpression;
		this.semi=semi;
	}

	@Override
	public InternalSyntaxToken deleteKeyword() {
		return deleteKeyword;
	}

	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}

	@Override
	public FieldReferenceTreeImpl tableReference() {
		return tableReference;
	}

	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	@Override
	public InternalSyntaxToken asCorrelationName() {
		return asCorrelationName;
	}

	@Override
	public InternalSyntaxToken whereKeyword() {
		return whereKeyword;
	}

	@Override
	public ExpressionTree whereExpression() {
		return whereExpression;
	}
	
	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDeleteFromStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.DELETE_FROM_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(deleteKeyword, fromKeyword, tableReference, asKeyword, asCorrelationName,
				whereKeyword, whereExpression, semi);
	}

}
