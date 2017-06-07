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
package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.VariableReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.google.common.collect.Iterators;

public class CallExpressionTreeImpl extends EsqlTree implements CallExpressionTree, TypableTree {

	private FunctionTree function;
	private VariableReferenceTree variableReference;
	private ParameterListTreeImpl parameters;
	private ExpressionTree expression;
	private TypeSet types = TypeSet.emptyTypeSet();

	public CallExpressionTreeImpl(FunctionTree function) {
		this.function = function;
	}

	public CallExpressionTreeImpl(VariableReferenceTree variableReference, ParameterListTreeImpl parameters) {
		this.variableReference = variableReference;
		this.parameters = parameters;
	}

	public CallExpressionTreeImpl(VariableReferenceTree variableReference) {
		this.variableReference = variableReference;
	}

	public CallExpressionTreeImpl(ExpressionTree expression) {
		this.expression = expression;
	}

	@Override
	public FunctionTree function() {
		return function;
	}

	@Override
	public VariableReferenceTree functionName() {
		return variableReference;
	}

	@Override
	public ParameterListTreeImpl parameters() {
		return parameters;
	}

	@Override
	public TypeSet types() {
		return types;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCallExpression(this);

	}

	@Override
	public Kind getKind() {
		return Kind.CALL_EXPRESSION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(expression, function, variableReference, parameters);
	}

	@Override
	public void add(Type type) {
		types.add(type);
	}

}
