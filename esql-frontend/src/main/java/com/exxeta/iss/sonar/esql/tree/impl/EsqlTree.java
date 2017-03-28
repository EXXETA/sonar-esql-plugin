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
