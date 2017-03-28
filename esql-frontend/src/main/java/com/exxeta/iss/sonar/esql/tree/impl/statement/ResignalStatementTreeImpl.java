package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResignalStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ResignalStatementTreeImpl extends EsqlTree implements ResignalStatementTree {

	private InternalSyntaxToken resignalKeyword;
	private InternalSyntaxToken semi;

	public ResignalStatementTreeImpl(InternalSyntaxToken resignalKeyword, InternalSyntaxToken semi) {
		super();
		this.resignalKeyword = resignalKeyword;
		this.semi = semi;
	}

	public InternalSyntaxToken resignalKeyword() {
		return resignalKeyword;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitResignalStatement(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.RESIGNAL_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(resignalKeyword, semi);
	}
	
	

}
