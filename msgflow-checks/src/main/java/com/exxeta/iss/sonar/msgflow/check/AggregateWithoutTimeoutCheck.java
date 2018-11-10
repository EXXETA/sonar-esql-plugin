package com.exxeta.iss.sonar.msgflow.check;

import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorCheck;
import com.exxeta.iss.sonar.msgflow.parser.AggregateControlNode;

public class AggregateWithoutTimeoutCheck extends MsgflowVisitorCheck {

	private static final String MESSAGE = "'timeoutInterval' property for Aggregate Control Node '%s' is set to infinite(value = 0).";

	@Override
	public void visitAggregateControlNode(final AggregateControlNode aggregateControlNode) {
		if (aggregateControlNode.getTimeoutInterval().equals("0")) {
			addIssue(aggregateControlNode, String.format(MESSAGE, aggregateControlNode.getName()));
		}
	}

}
