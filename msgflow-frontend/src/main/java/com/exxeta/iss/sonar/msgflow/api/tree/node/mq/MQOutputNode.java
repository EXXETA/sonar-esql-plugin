package com.exxeta.iss.sonar.msgflow.api.tree.node.mq;

import com.exxeta.iss.sonar.msgflow.api.tree.Located;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;

public interface MQOutputNode extends MessageFlowNode, Located {
	String queueName();
}
