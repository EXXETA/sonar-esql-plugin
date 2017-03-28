package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class DeleteStatementTreeImpl extends EsqlTree implements DeleteStatementTree{

	private InternalSyntaxToken deleteKeyword;
	private InternalSyntaxToken qualifier;
	private InternalSyntaxToken ofKeyword;
	private FieldReferenceTree fieldReference;
	private InternalSyntaxToken semi;
	public DeleteStatementTreeImpl(InternalSyntaxToken deleteKeyword, InternalSyntaxToken qualifier,
			InternalSyntaxToken ofKeyword, FieldReferenceTree fieldReference, InternalSyntaxToken semi) {
		super();
		this.deleteKeyword = deleteKeyword;
		this.qualifier = qualifier;
		this.ofKeyword = ofKeyword;
		this.fieldReference = fieldReference;
		this.semi=semi;
	}
	public InternalSyntaxToken deleteKeyword() {
		return deleteKeyword;
	}
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}
	public InternalSyntaxToken ofKeyword() {
		return ofKeyword;
	}
	public FieldReferenceTree fieldReference() {
		return fieldReference;
	}
	public InternalSyntaxToken semi(){
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDeleteStatement(this);
	}
	@Override
	public Kind getKind() {
		return Kind.DELETE_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(deleteKeyword, qualifier, ofKeyword, fieldReference, semi);
	}
	
	
}
