package com.exxeta.iss.sonar.msgflow.api.tree;

import java.util.List;

public interface MessageFlowCommentNote extends Located, Tree {
	String comment();

	List<String> associations();

}
