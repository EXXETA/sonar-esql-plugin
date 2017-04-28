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

import com.exxeta.iss.sonar.esql.api.tree.PathClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class PathClauseTreeImpl  extends EsqlTree implements PathClauseTree{

	private final InternalSyntaxToken pathToken;
	private final SeparatedList<SchemaNameTree> schemaNames;
	
	public PathClauseTreeImpl(InternalSyntaxToken pathToken, SeparatedList<SchemaNameTree> schemaNames) {
		this.pathToken=pathToken;
		this.schemaNames=schemaNames;
	}
	
	@Override
	public InternalSyntaxToken pathToken() {
		return pathToken;
	}
	
	@Override
	public SeparatedList<SchemaNameTree> schemaNames() {
		return schemaNames;
	}

	@Override
	public Kind getKind() {
		return Kind.PATH_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(
				Iterators.singletonIterator(pathToken), 
				schemaNames.elementsAndSeparators(Functions.<SchemaNameTree>identity())
		);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathClause(this);
	}

	
}
