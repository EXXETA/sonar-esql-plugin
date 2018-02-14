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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class CreateModuleStatementTreeImpl extends EsqlTree implements CreateModuleStatementTree {

	private final InternalSyntaxToken createKeyword;
	private final InternalSyntaxToken moduleType;
	private final InternalSyntaxToken moduleKeyword;
	private final IdentifierTree moduleName;
	private final StatementsTree moduleStatementsList;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken moduleKeyword2;

	public CreateModuleStatementTreeImpl(InternalSyntaxToken createKeyword, InternalSyntaxToken moduleType,
			InternalSyntaxToken moduleKeyword, IdentifierTree indentifier,
			StatementsTree moduleStatementsList, InternalSyntaxToken endKeyword,
			InternalSyntaxToken moduleKeyword2) {
		super();
		this.createKeyword = createKeyword;
		this.moduleType = moduleType;
		this.moduleKeyword = moduleKeyword;
		this.moduleName = indentifier;
		this.moduleStatementsList = moduleStatementsList;
		this.endKeyword = endKeyword;
		this.moduleKeyword2 = moduleKeyword2;
	}

	@Override
	public InternalSyntaxToken createKeyword() {
		return createKeyword;
	}

	@Override
	public InternalSyntaxToken moduleType() {
		return moduleType;
	}

	@Override
	public InternalSyntaxToken moduleKeyword() {
		return moduleKeyword;
	}

	@Override
	public IdentifierTree moduleName() {
		return moduleName;
	}

	@Override
	public StatementsTree moduleStatementsList() {
		return moduleStatementsList;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken moduleKeyword2() {
		return moduleKeyword2;
	}

	@Override
	public Kind getKind() {
		return Kind.CREATE_MODULE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(createKeyword, moduleType, moduleKeyword, moduleName, moduleStatementsList),
				Iterators.forArray(endKeyword, moduleKeyword2));
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCreateModuleStatement(this);
	}

}
