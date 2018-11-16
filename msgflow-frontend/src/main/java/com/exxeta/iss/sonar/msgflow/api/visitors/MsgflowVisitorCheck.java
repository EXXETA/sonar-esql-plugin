package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.tree.Tree;

public abstract class MsgflowVisitorCheck extends DoubleDispatchMsgflowVisitor implements MsgflowCheck {

	private final Issues issues = new Issues(this);

	@Override
	public <T extends Issue> T addIssue(final T issue) {
		return issues.addIssue(issue);
	}

	@Override
	public PreciseIssue addIssue(final Tree tree, final String message) {
		return issues.addIssue(tree, message);
	}

	@Override
	public List<Issue> scanFile(final MsgflowVisitorContext context) {
		issues.reset();
		scanTree(context);
		return issues.getList();
	}

}
