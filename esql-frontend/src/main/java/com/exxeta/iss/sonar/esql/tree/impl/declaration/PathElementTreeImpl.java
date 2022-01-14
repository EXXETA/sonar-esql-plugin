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

import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class PathElementTreeImpl extends EsqlTree implements PathElementTree {

	private PathElementTypeTreeImpl type;
	private PathElementNamespaceTreeImpl namespace;
	private PathElementNameTreeImpl name;
	private IndexTreeImpl index;

	public void type(PathElementTypeTreeImpl type){
		this.type=type;
	}
	
	public void namespace(PathElementNamespaceTreeImpl namespace){
		this.namespace=namespace;
	}
	
	public void name(PathElementNameTreeImpl name){
		this.name=name;
	}
	
	public void index(IndexTreeImpl index) {
		this.index = index;
		
	}

	@Override
	public PathElementTypeTreeImpl type() {
		return type;
	}
	
	@Override
	public PathElementNamespaceTreeImpl namespace() {
		return namespace;
	}
	
	@Override
	public PathElementNameTreeImpl name() {
		return name;
	}



	@Override
	public IndexTreeImpl index() {
		return index;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitPathElement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.PATH_ELEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(type, namespace, name, index);
	}

}
