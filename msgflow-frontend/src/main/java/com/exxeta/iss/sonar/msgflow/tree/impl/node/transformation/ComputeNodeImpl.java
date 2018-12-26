package com.exxeta.iss.sonar.msgflow.tree.impl.node.transformation;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.tree.node.transformation.ComputeNode;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public class ComputeNodeImpl extends AbstractMessageFlowNode implements ComputeNode {

	private final String dataSource;

	public ComputeNodeImpl(final Node node, final String id, final String name, final int locationX,
			final int locationY, final String dataSource) {
		super(node, id, name, locationX, locationY);
		this.dataSource = dataSource;
	}

	@Override
	public void accept(final DoubleDispatchMsgflowVisitor visitor) {
		visitor.visitComputeNode(this);
	}

	@Override
	public Kind getKind() {
		return Kind.COMPUTE;
	}

	@Override
	public String dataSource() {
		return dataSource;
	}




}
