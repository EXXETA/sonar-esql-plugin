package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DetachStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class DetachStatementTreeImpl extends EsqlTree implements DetachStatementTree{
	private InternalSyntaxToken detachKeyword;
	private FieldReferenceTreeImpl fieldReference;
	private InternalSyntaxToken semi;
	public DetachStatementTreeImpl(InternalSyntaxToken detachKeyword, FieldReferenceTreeImpl fieldReference,
			InternalSyntaxToken semi) {
		super();
		this.detachKeyword = detachKeyword;
		this.fieldReference = fieldReference;
		this.semi = semi;
	}
	@Override
	public InternalSyntaxToken detachKeyword() {
		return detachKeyword;
	}
	@Override
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
	}
	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDetachStatement(this);
	}
	@Override
	public Kind getKind() {
		return Kind.DETACH_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(detachKeyword, fieldReference, semi);
	}
	
}
