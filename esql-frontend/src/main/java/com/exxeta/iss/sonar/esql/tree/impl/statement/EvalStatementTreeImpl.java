package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.EvalStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class EvalStatementTreeImpl extends EsqlTree implements EvalStatementTree{
	private InternalSyntaxToken evalKeyword;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree expression;
	private InternalSyntaxToken closingParenthesis;
	private InternalSyntaxToken semi;
	public EvalStatementTreeImpl(InternalSyntaxToken evalKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, InternalSyntaxToken closingParenthesis, InternalSyntaxToken semi) {
		super();
		this.evalKeyword = evalKeyword;
		this.openingParenthesis = openingParenthesis;
		this.expression = expression;
		this.closingParenthesis = closingParenthesis;
		this.semi=semi;
	}
	public InternalSyntaxToken evalKeyword() {
		return evalKeyword;
	}
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	public ExpressionTree expression() {
		return expression;
	}
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitEvalStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.EVAL_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(evalKeyword, openingParenthesis, expression, closingParenthesis, semi);
	}
	
	
	
	
}
