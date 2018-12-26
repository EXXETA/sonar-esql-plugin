package com.exxeta.iss.sonar.msgflow.api.tree.node.transformation;

import com.exxeta.iss.sonar.msgflow.api.tree.Located;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;

public interface ComputeNode extends MessageFlowNode, Located {
	String dataSource();
}
