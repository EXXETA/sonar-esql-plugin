package com.exxeta.iss.sonar.msgflow.api.tree;

import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQInputNode;
import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQOutputNode;
import com.exxeta.iss.sonar.msgflow.api.tree.node.routing.AggregateControlNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;

public interface Tree {
	public enum Kind {
		CONNECTION(MessageFlowConnection.class), AGGREGATE_CONTROL(AggregateControlNode.class),
		MESSAGEFLOW(MsgflowTree.class), MQ_INPUT(MQInputNode.class), MQ_OUTPUT(MQOutputNode.class);

		Class<? extends Tree> associatedInterface;

		Kind(final Class<? extends Tree> associatedInterface) {
			this.associatedInterface = associatedInterface;
		}

		boolean contains(final Object other) {
			return this.equals(other);
		}

		Class<? extends Tree> getAssociatedInterface() {
			return associatedInterface;
		}

	}

	void accept(DoubleDispatchMsgflowVisitor visitor);

	int endColumn();

	int endLine();

	boolean is(Kind... kind);

	int startColumn();

	int startLine();

}
