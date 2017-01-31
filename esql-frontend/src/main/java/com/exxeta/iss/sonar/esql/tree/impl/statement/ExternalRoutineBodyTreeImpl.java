package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ExternalRoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class ExternalRoutineBodyTreeImpl extends EsqlTree implements ExternalRoutineBodyTree {

	private final SyntaxToken externalKeyword;
	private final SyntaxToken nameKeyword;
	private final SyntaxToken expression;

	public ExternalRoutineBodyTreeImpl(SyntaxToken externalKeyword, SyntaxToken nameKeyword, SyntaxToken expression) {
		this.externalKeyword = externalKeyword;
		this.nameKeyword = nameKeyword;
		this.expression = expression;
	}

	@Override
	public SyntaxToken externalKeyword() {
		return externalKeyword;
	}

	@Override
	public SyntaxToken nameKeyword() {
		return nameKeyword;
	}

	@Override
	public SyntaxToken expression() {
		return expression;
	}

	@Override
	public Kind getKind() {
		return Kind.EXTERNAL_ROUTINE_BODY;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(externalKeyword, nameKeyword, expression);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitExternalRoutineBody(this);
	}

}
