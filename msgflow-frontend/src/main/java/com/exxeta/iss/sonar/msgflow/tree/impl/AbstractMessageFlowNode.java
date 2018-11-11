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
	 * the flag for 'build Tree Using Schema' of a message flow node
	 */
	private boolean buildTreeUsingSchema;

	/**
	 * the flag for 'mixed Content Retain Mode' of a message flow node
	 */
	private boolean mixedContentRetainMode;

	/**
	 * the flag for 'comments Retain Mode' of a message flow node
	 */
	private boolean commentsRetainMode;

	/**
	 * the flag for 'validate Master' of a message flow node
	 */
	private boolean validateMaster;

	/**
	 * the message domain of a message flow node
	 */
	private String messageDomainProperty;

	/**
	 * the message set of a message flow node
	 */
	private String messageSetProperty;

	/**
	 * the record definition of a message flow node
	 *
	 * e.g. for File Output Node "Record is Whole File", "Record is Unmodified
	 * Data", "Record is Fixed Length Data", "Record is Delimited Data"
	 */
	private String recordDefinition;

	/**
	 * the request message location of a message flow node
	 */
	private String requestMsgLocationInTree;

	/**
	 * the message domain of a message flow node
	 */
	private String messageDomain;

	/**
	 * the message set of a message flow node
	 */
	private String messageSet;

	/**
	 * the flag for 'reset Message Domain' of a message flow node
	 */
	private boolean resetMessageDomain;

	/**
	 * the flag for 'reset Message Set' of a message flow node
	 */
	private boolean resetMessageSet;

	/**
	 * the flag for 'reset Message Type' of a message flow node
	 */
	private boolean resetMessageType;

	/**
	 * the flag for 'reset Message Format' of a message flow node
	 */
	private boolean resetMessageFormat;

	/**
	 * the flag for "Monitoring / Monitoring events"
	 */
	private boolean areMonitoringEventsEnabled;

	/**
	 * the list of input terminals of a message flow node
	 */
	private List<Terminal> inputTerminals;

	/**
	 * the list of output terminals of a message flow node
	 */
	private List<Terminal> outputTerminals;

	/**
	 * the list of custom properties of a message flow node
	 */
	private Map<String, Object> properties;

	private List<Terminal> sourceTerminals;

	private List<Terminal> targetTerminals;

	private int locationX;

	private int locationY;

	public AbstractMessageFlowNode(final Node node, final String id, final String name, final int locationX,
			final int locationY) {
		super(node);
		this.id = id;
		this.name = name;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	/**
	 * Constructor
	 *
	 * Creates a new message flow node (model) and initializes its properties.
	 */
	public AbstractMessageFlowNode(final Node node, final String id, final String name, final String type,
			final boolean buildTreeUsingSchema, final boolean mixedContentRetainMode, final boolean commentsRetainMode,
			final boolean validateMaster, final String messageDomainProperty, final String messageSetProperty,
			final String requestMsgLocationInTree, final String messageDomain, final String messageSet,
			final String recordDefinition, final boolean resetMessageDomain, final boolean resetMessageSet,
			final boolean resetMessageType, final boolean resetMessageFormat, final boolean areMonitoringEventsEnabled,
			final List<Terminal> inputTerminals, final List<Terminal> outputTerminals,
			final Map<String, Object> properties) {
		super(node);
		this.id = id;
		this.name = name;
		this.buildTreeUsingSchema = buildTreeUsingSchema;
		this.mixedContentRetainMode = mixedContentRetainMode;
		this.commentsRetainMode = commentsRetainMode;
		this.validateMaster = validateMaster;
		this.messageDomainProperty = messageDomainProperty;
		this.messageSetProperty = messageSetProperty;
		this.requestMsgLocationInTree = requestMsgLocationInTree;
		this.messageDomain = messageDomain;
		this.messageSet = messageSet;
		this.recordDefinition = recordDefinition;
		this.resetMessageDomain = resetMessageDomain;
		this.resetMessageSet = resetMessageSet;
		this.resetMessageType = resetMessageType;
		this.resetMessageFormat = resetMessageFormat;
		this.areMonitoringEventsEnabled = areMonitoringEventsEnabled;
		this.inputTerminals = inputTerminals;
		this.outputTerminals = outputTerminals;
		this.properties = properties;
	}

	/**
	 * The method returns the flag for 'areMonitoringEventsEnabled' of the message
	 * flow node.
	 *
	 * @return the flag for 'areMonitoringEventsEnabled' of the message flow node
	 */
	public boolean areMonitoringEventsEnabled() {
		return areMonitoringEventsEnabled;
	}

	/**
	 * The method returns a the ID of a message flow node.
	 *
	 * @return the ID of a message flow node
	 */
	public String id() {
		return id;
	}

	/**
	 * The method returns a the list of input terminals of a message flow node.
	 *
	 * @return the list of input terminals of a message flow node
	 */
	public List<Terminal> getInputTerminals() {
		return inputTerminals;
	}

	/**
	 * The method returns a message domain of a message flow node.
	 *
	 * @return message domain of a message flow node
	 */
	public String getMessageDomain() {
		return messageDomain;
	}

	/**
	 * The method returns a the message domain of a message flow node.
	 *
	 * @return the message domain of a message flow node
	 */
	public String getMessageDomainProperty() {
		return messageDomainProperty;
	}

	/**
	 * The method returns a the message set of a message flow node.
	 *
	 * @return the message set of a message flow node
	 */
	public String getMessageSet() {
		return messageSet;
	}

	/**
	 * The method returns a the message set of a message flow node.
	 *
	 * @return the message set of a message flow node
	 */
	public String getMessageSetProperty() {
		return messageSetProperty;
	}

	/**
	 * The method returns a the name of a message flow node.
	 *
	 * @return the name of a message flow node
	 */
	public String name() {
		return name;
	}

	/**
	 * The method returns a the list of output terminals of a message flow node.
	 *
	 * @return the list of output terminals of a message flow node
	 */
	public List<Terminal> getOutputTerminals() {
		return outputTerminals;
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
	 * The method returns the record definition of a message flow node.
	 *
	 * @return the recordDefinition
	 */
	public String getRecordDefinition() {
		return recordDefinition;
	}

	/**
	 * The method returns a the request message location of a message flow node.
	 *
	 * @return the request message location of a message flow node
	 */
	public String getRequestMsgLocationInTree() {
		return requestMsgLocationInTree;
	}

	/**
	 * The method returns a the flag for 'build Tree Using Schema' of a message flow
	 * node.
	 *
	 * @return the flag for 'build Tree Using Schema' of a message flow node
	 */
	public boolean isBuildTreeUsingSchema() {
		return buildTreeUsingSchema;
	}

	public boolean isCommentsRetainMode() {
		return commentsRetainMode;
	}

	public boolean isMixedContentRetainMode() {
		return mixedContentRetainMode;
	}

	public boolean isResetMessageDomain() {
		return resetMessageDomain;
	}

	public boolean isResetMessageFormat() {
		return resetMessageFormat;
	}

	public boolean isResetMessageSet() {
		return resetMessageSet;
	}

	public boolean isResetMessageType() {
		return resetMessageType;
	}

	public boolean isValidateMaster() {
		return validateMaster;
	}

	@Override
	public int locationX() {
		return locationX;
	}

	@Override
	public int locationY() {
		return locationY;
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