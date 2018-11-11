package com.exxeta.iss.sonar.msgflow.api.visitors;

import java.util.ArrayList;
import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.tree.Tree;

public class Issues {
	private List<Issue> issueList;
	private final MsgflowCheck check;

	public Issues(final MsgflowCheck check) {
		this.check = check;
		issueList = new ArrayList<>();
	}

	public <T extends Issue> T addIssue(final T issue) {
		issueList.add(issue);
		return issue;
	}

	public PreciseIssue addIssue(final Tree tree, final String message) {
		final PreciseIssue preciseIssue = new PreciseIssue(check, new IssueLocation(tree, message));
		addIssue(preciseIssue);
		return preciseIssue;
	}

	public List<Issue> getList() {
		return issueList;
	}

	public void reset() {
		issueList = new ArrayList<>();
	}
}
