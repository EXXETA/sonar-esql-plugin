package com.exxeta.iss.sonar.msgflow.api.visitors;

import org.sonar.api.batch.fs.InputFile;

import com.exxeta.iss.sonar.msgflow.parser.Msgflow;

public class MsgflowVisitorContext {

	private final Msgflow msgflow;
	private final MsgflowFile msgflowFile;

	public MsgflowVisitorContext(final Msgflow msgflow, final InputFile inputFile) {
		super();
		this.msgflow = msgflow;
		msgflowFile = new MsgflowFileImpl(inputFile);
	}

	public Msgflow getMsgflow() {
		return msgflow;
	}

	public MsgflowFile getMsgflowFile() {
		return msgflowFile;
	}

}
