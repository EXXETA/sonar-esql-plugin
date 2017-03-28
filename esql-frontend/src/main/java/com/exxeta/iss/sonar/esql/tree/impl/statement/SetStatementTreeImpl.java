package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class SetStatementTreeImpl extends EsqlTree implements SetStatementTree{

	private InternalSyntaxToken setKeyword;
	private FieldReferenceTreeImpl fieldReference;
	private InternalSyntaxToken type;
	private InternalSyntaxToken equal;
	private ExpressionTree expression;
	private final InternalSyntaxToken semiToken;

	public SetStatementTreeImpl(InternalSyntaxToken setKeyword, FieldReferenceTreeImpl fieldReference,
			InternalSyntaxToken type, InternalSyntaxToken equal, ExpressionTree expression, InternalSyntaxToken semiToken) {
		super();
		this.setKeyword = setKeyword;
		this.fieldReference = fieldReference;
		this.type = type;
		this.equal = equal;
		this.expression = expression;
		this.semiToken=semiToken;
	}
	public InternalSyntaxToken setKeyword() {
		return setKeyword;
	}
	public FieldReferenceTreeImpl fieldReference() {
		return fieldReference;
	}
	public InternalSyntaxToken type() {
		return type;
	}
	public InternalSyntaxToken equal() {
		return equal;
	}
	public ExpressionTree expression() {
		return expression;
	}
	public InternalSyntaxToken semiToken(){
		return semiToken;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSetStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.SET_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(setKeyword, fieldReference, type, equal, expression, semiToken);
	}
	

}
