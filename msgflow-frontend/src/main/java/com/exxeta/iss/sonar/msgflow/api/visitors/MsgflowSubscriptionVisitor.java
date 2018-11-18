package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.util.Collection;
import java.util.Set;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;
import com.exxeta.iss.sonar.msgflow.tree.impl.MessageFlowConnectionImpl;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowImpl;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;
import com.google.common.base.Preconditions;

public abstract class MsgflowSubscriptionVisitor implements MsgflowVisitor {
	private MsgflowVisitorContext context;
	private Collection<Tree.Kind> nodesToVisit;

	@Override
	public MsgflowVisitorContext getContext() {
		Preconditions.checkState(context != null,
				"this#scanTree(context) should be called to initialised the context before accessing it");
		return context;
	}

	protected boolean isSubscribed(final Tree tree) {
		return nodesToVisit.contains(((MsgflowTree) tree).getKind());
	}

	public void leaveFile(final Tree scriptTree) {
		// default behaviour is to do nothing
	}

	public void leaveNode(final Tree tree) {
		// Default behavior : do nothing.
	}

	public abstract Set<Tree.Kind> nodesToVisit();

	@Override
	public final void scanTree(final MsgflowVisitorContext context) {
		this.context = context;
		visitFile(context.getMsgflow());
		scanTree(context.getMsgflow());
		leaveFile(context.getMsgflow());
	}

	public void scanTree(final Tree tree) {
		nodesToVisit = nodesToVisit();
		visit(tree);
	}

	private void visit(final Tree tree) {
		final boolean isSubscribed = isSubscribed(tree);
		if (isSubscribed) {
			visitNode(tree);
		}
		visitChildren(tree);
		if (isSubscribed) {
			leaveNode(tree);
		}
	}

	private void visitChildren(final Tree tree) {
		if (tree instanceof MsgflowImpl) {

			final MsgflowImpl msgflow = (MsgflowImpl) tree;

			for (final AbstractMessageFlowNode node : msgflow.getMessageFlowNodes()) {
				visit(node);
			}
			for (final MessageFlowConnectionImpl connection : msgflow.getConnections()) {
				visit(connection);
			}
		}

	}

	public void visitFile(final Tree scriptTree) {
		// default behaviour is to do nothing
	}

	public void visitNode(final Tree tree) {
		// Default behavior : do nothing.
	}
}
