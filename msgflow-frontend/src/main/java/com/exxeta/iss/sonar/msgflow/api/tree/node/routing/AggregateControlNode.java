package com.exxeta.iss.sonar.msgflow.api.tree.node.routing;

import com.exxeta.iss.sonar.msgflow.api.tree.Located;
import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;

public interface AggregateControlNode extends Located, MessageFlowNode {
	String aggregateName();

	String timeoutInterval();

	String timeoutLocation();

}
