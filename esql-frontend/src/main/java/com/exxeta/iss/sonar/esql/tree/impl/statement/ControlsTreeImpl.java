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
import com.exxeta.iss.sonar.esql.api.tree.statement.ControlsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ControlsTreeImpl extends EsqlTree implements ControlsTree{

	private InternalSyntaxToken finalizeKeyword;
	private InternalSyntaxToken finalizeType;
	private InternalSyntaxToken deleteKeyword;
	private InternalSyntaxToken deleteType;
	
	
	public ControlsTreeImpl(InternalSyntaxToken finalizeKeyword, InternalSyntaxToken finalizeType,
			InternalSyntaxToken deleteKeyword, InternalSyntaxToken deleteType) {
		super();
		this.finalizeKeyword = finalizeKeyword;
		this.finalizeType = finalizeType;
		this.deleteKeyword = deleteKeyword;
		this.deleteType = deleteType;
	}

@Override
	public InternalSyntaxToken finalizeKeyword() {
		return finalizeKeyword;
	}


@Override
	public InternalSyntaxToken finalizeType() {
		return finalizeType;
	}


@Override
	public InternalSyntaxToken deleteKeyword() {
		return deleteKeyword;
	}


@Override
	public InternalSyntaxToken deleteType() {
		return deleteType;
	}
	
	
@Override
public Kind getKind() {
	return Kind.MESSAGE_SOURCE;
}

@Override
public Iterator<Tree> childrenIterator() {
	return Iterators.forArray(finalizeKeyword, finalizeType, deleteKeyword, deleteType);
}

@Override
public void accept(DoubleDispatchVisitor visitor) {
	visitor.visitControls(this);
}
	
	

}
