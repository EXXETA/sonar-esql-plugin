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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class AliasedFieldReferenceTreeImpl extends EsqlTree implements AliasedFieldReferenceTree{
	private FieldReferenceTreeImpl fieldRefernce;
	private InternalSyntaxToken asKeyword;
	private InternalSyntaxToken alias;
	public AliasedFieldReferenceTreeImpl(FieldReferenceTreeImpl fieldRefernce, InternalSyntaxToken asKeyword,
			InternalSyntaxToken alias) {
		super();
		this.fieldRefernce = fieldRefernce;
		this.asKeyword = asKeyword;
		this.alias = alias;
	}
	@Override
	public FieldReferenceTreeImpl fieldRefernce() {
		return fieldRefernce;
	}
	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}
	@Override
	public InternalSyntaxToken alias() {
		return alias;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAliasedFieldReference(this);
	}
	@Override
	public Kind getKind() {
		return Kind.ALIASED_FIELD_REFERENCE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(fieldRefernce, asKeyword, alias);
	}
	
	
	
	
}
