package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class RoutineBodyTreeImpl extends EsqlTree implements RoutineBodyTree {

	private final StatementTree statement;
	private final ExternalRoutineBodyTreeImpl externalRoutineBody;

	public RoutineBodyTreeImpl(StatementTree statement) {
		this.statement = statement;
		this.externalRoutineBody = null;
	}

	public RoutineBodyTreeImpl(ExternalRoutineBodyTreeImpl externalRoutineBody) {
		this.statement = null;
		this.externalRoutineBody = externalRoutineBody;
	}
	
	@Override
	public StatementTree statement() {
		return statement;
	}
	
	@Override
	public ExternalRoutineBodyTreeImpl externalRoutineBody() {
		return externalRoutineBody;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitRoutineBody(this);

	}

	@Override
	public Kind getKind() {
		return Kind.ROUTINE_BODY;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		if (externalRoutineBody!=null){
			return Iterators.singletonIterator(externalRoutineBody);
		} else {
			return Iterators.singletonIterator(statement);
		}
	}

}
