package com.exxeta.iss.sonar.msgflow.api.tree;

public interface MessageFlowConnection extends Tree {
	String srcNode();

	String srcTerminal();

	String targetNode();

	String targetTerminal();
}
