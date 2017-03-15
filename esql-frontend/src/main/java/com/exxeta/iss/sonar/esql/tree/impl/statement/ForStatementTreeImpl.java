package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ForStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class ForStatementTreeImpl extends EsqlTree implements ForStatementTree {
	private final InternalSyntaxToken forKeyword;
	private final InternalSyntaxToken correlationName;
	private final InternalSyntaxToken asKeyword;
	private final FieldReferenceTreeImpl fieldReference;
	private final InternalSyntaxToken doKeyword;
	private final List<StatementTree> statements;
	private final InternalSyntaxToken forKeyword2;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken semi;

	public ForStatementTreeImpl(InternalSyntaxToken forKeyword, InternalSyntaxToken correlationName,
			InternalSyntaxToken asKeyword, FieldReferenceTreeImpl fieldReference, InternalSyntaxToken doKeyword, List<StatementTree> statements,
			InternalSyntaxToken forKeyword2, InternalSyntaxToken endKeyword, InternalSyntaxToken semi) {
		super();
		this.forKeyword = forKeyword;
		this.correlationName = correlationName;
		this.asKeyword = asKeyword;
		this.fieldReference = fieldReference;
		this.doKeyword=doKeyword;
		this.statements = statements;
		this.forKeyword2 = forKeyword2;
		this.endKeyword = endKeyword;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}

	@Override
	public InternalSyntaxToken correlationName() {
		return correlationName;
	}

	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	@Override
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
	}
	
	@Override
	public InternalSyntaxToken doKeyword() {
		return doKeyword;
	}

	@Override
	public List<StatementTree> statements() {
		return statements;
	}

	@Override
	public InternalSyntaxToken forKeyword2() {
		return forKeyword2;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitForStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.FOR_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(forKeyword, correlationName, asKeyword, fieldReference),
				statements.iterator(), Iterators.forArray(forKeyword2, endKeyword, semi));
	}

}
