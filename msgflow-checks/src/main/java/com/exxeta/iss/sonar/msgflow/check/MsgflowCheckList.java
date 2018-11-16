package com.exxeta.iss.sonar.msgflow.check;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class MsgflowCheckList {
	public static final String REPOSITORY_KEY = "msgflow";

	public static List<Class> getChecks() {
		return ImmutableList.<Class>of(AggregateWithoutTimeoutCheck.class);
	}

	private MsgflowCheckList() {
	}
}
