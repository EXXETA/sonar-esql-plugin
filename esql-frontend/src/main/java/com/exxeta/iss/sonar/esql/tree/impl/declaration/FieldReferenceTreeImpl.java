/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class FieldReferenceTreeImpl extends EsqlTree implements FieldReferenceTree, TypableTree {

	
	private final PathElementTree pathElement;
	private final SeparatedList<PathElementTree> pathElements;

	private TypeSet types = TypeSet.emptyTypeSet();


	public FieldReferenceTreeImpl(PathElementTree pathElement, SeparatedList<PathElementTree> pathElements) {
		
		this.pathElements = pathElements;
		this.pathElement = pathElement;
	}

	@Override
	public PathElementTree pathElement() {
		return pathElement;
	}

	@Override
	public SeparatedList<PathElementTree> pathElements() {
		return pathElements;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitFieldReference(this);

	}

	@Override
	public Kind getKind() {

		return Kind.FIELD_REFERENCE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray( pathElement),
				pathElements.elementsAndSeparators(Functions.<PathElementTree>identity()));
	}

	@Override
	public TypeSet types() {
		return types;
	}

	@Override
	public void add(Type type) {
		types.add(type);
	}

}
