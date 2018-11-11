package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.msgflow.api.tree.node.mq.MQInputNode;
import com.exxeta.iss.sonar.msgflow.api.tree.node.routing.AggregateControlNode;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowImpl;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;
import com.google.common.base.Preconditions;

public class MsgflowVisitor {

	private MsgflowVisitorContext context = null;

	public MsgflowVisitorContext getContext() {
		Preconditions.checkState(context != null,
				"this#scanTree(context) should be called to initialised the context before accessing it");
		return context;
	}

	protected <T extends MsgflowTree> void scan(final List<T> trees) {
		trees.forEach(this::scan);
	}

	protected void scan(@Nullable final MsgflowTree tree) {
		if (tree != null) {
			tree.accept(this);
		}
	}

	public final void scanTree(final MsgflowVisitorContext context) {
		this.context = context;
		scan(context.getMsgflow());
	}

	public void visitAggregateControlNode(final AggregateControlNode aggregateControlNode) {
	}

	public void visitMQInputNode(final MQInputNode aggregateControlNode) {
	}

	public void visitMsgflow(final MsgflowImpl msgflow) {
		msgflow.getMessageFlowNodes().stream().forEach(node -> node.accept(this));

	}
}