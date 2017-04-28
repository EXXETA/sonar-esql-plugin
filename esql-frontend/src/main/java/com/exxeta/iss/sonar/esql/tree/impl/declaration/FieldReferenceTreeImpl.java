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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class FieldReferenceTreeImpl extends EsqlTree implements FieldReferenceTree {

	private final ExpressionTree primaryExpression;
	private final InternalSyntaxToken variable;
	private final PathElementTree pathElement;
	private final SeparatedList<PathElementTree> pathElements;

	public FieldReferenceTreeImpl(ExpressionTree primaryExpression, SeparatedList<PathElementTree> pathElements) {
		this.primaryExpression = primaryExpression;
		this.variable=null;
		this.pathElements = pathElements;
		this.pathElement=null;
	}

	public FieldReferenceTreeImpl(InternalSyntaxToken variable, SeparatedList<PathElementTree> pathElements) {
		this.primaryExpression=null;
		this.variable = variable;
		this.pathElements = pathElements;
		this.pathElement=null;
	}

	public FieldReferenceTreeImpl(PathElementTree pathElement, SeparatedList<PathElementTree> pathElements) {
		this.primaryExpression=null;
		this.variable = null;
		this.pathElements = pathElements;
		this.pathElement=pathElement;
	}

	@Override
	public ExpressionTree primaryExpression() {
		return primaryExpression;
	}
	
	@Override
	public InternalSyntaxToken variable() {
		return variable;
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
		return Iterators.concat(Iterators.forArray(primaryExpression, variable, pathElement),
				pathElements.elementsAndSeparators(Functions.<PathElementTree>identity()));
	}

}
