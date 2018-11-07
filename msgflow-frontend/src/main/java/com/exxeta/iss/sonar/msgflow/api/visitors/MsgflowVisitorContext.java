package com.exxeta.iss.sonar.msgflow.api.visitors;

import com.exxeta.iss.sonar.msgflow.parser.Msgflow;

public class MsgflowVisitorContext {

	private final Msgflow msgflow;
	private final MsgflowFile msgflowFile;

	public MsgflowVisitorContext(Msgflow msgflow, MsgflowFile msgflowFile) {
		super();
		this.msgflow = msgflow;
		this.msgflowFile = msgflowFile;
	}

}
