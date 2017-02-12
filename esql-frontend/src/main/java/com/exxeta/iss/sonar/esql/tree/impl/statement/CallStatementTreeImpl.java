package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class CallStatementTreeImpl extends EsqlTree implements CallStatementTree{

	private InternalSyntaxToken callKeyword;
	private SchemaNameTree schemaName;
	private InternalSyntaxToken dot;
	private InternalSyntaxToken routineName;
	private InternalSyntaxToken openParen;
	private SeparatedList<ExpressionTree> parameterList;
	private InternalSyntaxToken closeParen;
	private InternalSyntaxToken inKeyword;
	private FieldReferenceTreeImpl schemaReference;
	private InternalSyntaxToken externalKeyword;
	private InternalSyntaxToken schemaKeyword;
	private InternalSyntaxToken externalSchemaName;
	private InternalSyntaxToken intoKeyword;
	private FieldReferenceTreeImpl intoTarget;
	private InternalSyntaxToken semi;

	public CallStatementTreeImpl(InternalSyntaxToken callKeyword, SchemaNameTree schemaName, InternalSyntaxToken dot,
			InternalSyntaxToken routineName, InternalSyntaxToken openParen, SeparatedList<ExpressionTree> parameterList,
			InternalSyntaxToken closeParen, InternalSyntaxToken semi) {
		this.callKeyword=callKeyword;
		this.schemaName=schemaName;
		this.dot=dot;
		this.routineName=routineName;
		this.openParen=openParen;
		this.parameterList=parameterList;
		this.closeParen=closeParen;
		this.semi=semi;
	}

	public void inClause(InternalSyntaxToken inKeyword, FieldReferenceTreeImpl schemaReference) {
		this.inKeyword=inKeyword;
		this.schemaReference=schemaReference;
		
	}

	public void externalSchema(InternalSyntaxToken externalKeyword, InternalSyntaxToken schemaKeyword, InternalSyntaxToken externalSchemaName) {
		this.externalKeyword=externalKeyword;
		this.schemaKeyword=schemaKeyword;
		this.externalSchemaName=externalSchemaName;
		
	}

	public void intoClause(InternalSyntaxToken intoKeyword, FieldReferenceTreeImpl intoTarget) {
		this.intoKeyword=intoKeyword;
		this.intoTarget=intoTarget;
		
	}

	public InternalSyntaxToken callKeyword() {
		return callKeyword;
	}

	public SchemaNameTree schemaName() {
		return schemaName;
	}

	public InternalSyntaxToken dot() {
		return dot;
	}

	public InternalSyntaxToken routineName() {
		return routineName;
	}

	public InternalSyntaxToken openParen() {
		return openParen;
	}

	public SeparatedList<ExpressionTree> parameterList() {
		return parameterList;
	}

	public InternalSyntaxToken closeParen() {
		return closeParen;
	}

	public InternalSyntaxToken inKeyword() {
		return inKeyword;
	}

	public FieldReferenceTreeImpl schemaReference() {
		return schemaReference;
	}

	public InternalSyntaxToken externalKeyword() {
		return externalKeyword;
	}

	public InternalSyntaxToken schemaKeyword() {
		return schemaKeyword;
	}

	public InternalSyntaxToken externalSchemaName() {
		return externalSchemaName;
	}

	public InternalSyntaxToken intoKeyword() {
		return intoKeyword;
	}

	public FieldReferenceTreeImpl intoTarget() {
		return intoTarget;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCallStatement(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.CALL_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(
				Iterators.forArray(	callKeyword,schemaName,	dot, routineName, openParen),
				parameterList.elementsAndSeparators(Functions.<ExpressionTree> identity()),
				Iterators.forArray( closeParen, inKeyword, schemaReference, externalKeyword, schemaKeyword, externalSchemaName, intoKeyword, intoTarget, semi)
				
				);
	}	
	

}
