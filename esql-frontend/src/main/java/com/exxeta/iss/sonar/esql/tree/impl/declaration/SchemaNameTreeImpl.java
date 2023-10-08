/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class SchemaNameTreeImpl  extends EsqlTree implements SchemaNameTree {
	private final SeparatedList<InternalSyntaxToken> schemaElements;
	
	public SchemaNameTreeImpl( SeparatedList<InternalSyntaxToken> schemaElements) {
		this.schemaElements=schemaElements;
	}
	
	@Override
	public SeparatedList<InternalSyntaxToken> schemaElements() {
		return schemaElements;
	}

	@Override
	public Kind getKind() {
		return Kind.PATH_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return schemaElements.elementsAndSeparators(Functions.<InternalSyntaxToken>identity());
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSchemaName(this);
	}

	public String text(){
		StringBuilder sb = new StringBuilder();
		for (InternalSyntaxToken element : schemaElements){
			if (sb.length()>0){
				sb.append('.');
			}
				
			sb.append(element.text());
		}
		return sb.toString();
	}

}
