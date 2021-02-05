/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class CallStatementTreeImpl extends EsqlTree implements CallStatementTree{

	private InternalSyntaxToken callKeyword;
	private SchemaNameTree routineName;
	private InternalSyntaxToken openParen;
	private SeparatedList<ExpressionTree> parameterList;
	private InternalSyntaxToken closeParen;
	private InternalSyntaxToken inKeyword;
	private FieldReferenceTreeImpl schemaReference;
	private InternalSyntaxToken externalKeyword;
	private InternalSyntaxToken schemaKeyword;
	private ExpressionTree externalSchemaName;
	private InternalSyntaxToken intoKeyword;
	private FieldReferenceTreeImpl intoTarget;
	private InternalSyntaxToken semi;

	public CallStatementTreeImpl(InternalSyntaxToken callKeyword, SchemaNameTree routineName, 
			InternalSyntaxToken openParen, SeparatedList<ExpressionTree> parameterList,
			InternalSyntaxToken closeParen, InternalSyntaxToken semi) {
		this.callKeyword=callKeyword;
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

	public void externalSchema(InternalSyntaxToken externalKeyword, InternalSyntaxToken schemaKeyword, ExpressionTree externalSchemaName) {
		this.externalKeyword=externalKeyword;
		this.schemaKeyword=schemaKeyword;
		this.externalSchemaName=externalSchemaName;
		
	}

	public void intoClause(InternalSyntaxToken intoKeyword, FieldReferenceTreeImpl intoTarget) {
		this.intoKeyword=intoKeyword;
		this.intoTarget=intoTarget;
		
	}

	@Override
	public InternalSyntaxToken callKeyword() {
		return callKeyword;
	}

	@Override
	public SchemaNameTree routineName() {
		return routineName;
	}

	@Override
	public InternalSyntaxToken openParen() {
		return openParen;
	}

	@Override
	public SeparatedList<ExpressionTree> parameterList() {
		return parameterList;
	}

	@Override
	public InternalSyntaxToken closeParen() {
		return closeParen;
	}

	@Override
	public InternalSyntaxToken inKeyword() {
		return inKeyword;
	}

	@Override
	public FieldReferenceTreeImpl schemaReference() {
		return schemaReference;
	}

	@Override
	public InternalSyntaxToken externalKeyword() {
		return externalKeyword;
	}

	@Override
	public InternalSyntaxToken schemaKeyword() {
		return schemaKeyword;
	}

	@Override
	public ExpressionTree externalSchemaName() {
		return externalSchemaName;
	}

	@Override
	public InternalSyntaxToken intoKeyword() {
		return intoKeyword;
	}

	@Override
	public FieldReferenceTreeImpl intoTarget() {
		return intoTarget;
	}

	@Override
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
				Iterators.forArray(	callKeyword, routineName, openParen),
				parameterList.elementsAndSeparators(Functions.<ExpressionTree> identity()),
				Iterators.forArray( closeParen, inKeyword, schemaReference, externalKeyword, schemaKeyword, externalSchemaName, intoKeyword, intoTarget, semi)
				
				);
	}	
	

}
