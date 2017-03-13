package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ValuesClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ValuesClauseTreeImpl extends EsqlTree implements ValuesClauseTree {

	private InternalSyntaxToken identityKeyword;
	private FieldReferenceTreeImpl identity;
	private InternalSyntaxToken typeKeyword;
	private ExpressionTree type;
	private InternalSyntaxToken namespaceKeyword;
	private ExpressionTree namespace;
	private InternalSyntaxToken nameKeyword;
	private ExpressionTree name;
	private InternalSyntaxToken valueKeyword;
	private ExpressionTree value;

	public ValuesClauseTreeImpl(InternalSyntaxToken identityKeyword, FieldReferenceTreeImpl identity,
			InternalSyntaxToken typeKeyword, ExpressionTree type, InternalSyntaxToken namespaceKeyword,
			ExpressionTree namespace, InternalSyntaxToken nameKeyword, ExpressionTree name,
			InternalSyntaxToken valueKeyword, ExpressionTree value) {
		super();
		this.identityKeyword = identityKeyword;
		this.identity = identity;
		this.typeKeyword = typeKeyword;
		this.type = type;
		this.namespaceKeyword = namespaceKeyword;
		this.namespace = namespace;
		this.nameKeyword = nameKeyword;
		this.name = name;
		this.valueKeyword = valueKeyword;
		this.value = value;
	}

	@Override
	public InternalSyntaxToken identityKeyword() {
		return identityKeyword;
	}

	@Override
	public FieldReferenceTreeImpl identity() {
		return identity;
	}

	@Override
	public InternalSyntaxToken typeKeyword() {
		return typeKeyword;
	}

	@Override
	public ExpressionTree type() {
		return type;
	}

	@Override
	public InternalSyntaxToken namespaceKeyword() {
		return namespaceKeyword;
	}

	@Override
	public ExpressionTree namespace() {
		return namespace;
	}

	@Override
	public InternalSyntaxToken nameKeyword() {
		return nameKeyword;
	}

	@Override
	public ExpressionTree name() {
		return name;
	}

	@Override
	public InternalSyntaxToken valueKeyword() {
		return valueKeyword;
	}

	@Override
	public ExpressionTree value() {
		return value;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitValuesClause(this);
	}

	@Override
	public Kind getKind() {
		return Kind.VALUES_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(identityKeyword, identity, typeKeyword, type, namespaceKeyword, namespace,
				nameKeyword, name, valueKeyword, value);
	}

}
