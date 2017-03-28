package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.BrokerSchemaStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.EsqlContentsTree;
import com.exxeta.iss.sonar.esql.api.tree.PathClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ProgramTreeImpl extends EsqlTree implements ProgramTree {

	@Nullable
	private final BrokerSchemaStatementTree brokerSchema;

	@Nullable
	private final PathClauseTree pathClause;

	@Nullable
	private final InternalSyntaxToken semi;

	private final EsqlContentsTree esqlContents;

	private final InternalSyntaxToken eof;

	public ProgramTreeImpl(BrokerSchemaStatementTree brokerSchema, PathClauseTree pathClause, InternalSyntaxToken semi,
			EsqlContentsTree esqlContents, InternalSyntaxToken eof) {
		super();
		this.brokerSchema = brokerSchema;
		this.pathClause = pathClause;
		this.semi = semi;
		this.esqlContents=esqlContents;
		this.eof = eof;
	}

	@Override
	public BrokerSchemaStatementTree brokerSchemaStatement() {
		return brokerSchema;
	}
	
	@Override
	public PathClauseTree pathClause() {
		return pathClause;
	}
	
	@Override
	public EsqlContentsTree esqlContents() {
		return esqlContents;
	}
	
	@Override
	public SyntaxToken semiToken() {
		return semi;
	}

	@Override
	public SyntaxToken EOFToken() {
		return eof;
	}

	@Override
	public Kind getKind() {
		return Kind.PROGRAM;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.<Tree> forArray(brokerSchema, pathClause, semi, esqlContents, eof);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitProgram(this);
	}

}
