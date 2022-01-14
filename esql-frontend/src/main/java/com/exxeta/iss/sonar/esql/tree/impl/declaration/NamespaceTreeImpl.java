/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class NamespaceTreeImpl extends EsqlTree implements NamespaceTree{

	private InternalSyntaxToken identifier;

	public NamespaceTreeImpl(InternalSyntaxToken identifier) {
		this.identifier=identifier;
	}
	
	@Override
	public SyntaxToken identifier() {
		return identifier;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitNamespace(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.NAMESPACE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.singletonIterator(identifier);
	}
	
	

}
