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
package com.exxeta.iss.sonar.esql.tree.impl;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public abstract class EsqlTree implements Tree {

	private Tree parent;

	public int getLine() {
		return firstToken().line();
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

	 @Override
	  public boolean isAncestorOf(Tree tree) {
	    Tree parentTree = tree.parent();
	    if (this.equals(parentTree)) {
	      return true;
	    }
	    if (parentTree == null) {
	      return false;
	    }
	    return this.isAncestorOf(parentTree);
	  }
	 
	 @Override
	  public Stream<EsqlTree> descendants() {
	    if (this.isLeaf()) {
	      return Stream.empty();
	    }
	    Stream<EsqlTree> kins = childrenStream().filter(Objects::nonNull)
	      .filter(tree -> tree instanceof EsqlTree)
	      .map(tree -> (EsqlTree) tree);
	    for (Iterator<Tree> iterator = this.childrenIterator(); iterator.hasNext();) {
	      Tree tree = iterator.next();
	      if (tree != null) {
	        kins = Stream.concat(kins, tree.descendants());
	      }
	    }
	    return kins;
	  }
	 
	  @Override
	  public Stream<Tree> childrenStream() {
	    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(childrenIterator(), Spliterator.ORDERED), false);
	  }

	
	public abstract Iterator<Tree> childrenIterator();

	public boolean isLeaf() {
		return false;
	}

	public SyntaxToken lastToken() {
		SyntaxToken lastToken = null;
		Iterator<Tree> childrenIterator = childrenIterator();
		while (childrenIterator.hasNext()) {
			EsqlTree child = (EsqlTree) childrenIterator.next();
			if (child != null) {
				SyntaxToken childLastToken = child.lastToken();
				if (childLastToken != null) {
					lastToken = childLastToken;
				}
			}
		}
		return lastToken;
	}

	public SyntaxToken firstToken() {
		Iterator<Tree> childrenIterator = childrenIterator();
		Tree child;
		do {
			if (childrenIterator.hasNext()) {
				child = childrenIterator.next();
			} else {
				throw new IllegalStateException("Tree has no non-null children " + getKind());
			}
		} while (child == null);
		return ((EsqlTree) child).firstToken();
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public Tree parent() {
		return parent;
	}
}
