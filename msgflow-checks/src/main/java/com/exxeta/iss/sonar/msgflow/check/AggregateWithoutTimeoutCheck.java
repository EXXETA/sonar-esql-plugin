package com.exxeta.iss.sonar.msgflow.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.msgflow.api.tree.node.routing.AggregateControlNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitorCheck;

@Rule(key = "AggregateWithoutTimeout")
public class AggregateWithoutTimeoutCheck extends DoubleDispatchMsgflowVisitorCheck {

	private static final String MESSAGE = "'timeoutInterval' property for Aggregate Control Node '%s' is set to infinite(value = 0).";

	@Override
	public void visitAggregateControlNode(final AggregateControlNode aggregateControlNode) {
		if (aggregateControlNode.timeoutInterval().equals("0")) {
			addIssue(aggregateControlNode, String.format(MESSAGE, aggregateControlNode.name()));
		}
	}

}
