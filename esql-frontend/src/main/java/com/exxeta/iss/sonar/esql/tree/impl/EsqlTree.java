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
package com.exxeta.iss.sonar.esql.tree.impl;

import java.util.Iterator;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public abstract class EsqlTree implements Tree {

	private Tree parent;

	public int getLine() {
		return getFirstToken().line();
	}

	@Override
	public final boolean is(Kind... kind) {
		if (getKind() != null) {
			for (Kind kindIter : kind) {
				if (getKind() == kindIter) {
					return true;
				}
			}
		}
		return false;
	}

	public abstract Kind getKind();

	public abstract Iterator<Tree> childrenIterator();

	public boolean isLeaf() {
		return false;
	}

	public SyntaxToken getLastToken() {
		SyntaxToken lastToken = null;
		Iterator<Tree> childrenIterator = childrenIterator();
		while (childrenIterator.hasNext()) {
			EsqlTree child = (EsqlTree) childrenIterator.next();
			if (child != null) {
				SyntaxToken childLastToken = child.getLastToken();
				if (childLastToken != null) {
					lastToken = childLastToken;
				}
			}
		}
		return lastToken;
	}

	public SyntaxToken getFirstToken() {
		Iterator<Tree> childrenIterator = childrenIterator();
		Tree child;
		do {
			if (childrenIterator.hasNext()) {
				child = childrenIterator.next();
			} else {
				throw new IllegalStateException("Tree has no non-null children " + getKind());
			}
		} while (child == null);
		return ((EsqlTree) child).getFirstToken();
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public Tree getParent() {
		return parent;
	}
}
