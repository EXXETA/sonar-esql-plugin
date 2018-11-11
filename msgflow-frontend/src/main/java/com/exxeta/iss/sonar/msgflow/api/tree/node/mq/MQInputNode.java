package com.exxeta.iss.sonar.msgflow.api.tree.node.mq;

import com.exxeta.iss.sonar.msgflow.api.tree.Located;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;

public interface MQInputNode extends MessageFlowNode, Located {
	String queueName();
}
