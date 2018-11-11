package com.exxeta.iss.sonar.msgflow.api.tree;

import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public interface Terminal {

	boolean getConnectedTerminal();

	boolean isConnected();

	String name();

	AbstractMessageFlowNode parent();

}
