package com.exxeta.iss.sonar.msgflow.api.visitors;

import org.sonar.api.batch.fs.InputFile;

import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;

public class MsgflowVisitorContext {

	private final MsgflowTree msgflow;
	private final MsgflowFile msgflowFile;

	public MsgflowVisitorContext(final MsgflowTree msgflow, final InputFile inputFile) {
		super();
		this.msgflow = msgflow;
		msgflowFile = new MsgflowFileImpl(inputFile);
	}

	public MsgflowTree getMsgflow() {
		return msgflow;
	}

	public MsgflowFile getMsgflowFile() {
		return msgflowFile;
	}

}
