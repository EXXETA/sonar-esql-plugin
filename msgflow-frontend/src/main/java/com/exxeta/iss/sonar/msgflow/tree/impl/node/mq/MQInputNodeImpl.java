package com.exxeta.iss.sonar.msgflow.tree.impl.node.mq;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQInputNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public class MQInputNodeImpl extends AbstractMessageFlowNode implements MQInputNode {
	private final String queueName;

	public MQInputNodeImpl(final Node node, final String id, final String name, final int locationX,
			final int locationY, final String queueName) {
		super(node, id, name, locationX, locationY);
		this.queueName = queueName;
	}

	@Override
	public void accept(final MsgflowVisitor visitor) {
		visitor.visitMQInputNode(this);
	}

	@Override
	public String queueName() {
		return queueName;
	}

}
