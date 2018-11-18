package com.exxeta.iss.sonar.msgflow.tree.impl;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;

public abstract class MsgflowTree implements Tree {

	private final Node domNode;

	public MsgflowTree(final Node domNode) {
		this.domNode = domNode;
	}

	@Override
	public int endColumn() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[1])-1;
	}

	@Override
	public int endLine() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[0]);
	}

	public abstract Kind getKind();

	@Override
	public final boolean is(final Kind... kind) {
		if (getKind() != null) {
			for (final Kind kindIter : kind) {
				if (getKind() == kindIter) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int startColumn() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[1])-2;
	}

	@Override
	public int startLine() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[0]);
	}
	
}
