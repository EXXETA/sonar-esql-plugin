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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ExternalRoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class ExternalRoutineBodyTreeImpl extends EsqlTree implements ExternalRoutineBodyTree {

	private final SyntaxToken externalKeyword;
	private final SyntaxToken nameKeyword;
	private final SyntaxToken expression;

	public ExternalRoutineBodyTreeImpl(SyntaxToken externalKeyword, SyntaxToken nameKeyword, SyntaxToken expression) {
		this.externalKeyword = externalKeyword;
		this.nameKeyword = nameKeyword;
		this.expression = expression;
	}

	@Override
	public SyntaxToken externalKeyword() {
		return externalKeyword;
	}

	@Override
	public SyntaxToken nameKeyword() {
		return nameKeyword;
	}

	@Override
	public SyntaxToken expression() {
		return expression;
	}

	@Override
	public Kind getKind() {
		return Kind.EXTERNAL_ROUTINE_BODY;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(externalKeyword, nameKeyword, expression);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitExternalRoutineBody(this);
	}

}
