package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SubstringFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class SubstringFunctionTreeImpl extends EsqlTree implements SubstringFunctionTree{
	private InternalSyntaxToken substringKeyword;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree sourceExpression; 
	private InternalSyntaxToken qualifier; 
	private ExpressionTree location;
	private InternalSyntaxToken forKeyword;
	private ExpressionTree stringLength;
	private InternalSyntaxToken closingParenthesis;
	public SubstringFunctionTreeImpl(InternalSyntaxToken substringKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceExpression, InternalSyntaxToken qualifier, ExpressionTree location,
			InternalSyntaxToken forKeyword, ExpressionTree stringLength, InternalSyntaxToken closingParenthesis) {
		super();
		this.substringKeyword = substringKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceExpression = sourceExpression;
		this.qualifier = qualifier;
		this.location = location;
		this.forKeyword = forKeyword;
		this.stringLength = stringLength;
		this.closingParenthesis = closingParenthesis;
	}
	public SubstringFunctionTreeImpl(InternalSyntaxToken substringKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceExpression, InternalSyntaxToken qualifier, ExpressionTree location,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.substringKeyword = substringKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceExpression = sourceExpression;
		this.qualifier = qualifier;
		this.location = location;
		this.forKeyword = null;
		this.stringLength = null;
		this.closingParenthesis = closingParenthesis;
	}
	public InternalSyntaxToken substringKeyword() {
		return substringKeyword;
	}
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	public ExpressionTree sourceExpression() {
		return sourceExpression;
	}
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}
	public ExpressionTree location() {
		return location;
	}
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}
	public ExpressionTree stringLength() {
		return stringLength;
	}
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSubstringFunction(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.SUBSTRING_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(substringKeyword, openingParenthesis, sourceExpression, qualifier, location, forKeyword, stringLength, closingParenthesis);
	}
	
	
	
	
	
}
