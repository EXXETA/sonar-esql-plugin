package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.BrokerSchemaStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class BrokerSchemaStatementTreeImpl extends EsqlTree implements BrokerSchemaStatementTree {
	private final SyntaxToken brokerKeyword;
	private final SyntaxToken schemaKeyword;
	private final SchemaNameTree schemaName;

	public BrokerSchemaStatementTreeImpl(SyntaxToken brokerKeyword, SyntaxToken schemaKeyword,
			SchemaNameTree schemaName) {
		super();
		this.brokerKeyword = brokerKeyword;
		this.schemaKeyword = schemaKeyword;
		this.schemaName = schemaName;
	}

	@Override
	public SyntaxToken brokerKeyword() {
		return brokerKeyword;
	}

	@Override
	public SyntaxToken schemaKeyword() {
		return schemaKeyword;
	}

	@Override
	public SchemaNameTree schemaName() {
		return schemaName;
	}

	@Override
	public Kind getKind() {
		return Kind.BROKER_SCHEMA_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(brokerKeyword, schemaKeyword, schemaName);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitBrokerSchemaStatement(this);
	}

}
