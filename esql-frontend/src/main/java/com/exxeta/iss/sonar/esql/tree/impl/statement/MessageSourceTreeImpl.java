package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MessageSourceTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class MessageSourceTreeImpl extends EsqlTree implements MessageSourceTree {

	private InternalSyntaxToken environmentKeyword;
	private InternalSyntaxToken environment;
	private InternalSyntaxToken messageKeyword;
	private InternalSyntaxToken message;
	private InternalSyntaxToken exceptionKeyword;
	private InternalSyntaxToken exception;

	public MessageSourceTreeImpl(InternalSyntaxToken environmentKeyword, InternalSyntaxToken environment,
			InternalSyntaxToken messageKeyword, InternalSyntaxToken message, InternalSyntaxToken exceptionKeyword,
			InternalSyntaxToken exception) {
		super();
		this.environmentKeyword = environmentKeyword;
		this.environment = environment;
		this.messageKeyword = messageKeyword;
		this.message = message;
		this.exceptionKeyword = exceptionKeyword;
		this.exception = exception;
	}

	@Override
	public InternalSyntaxToken environmentKeyword() {
		return environmentKeyword;
	}

	@Override
	public InternalSyntaxToken environment() {
		return environment;
	}

	@Override
	public InternalSyntaxToken messageKeyword() {
		return messageKeyword;
	}

	@Override
	public InternalSyntaxToken message() {
		return message;
	}

	@Override
	public InternalSyntaxToken exceptionKeyword() {
		return exceptionKeyword;
	}

	@Override
	public InternalSyntaxToken exception() {
		return exception;
	}

	@Override
	public Kind getKind() {
		return Kind.MESSAGE_SOURCE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(environmentKeyword, environment, messageKeyword, message, exceptionKeyword,
				exception);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitMessageSource(this);
	}

}
