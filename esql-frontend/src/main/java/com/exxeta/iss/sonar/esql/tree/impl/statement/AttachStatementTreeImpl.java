package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.AttachStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class AttachStatementTreeImpl extends EsqlTree implements AttachStatementTree {

	private SyntaxToken attachKeyword;

	private FieldReferenceTree dynamicReference;

	private SyntaxToken toKeyword;

	private FieldReferenceTree fieldReference;

	private SyntaxToken asKeyword;

	private SyntaxToken location;

	private SyntaxToken semi;

	public AttachStatementTreeImpl(SyntaxToken attachKeyword, FieldReferenceTree dynamicReference,
			SyntaxToken toKeyword, FieldReferenceTree fieldReference, SyntaxToken asKeyword, SyntaxToken location,
			SyntaxToken semi) {
		super();
		this.attachKeyword = attachKeyword;
		this.dynamicReference = dynamicReference;
		this.toKeyword = toKeyword;
		this.fieldReference = fieldReference;
		this.asKeyword = asKeyword;
		this.location = location;
		this.semi = semi;
	}

	public SyntaxToken attachKeyword() {
		return attachKeyword;
	}

	public FieldReferenceTree dynamicReference() {
		return dynamicReference;
	}

	public SyntaxToken toKeyword() {
		return toKeyword;
	}

	public FieldReferenceTree fieldReference() {
		return fieldReference;
	}

	public SyntaxToken asKeyword() {
		return asKeyword;
	}

	public SyntaxToken location() {
		return location;
	}

	public SyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAttachStatement(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.ATTACH_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(attachKeyword, dynamicReference, toKeyword, fieldReference, asKeyword, location, semi);
	}

	
	
}
