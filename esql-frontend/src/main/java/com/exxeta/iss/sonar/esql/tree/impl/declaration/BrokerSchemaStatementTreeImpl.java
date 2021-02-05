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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.BrokerSchemaStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class BrokerSchemaStatementTreeImpl extends EsqlTree implements BrokerSchemaStatementTree {
	private final SyntaxToken brokerKeyword;
	private final SyntaxToken schemaKeyword;
	private final SchemaNameTree schemaName;

	public BrokerSchemaStatementTreeImpl(SyntaxToken brokerKeyword, SyntaxToken schemaKeyword,
			SchemaNameTree schemaName) {
		super();
		this.brokerKeyword = brokerKeyword;
		this.schemaKeyword = schemaKeyword;
		this.schemaName = schemaName;
	}

	@Override
	public SyntaxToken brokerKeyword() {
		return brokerKeyword;
	}

	@Override
	public SyntaxToken schemaKeyword() {
		return schemaKeyword;
	}

	@Override
	public SchemaNameTree schemaName() {
		return schemaName;
	}

	@Override
	public Kind getKind() {
		return Kind.BROKER_SCHEMA_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(brokerKeyword, schemaKeyword, schemaName);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitBrokerSchemaStatement(this);
	}

}
