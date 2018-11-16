package com.exxeta.iss.sonar.msgflow.api.visitors;

public interface MsgflowVisitor {
	MsgflowVisitorContext getContext();

	void scanTree(MsgflowVisitorContext context);
}
