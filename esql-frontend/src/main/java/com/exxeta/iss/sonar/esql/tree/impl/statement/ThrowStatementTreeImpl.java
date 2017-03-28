package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class ThrowStatementTreeImpl extends EsqlTree implements ThrowStatementTree{

	private SyntaxToken throwKeyword;

	private SyntaxToken userKeyword;

	private SyntaxToken exceptionKeyword;

	private SyntaxToken severityKeyword;

	private ExpressionTree severity;

	private SyntaxToken catalogKeyword;

	private ExpressionTree catalog;

	private SyntaxToken messageKeyword;

	private ExpressionTree message;

	private SyntaxToken valuesKeyword;

	private ParameterListTree values;
	
	private SyntaxToken semi;

	public ThrowStatementTreeImpl(SyntaxToken throwKeyword, SyntaxToken userKeyword,
			SyntaxToken exceptionKeyword, SyntaxToken severityKeyword, ExpressionTree severity,
			SyntaxToken catalogKeyword, ExpressionTree catalog, SyntaxToken messageKeyword, ExpressionTree message,
			SyntaxToken valuesKeyword, ParameterListTree values, SyntaxToken semi) {
		super();
		this.throwKeyword = throwKeyword;
		this.userKeyword = userKeyword;
		this.exceptionKeyword = exceptionKeyword;
		this.severityKeyword = severityKeyword;
		this.severity = severity;
		this.catalogKeyword = catalogKeyword;
		this.catalog = catalog;
		this.messageKeyword = messageKeyword;
		this.message = message;
		this.valuesKeyword = valuesKeyword;
		this.values = values;
		this.semi=semi;
	}

	public SyntaxToken throwKeyword() {
		return throwKeyword;
	}

	public SyntaxToken userKeyword() {
		return userKeyword;
	}

	public SyntaxToken exceptionKeyword() {
		return exceptionKeyword;
	}

	public SyntaxToken severityKeyword() {
		return severityKeyword;
	}

	public ExpressionTree severity() {
		return severity;
	}

	public SyntaxToken catalogKeyword() {
		return catalogKeyword;
	}

	public ExpressionTree catalog() {
		return catalog;
	}

	public SyntaxToken messageKeyword() {
		return messageKeyword;
	}

	public ExpressionTree message() {
		return message;
	}

	public SyntaxToken valuesKeyword() {
		return valuesKeyword;
	}

	public ParameterListTree values() {
		return values;
	}
	
	public SyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitThrowStatement(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.THROW_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(throwKeyword, userKeyword, exceptionKeyword, severityKeyword, severity, catalogKeyword, catalog, messageKeyword, message, valuesKeyword, values, semi);
	}
	
	
	
}
