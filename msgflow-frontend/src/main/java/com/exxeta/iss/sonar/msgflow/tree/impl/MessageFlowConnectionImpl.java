package com.exxeta.iss.sonar.msgflow.tree.impl;

import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;

/**
 * The class is a model of a message flow connection the properties of message
 * flow connections.
 *
 * @author Arjav Shah
 */
public class MessageFlowConnectionImpl extends MsgflowTree implements MessageFlowConnection {
	/**
	 * a source node ID of the connection
	 */
	private String srcNode;
	/**
	 * a target node ID of the connection
	 */
	private String targetNode;
	/**
	 * a source terminal of the connection
	 */
	private String srcTerminal;
	/**
	 * a target terminal of the connection
	 */
	private String targetTerminal;

	public MessageFlowConnectionImpl() {
		super(null);
	}

	public MessageFlowConnectionImpl(final Element element, final String srcNode, final String srcTerminal, final String targetNode, final String targetTerminal) {
		super(element);
		this.targetNode = targetNode;
		this.srcNode = srcNode;
		this.srcTerminal = srcTerminal;
		this.targetTerminal = targetTerminal;
	}

	@Override
	public void accept(final DoubleDispatchMsgflowVisitor visitor) {
		visitor.visitConnection(this);

	}

	@Override
	public Kind getKind() {
		return Kind.CONNECTION;
	}

	/**
	 * The method returns the ID of the source Node.
	 *
	 * @return a ID of the source Node.
	 */
	public String srcNode() {
		return srcNode;
	}

	/**
	 * The method returns the source terminal.
	 *
	 * @return a name of the source terminal.
	 */
	public String srcTerminal() {
		return srcTerminal;
	}

	/**
	 * The method returns the ID of the target Node.
	 *
	 * @return a ID of the target Node.
	 */
	public String targetNode() {
		return targetNode;
	}

	/**
	 * The method returns the target terminal.
	 *
	 * @return a name of the target terminal.
	 */
	public String targetTerminal() {
		return targetTerminal;
	}

}