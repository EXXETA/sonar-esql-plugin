package com.exxeta.iss.sonar.msgflow.api.visitors;

import org.sonar.api.batch.fs.InputFile;

import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowImpl;

public class MsgflowVisitorContext {

	private final MsgflowImpl msgflow;
	private final MsgflowFile msgflowFile;

	public MsgflowVisitorContext(final MsgflowImpl msgflow, final InputFile inputFile) {
		super();
		this.msgflow = msgflow;
		msgflowFile = new MsgflowFileImpl(inputFile);
	}

	public MsgflowImpl getMsgflow() {
		return msgflow;
	}

	public MsgflowFile getMsgflowFile() {
		return msgflowFile;
	}

}
