package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class StatementsTreeImpl extends EsqlTree implements StatementsTree{

	private List<StatementTree> statements;
	
	public StatementsTreeImpl(List<StatementTree> statements) {
		this.statements=statements;
	}
	
	public List<StatementTree> statements() {
		return statements;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitStatements(this);
	}

	@Override
	public Kind getKind() {
		return Kind.STATEMENTS;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(statements.toArray(new Tree[statements.size()]));
	}

	
}
