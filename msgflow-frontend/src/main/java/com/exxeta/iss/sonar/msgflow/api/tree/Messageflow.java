package com.exxeta.iss.sonar.msgflow.api.tree;

import java.util.List;

import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public interface Messageflow {

	List<? extends AbstractMessageFlowNode> getMessageFlowNodes() ;
	List<?extends MessageFlowConnection> connections();
}
