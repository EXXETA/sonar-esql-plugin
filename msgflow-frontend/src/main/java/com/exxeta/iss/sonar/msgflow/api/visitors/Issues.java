package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.util.ArrayList;
import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.tree.MsgflowTree;

public class Issues {
	private List<Issue> issueList;
	private final MsgflowCheck check;

	public Issues(final MsgflowCheck check) {
		this.check = check;
		issueList = new ArrayList<>();
	}

	public PreciseIssue addIssue(final MsgflowTree tree, final String message) {
		final PreciseIssue preciseIssue = new PreciseIssue(check, new IssueLocation(tree, message));
		addIssue(preciseIssue);
		return preciseIssue;
	}

	public <T extends Issue> T addIssue(final T issue) {
		issueList.add(issue);
		return issue;
	}

	public List<Issue> getList() {
		return issueList;
	}

	public void reset() {
		issueList = new ArrayList<>();
	}
}
