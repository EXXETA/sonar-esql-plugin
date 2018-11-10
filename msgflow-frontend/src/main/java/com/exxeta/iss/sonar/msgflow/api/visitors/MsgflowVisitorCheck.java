package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.tree.MsgflowTree;

public abstract class MsgflowVisitorCheck extends MsgflowVisitor implements MsgflowCheck {

	private final Issues issues = new Issues(this);

	@Override
	public PreciseIssue addIssue(final MsgflowTree tree, final String message) {
		return issues.addIssue(tree, message);
	}

	@Override
	public <T extends Issue> T addIssue(final T issue) {
		return issues.addIssue(issue);
	}

	@Override
	public List<Issue> scanFile(final MsgflowVisitorContext context) {
		issues.reset();
		scanTree(context);
		return issues.getList();
	}

}
