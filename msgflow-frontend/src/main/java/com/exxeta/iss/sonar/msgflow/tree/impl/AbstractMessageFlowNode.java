/*
 * Sonar Message Flow Plugin
 * Copyright (C) 2015 Hendrik Scholz and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.msgflow.tree.impl;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.api.tree.Terminal;

/**
 * The class is a model of a message flow node. The model contains variables
 * holding connection data as well as configuration data of a message flow node.
 *
 */
public abstract class AbstractMessageFlowNode extends MsgflowTree implements MessageFlowNode {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(MessageFlowNode.class);

	private final String id;

	private final String name;

	/**
	 * the list of custom properties of a message flow node
	 */
	private Map<String, Object> properties;

	private List<Terminal> sourceTerminals;

	private List<Terminal> targetTerminals;

	private final int locationX;

	private final int locationY;

	public AbstractMessageFlowNode(final Node node, final String id, final String name, final int locationX,
			final int locationY) {
		super(node);
		this.id = id;
		this.name = name;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	/**
	 * This method returns the list of Custom Properties for the node
	 *
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * The method returns a the ID of a message flow node.
	 *
	 * @return the ID of a message flow node
	 */
	@Override
	public String id() {
		return id;
	}

	@Override
	public int locationX() {
		return locationX;
	}

	@Override
	public int locationY() {
		return locationY;
	}

	/**
	 * The method returns a the name of a message flow node.
	 *
	 * @return the name of a message flow node
	 */
	@Override
	public String name() {
		return name;
	}

	@Override
	public List<Terminal> sourceTerminals() {
		return sourceTerminals;
	}

	@Override
	public List<Terminal> targetTerminals() {
		return targetTerminals;
	}

}