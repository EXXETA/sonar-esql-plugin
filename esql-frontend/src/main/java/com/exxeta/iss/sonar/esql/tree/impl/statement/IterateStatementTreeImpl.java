package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IterateStatementTreeImpl extends EsqlTree implements IterateStatementTree{

	private final  InternalSyntaxToken iterateKeyword;
	private final LabelTreeImpl label;
	private final InternalSyntaxToken semi;
	public IterateStatementTreeImpl(InternalSyntaxToken iterateKeyword, LabelTreeImpl label, InternalSyntaxToken semi) {
		super();
		this.iterateKeyword = iterateKeyword;
		this.label = label;
		this.semi = semi;
	}
	public InternalSyntaxToken iterateKeyword() {
		return iterateKeyword;
	}
	public LabelTreeImpl label() {
		return label;
	}
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIterateStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.ITERATE_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(iterateKeyword, label, semi);
	}
	
	
	
	
	

}
