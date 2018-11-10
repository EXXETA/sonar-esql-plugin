package com.exxeta.iss.sonar.msgflow.api.tree;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitor;

public abstract class MsgflowTree {

	private final Node domNode;

	public MsgflowTree(final Node domNode) {
		this.domNode = domNode;
	}

	public abstract void accept(MsgflowVisitor visitor);

	public int endColumn() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[1]);
	}

	public int endLine() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[0]);
	}

	public int startColumn() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[1]);
	}

	public int startLine() {
		return domNode == null ? -1 : Integer.parseInt(domNode.getUserData("lineNumber").toString().split(":")[0]);
	}

}
