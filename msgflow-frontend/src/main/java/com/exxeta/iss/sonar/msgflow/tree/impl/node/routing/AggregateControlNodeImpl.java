package com.exxeta.iss.sonar.msgflow.tree.impl.node.routing;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.tree.node.routing.AggregateControlNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public class AggregateControlNodeImpl extends AbstractMessageFlowNode implements AggregateControlNode {
	private final String timeoutInterval;
	private final String timeoutLocation;
	private final String aggregateName;

	public AggregateControlNodeImpl(final Node node, final String id, final String name, final int locationX,
			final int locationY, final String timeoutLocation, final String timeoutInterval,
			final String aggregateName) {
		super(node, id, name, locationX, locationY);
		this.timeoutInterval = timeoutInterval;
		this.timeoutLocation = timeoutLocation;
		this.aggregateName = aggregateName;
	}

	@Override
	public void accept(final MsgflowVisitor visitor) {
		visitor.visitAggregateControlNode(this);
	}

	@Override
	public String aggregateName() {
		return aggregateName;
	}

	@Override
	public String timeoutInterval() {
		return timeoutInterval;
	}

	@Override
	public String timeoutLocation() {
		return timeoutLocation;
	}

}
