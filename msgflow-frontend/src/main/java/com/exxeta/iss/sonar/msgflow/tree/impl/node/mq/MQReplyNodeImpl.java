package com.exxeta.iss.sonar.msgflow.tree.impl.node.mq;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQReplyNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public class MQReplyNodeImpl extends AbstractMessageFlowNode implements MQReplyNode {
	private final String queueName;

	public MQReplyNodeImpl(final Node node, final String id, final String name, final int locationX,
			final int locationY, final String queueName) {
		super(node, id, name, locationX, locationY);
		this.queueName = queueName;
	}

	@Override
	public void accept(final DoubleDispatchMsgflowVisitor visitor) {
		visitor.visitMQReplyNode(this);
	}

	@Override
	public Kind getKind() {
		return Kind.MQ_INPUT;
	}

	@Override
	public String queueName() {
		return queueName;
	}



}
