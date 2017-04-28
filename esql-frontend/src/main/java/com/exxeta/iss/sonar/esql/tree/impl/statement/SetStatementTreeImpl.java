/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
