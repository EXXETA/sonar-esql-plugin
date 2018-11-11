package com.exxeta.iss.sonar.msgflow.api.tree;

import java.util.List;

public interface MessageFlowNode extends Located, Tree {

	String id();

	String name();

	List<Terminal> sourceTerminals();

	List<Terminal> targetTerminals();

}
