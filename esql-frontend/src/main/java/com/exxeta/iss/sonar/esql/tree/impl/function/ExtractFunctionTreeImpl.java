package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ExtractFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ExtractFunctionTreeImpl extends EsqlTree implements ExtractFunctionTree {
	private InternalSyntaxToken extractKeyword;
	private InternalSyntaxToken openingParenthesis;
	private InternalSyntaxToken type;
	private InternalSyntaxToken fromKeyword;
	private ExpressionTree sourceDate;
	private InternalSyntaxToken closingParenthesis;

	public ExtractFunctionTreeImpl(InternalSyntaxToken extractKeyword, InternalSyntaxToken openingParenthesis,
			InternalSyntaxToken type, InternalSyntaxToken fromKeyword, ExpressionTree sourceDate,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.extractKeyword = extractKeyword;
		this.openingParenthesis = openingParenthesis;
		this.type = type;
		this.fromKeyword = fromKeyword;
		this.sourceDate = sourceDate;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public InternalSyntaxToken extractKeyword() {
		return extractKeyword;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public InternalSyntaxToken type() {
		return type;
	}

	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}

	@Override
	public ExpressionTree sourceDate() {
		return sourceDate;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitExtractFunction(this);
	}

	@Override
	public Kind getKind() {
		return Kind.EXTRACT_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(extractKeyword, openingParenthesis, type, fromKeyword, sourceDate, closingParenthesis);
	}

	
	
}
