package com.exxeta.iss.sonar.msgflow.parser;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitor;

public class AggregateControlNode extends MessageFlowNode {
	private final String timeoutLocation;
	private final String timeoutInterval;
	private final String aggregateName;

	public AggregateControlNode(final Node node, final String id, final String name, final String location,
			final String timeoutLocation, final String timeoutInterval, final String aggregateName) {
		super(node, id, name, location);
		this.timeoutLocation = timeoutLocation;
		this.timeoutInterval = timeoutInterval;
		this.aggregateName = aggregateName;
	}

	@Override
	public void accept(final MsgflowVisitor visitor) {
		visitor.visitAggregateControlNode(this);

	}

	public String getAggregateName() {
		return aggregateName;
	}

	public String getTimeoutInterval() {
		return timeoutInterval;
	}

	public String getTimeoutLocation() {
		return timeoutLocation;
	}

}
