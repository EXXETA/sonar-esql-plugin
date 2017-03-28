package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.FromClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class FromClauseTreeImpl extends EsqlTree implements FromClauseTree{
	private InternalSyntaxToken fromKeyword;
	private FieldReferenceTreeImpl fieldReference;
	public FromClauseTreeImpl(InternalSyntaxToken fromKeyword, FieldReferenceTreeImpl fieldReference) {
		super();
		this.fromKeyword = fromKeyword;
		this.fieldReference = fieldReference;
	}
	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}
	@Override
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitFromClause(this);
	}
	@Override
	public Kind getKind() {
		return Kind.FROM_CLAUSE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(fromKeyword, fieldReference);
	}
	
	
}
