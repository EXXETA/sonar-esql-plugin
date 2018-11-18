/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.msgflow.tree.impl;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.exxeta.iss.sonar.msgflow.api.tree.Messageflow;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;

public class MsgflowImpl extends MsgflowTree implements Messageflow{

	/**
	 * The logger for the class.
	 */
	private static final Logger LOGGER = Loggers.get(MsgflowImpl.class);

	private final List<AbstractMessageFlowNode> nodes = new ArrayList<>();

	private final List<MessageFlowConnectionImpl> connections = new ArrayList<>();

	private final List<MessageFlowCommentNoteImpl> comments = new ArrayList<>();

	private String shortDescription;

	private String longDescription;

	public MsgflowImpl() {
		super(null);
	}

	@Override
	public void accept(final DoubleDispatchMsgflowVisitor visitor) {
		visitor.visitMsgflow(this);

	}

	public void addComment(final MessageFlowCommentNoteImpl msgFlowComment) {
		comments.add(msgFlowComment);
	}

	public void addConnection(final MessageFlowConnectionImpl conection) {
		connections.add(conection);
	}

	public void addNode(final AbstractMessageFlowNode mfn) {
		nodes.add(mfn);
	}

	/**
	 * The method returns a list of comment object of Message Flow
	 *
	 * @return a list of comment objects
	 */
	public List<MessageFlowCommentNoteImpl> getComments() {
		return comments;
	}

	/**
	 * The method returns a list of the connections of Message Flow
	 *
	 * @return a list of connections of the message flow
	 */
	public List<MessageFlowConnectionImpl> getConnections() {
		return connections;
	}

	@Override
	public Kind getKind() {
		return Kind.MESSAGEFLOW;
	}

	/**
	 * The method returns a long description of a Message Flow
	 *
	 * @return the long description of the message flow
	 */
	public String getLongDescription() {
		return longDescription;
	}

	public List<AbstractMessageFlowNode> getMessageFlowNodes() {
		return nodes;
	}

	/**
	 * The method returns a short description of a Message Flow
	 *
	 * @return the short description of the message flow
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	public void setLongDescription(final String longDescription) {
		this.longDescription = longDescription;
	}

	public void setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;

	}

}