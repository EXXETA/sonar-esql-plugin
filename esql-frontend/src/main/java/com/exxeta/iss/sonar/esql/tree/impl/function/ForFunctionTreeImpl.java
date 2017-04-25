package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ForFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ForFunctionTreeImpl extends EsqlTree implements ForFunctionTree {
	private InternalSyntaxToken forKeyword;
	private InternalSyntaxToken qualifier;
	private FieldReferenceTreeImpl fieldReference;
	private InternalSyntaxToken asKeyword;
	private InternalSyntaxToken asIdentifier;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree expression;
	private InternalSyntaxToken closingParenthesis;
	public ForFunctionTreeImpl(InternalSyntaxToken forKeyword, InternalSyntaxToken qualifier,
			FieldReferenceTreeImpl fieldReference, InternalSyntaxToken asKeyword, InternalSyntaxToken asIdentifier,
			InternalSyntaxToken openingParenthesis, ExpressionTree expression, InternalSyntaxToken closingParenthesis) {
		super();
		this.forKeyword = forKeyword;
		this.qualifier = qualifier;
		this.fieldReference = fieldReference;
		this.asKeyword = asKeyword;
		this.asIdentifier = asIdentifier;
		this.openingParenthesis = openingParenthesis;
		this.expression = expression;
		this.closingParenthesis = closingParenthesis;
	}
	public ForFunctionTreeImpl(InternalSyntaxToken forKeyword, InternalSyntaxToken qualifier,
			FieldReferenceTreeImpl fieldReference, InternalSyntaxToken openingParenthesis, ExpressionTree expression,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.forKeyword = forKeyword;
		this.qualifier = qualifier;
		this.fieldReference = fieldReference;
		this.openingParenthesis = openingParenthesis;
		this.expression = expression;
		this.closingParenthesis = closingParenthesis;
	}
	@Override
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}
	@Override
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}
	@Override
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
	}
	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}
	@Override
	public InternalSyntaxToken asIdentifier() {
		return asIdentifier;
	}
	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitForFunction(this);
	}
	@Override
	public Kind getKind() {
		return Kind.FOR_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(forKeyword, qualifier, fieldReference, asKeyword, asIdentifier, openingParenthesis, expression, closingParenthesis);
	}
	
	
}
