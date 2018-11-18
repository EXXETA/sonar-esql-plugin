package com.exxeta.iss.sonar.msgflow.api.tree;

import java.util.List;

public interface MessageFlowCommentNote extends Located {
	String comment();

	List<String> associations();

}
