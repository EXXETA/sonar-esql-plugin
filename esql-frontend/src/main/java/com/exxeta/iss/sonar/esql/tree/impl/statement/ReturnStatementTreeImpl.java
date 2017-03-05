package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.sonar.sslr.api.typed.Optional;

public class ReturnStatementTreeImpl extends EsqlTree implements ReturnStatementTree{
	private InternalSyntaxToken returnKeyword;
	private ExpressionTree expression;
	private InternalSyntaxToken semi;
	public ReturnStatementTreeImpl(InternalSyntaxToken returnKeyword, ExpressionTree expression,
			InternalSyntaxToken semi) {
		super();
		this.returnKeyword = returnKeyword;
		this.expression = expression;
		this.semi = semi;
	}
	public InternalSyntaxToken returnKeyword() {
		return returnKeyword;
	}
	public ExpressionTree expression() {
		return expression;
	}
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitReturnStatement(this);
	}
	@Override
	public Kind getKind() {
		return Kind.RETURN_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(returnKeyword, expression, semi);
	}
	
}
