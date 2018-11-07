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
package com.exxeta.iss.sonar.msgflow.parser;

import java.util.ArrayList;

/**
 * The class is a model of a message flow model containing a list of 
 * message flow nodes. 
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class Msgflow {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MessageFlow.class);
	
	/**
	 * a list of Collector Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> collectorNodes = new ArrayList<>();
	
	/**
	 * a list of Compute Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> computeNodes = new ArrayList<>();

	/**
	 * a list of File Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> fileInputNodes = new ArrayList<>();
	
	/**
	 * a list of File Output Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> fileOutputNodes = new ArrayList<>();
	
	/**
	 * a list of Http Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> httpInputNodes = new ArrayList<>();
	
	/**
	 * a list of Http Request Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> httpRequestNodes = new ArrayList<>();
	
	/**
	 * a list of Http Reply Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> httpReplyNodes = new ArrayList<>();
	
	/**
	 * a list of MQ Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqInputNodes = new ArrayList<>();
	
	/**
	 * a list of MQ Output Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqOutputNodes = new ArrayList<>();

	/**
	 * a list of MQ Get Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqGetNodes = new ArrayList<>();

	/**
	 * a list of MQ Header Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqHeaderNodes = new ArrayList<>();
	
	/**
	 * a list of MQ Reply Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqReplyNodes = new ArrayList<>();
	
	/**
	 * a list of Reset Content Descriptor Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> resetContentDescriptorNodes = new ArrayList<>();
	
	/**
	 * a list of Soap Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> soapInputNodes = new ArrayList<>();
	
	/**
	 * a list of Soap Request Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> soapRequestNodes = new ArrayList<>();
	
	/**
	 * a list of Timeout Control Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> timeoutControlNodes = new ArrayList<>();
	
	/**
	 * a list of Timeout Notification Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> timeoutNotificationNodes = new ArrayList<>();
	
	/**
	 * a list of Try Catch Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> tryCatchNodes = new ArrayList<>();
	
	/**
	 * a list of IMS Request Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> imsRequestNodes = new ArrayList<>();
	
	/**
	 * a list of Filter Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> filterNodes = new ArrayList<>();
	
	/**
	 * a list of trace Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> traceNodes = new ArrayList<>();
	
	/**
	 * a list of label Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> labelNodes = new ArrayList<>();
	
	/**
	 * a list of routeToLabel Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> routeToLabelNodes = new ArrayList<>();
	
	/**
	 * a list of aggregateControl Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> aggregateControlNodes = new ArrayList<>();
	
	/**
	 * a list of Database Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> databaseNodes = new ArrayList<>();
	
	/**
	 * a list of Route Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> routeNodes = new ArrayList<>();
	/**
	 * a list of miscellaneous Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> miscellaneousNodes = new ArrayList<>();
	
	/**
	 * a list of Connections of a message flow
	 */
	private ArrayList<MessageFlowConnection> connections = new ArrayList<>();
	
	/**
	 * a list of comment notes of a message flow
	 */
	private ArrayList<MessageFlowCommentNote> comments = new ArrayList<>();
	/**
	 * a short description of a message flow
	 */
	private String shortDescription;
	/**
	 * a long description of a message flow
	 */
	private String longDescription;
	

	public Msgflow() {
	}
	
	/**
	 * The method returns a list of the Collector Nodes of Message Flow.
	 * 
	 * @return a list of the Collector Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getCollectorNodes() {
		return collectorNodes;
	}

	/**
	 * The method returns a list of the Compute Nodes of Message Flow.
	 * 
	 * @return a list of the Compute Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getComputeNodes() {
		return computeNodes;
	}

	/**
	 * The method returns a list of the File Input Nodes of Message Flow.
	 * 
	 * @return a list of the File Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getFileInputNodes() {
		return fileInputNodes;
	}

	/**
	 * The method returns a list of the File Output Nodes of Message Flow.
	 * 
	 * @return a list of the File Output Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getFileOutputNodes() {
		return fileOutputNodes;
	}

	/**
	 * The method returns a list of the Http Input Nodes of Message Flow.
	 * 
	 * @return a list of the Http Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getHttpInputNodes() {
		return httpInputNodes;
	}

	/**
	 * The method returns a list of the Http Request Nodes of Message Flow.
	 * 
	 * @return a list of the Http Request Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getHttpRequestNodes() {
		return httpRequestNodes;
	}

	/**
	 * The method returns a list of the Http Reply Nodes of Message Flow.
	 * 
	 * @return the httpReplyNodes
	 */
	public ArrayList<MessageFlowNode> getHttpReplyNodes() {
		return httpReplyNodes;
	}

	/**
	 * The method returns a list of the MQ Input Nodes of Message Flow.
	 * 
	 * @return a list of the MQ Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqInputNodes() {
		return mqInputNodes;
	}

	/**
	 * The method returns a list of the MQ Output Nodes of Message Flow.
	 * 
	 * @return a list of the MQ Output Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqOutputNodes() {
		return mqOutputNodes;
	}

	/**
	 * The method returns a list of the MQGet Nodes of Message Flow.
	 * 
	 * @return a list of the MQGet Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqGetNodes() {
		return mqGetNodes;
	}

	/**
	 * The method returns a list of the MQHeader Nodes of Message Flow.
	 * 
	 * @return a list of the MQHeader Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqHeaderNodes() {
		return mqHeaderNodes;
	}

	/**
	 * The method returns a list of MQReply Nodes of Message Flow
	 * 
	 * @return the mqReplyNodes
	 */
	public ArrayList<MessageFlowNode> getMqReplyNodes() {
		return mqReplyNodes;
	}

	/**
	 * The method returns a list of the Reset Content Descriptor Nodes of Message Flow.
	 * 
	 * @return a list of the Reset Content Descriptor Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getResetContentDescriptorNodes() {
		return resetContentDescriptorNodes;
	}
	
	/**
	 * The method returns a list of the Soap Input Nodes of Message Flow.
	 * 
	 * @return a list of the Soap Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getSoapInputNodes() {
		return soapInputNodes;
	}

	/**
	 * The method returns a list of the Soap Request Nodes of Message Flow.
	 * 
	 * @return a list of the Soap Request Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getSoapRequestNodes() {
		return soapRequestNodes;
	}

	/**
	 * The method returns a list of the Timeout Control Nodes of Message Flow.
	 * 
	 * @return a list of the Timeout Control Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTimeoutControlNodes() {
		return timeoutControlNodes;
	}

	/**
	 * The method returns a list of the Timeout Notification Nodes of Message Flow.
	 * 
	 * @return a list of the Timeout Notification Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTimeoutNotificationNodes() {
		return timeoutNotificationNodes;
	}
	
	/**
	 * The method returns a list of the Try Catch Nodes of Message Flow.
	 * 
	 * @return a list of the Try Catch Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTryCatchNodes() {
		return tryCatchNodes;
	}
	
	/**
	 * The method returns a list of IMS Request nodes of Message Flow
	 * 
	 * @return a list of the IMS Request Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getImsRequestNodes() {
		return imsRequestNodes;
	}

	/**
	 * The method returns a list of IMS Request nodes of Message Flow
	 * 
	 * @return a list of the filter Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getFilterNodes() {
		return filterNodes;
	}

	/**
	 * The method returns a list of Trace nodes of Message Flow
	 * 
	 * @return a list of trace Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTraceNodes() {
		return traceNodes;
	}

	/**
	 * The method returns a list of label nodes of Message Flow
	 * 
	 * @return the labelNodes
	 */
	public ArrayList<MessageFlowNode> getLabelNodes() {
		return labelNodes;
	}

	/**
	 * The method returns a list of routeToLabel Nodes of Message Flow
	 * 
	 * @return the routeToLabelNodes
	 */
	public ArrayList<MessageFlowNode> getRouteToLabelNodes() {
		return routeToLabelNodes;
	}

	/**
	 * The method returns a list of aggregateControl nodes
	 * 
	 * @return a list of aggregateControl nodes of MessageFlow
	 */
	public ArrayList<MessageFlowNode> getAggregateControlNodes() {
		return aggregateControlNodes;
	}

	/**
	 * The method returns a list of database nodes
	 * 
	 * @return the databaseNodes
	 */
	public ArrayList<MessageFlowNode> getDatabaseNodes() {
		return databaseNodes;
	}

	/**
	 * The method returns a list of route nodes
	 * 
	 * @return the routeNodes
	 */
	public ArrayList<MessageFlowNode> getRouteNodes() {
		return routeNodes;
	}

	/**
	 * The method returns a list of miscellaneous/uncategorized nodes
	 * 
	 * @return a list of miscellaneous/uncategorized nodes of MessageFlow
	 */
	public ArrayList<MessageFlowNode> getMiscellaneousNodes() {
		return miscellaneousNodes;
	}

	/**
	 * The method returns a list of the connections of Message Flow
	 * 
	 * @return a list of connections of the message flow
	 */
	public ArrayList<MessageFlowConnection> getConnections() {
		return connections;
	}

	/**
	 * The method returns a list of comment object of Message Flow
	 * 
	 * @return a list of comment objects
	 */
	public ArrayList<MessageFlowCommentNote> getComments() {
		return comments;
	}

	/**
	 * The method returns a short description of a Message Flow
	 * 
	 * @return the short description of the message flow
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * The method returns a long description of a Message Flow
	 * 
	 * @return the long description of the message flow
	 */
	public String getLongDescription() {
		return longDescription;
	}

	public void addCollectorNode(MessageFlowNode mfn) {
		collectorNodes.add(mfn);
	}

	public void addComputeNode(MessageFlowNode mfn) {
		computeNodes.add(mfn);
	}

	public void addFileInputNode(MessageFlowNode mfn) {
		fileInputNodes.add(mfn);
	}

	public void addFileOutputNode(MessageFlowNode mfn) {
		fileOutputNodes.add(mfn);
	}

	public void addHttpInputNode(MessageFlowNode mfn) {
		httpInputNodes.add(mfn);
	}

	public void addHttpRequestNode(MessageFlowNode mfn) {
		httpRequestNodes.add(mfn);
	}

	public void addHttpReplyNode(MessageFlowNode mfn) {
		httpReplyNodes.add(mfn);
	}

	public void addMqInputNode(MessageFlowNode mfn) {
		mqInputNodes.add(mfn);
	}

	public void addMqOutputNode(MessageFlowNode mfn) {
		mqOutputNodes.add(mfn);
	}

	public void addMqGetNode(MessageFlowNode mfn) {
		mqGetNodes.add(mfn);
	}

	public void addMqHeaderNodes(MessageFlowNode mfn) {
		mqHeaderNodes.add(mfn);
		
	}

	public void addMqReplyNodes(MessageFlowNode mfn) {
		mqReplyNodes.add(mfn);
		
	}

	public void addResetContentDescriptorNodes(MessageFlowNode mfn) {
		resetContentDescriptorNodes.add(mfn);
		
	}

	public void addSoapRequestNode(MessageFlowNode mfn) {
		soapRequestNodes.add(mfn);
	}

	public void addSoapInputNode(MessageFlowNode mfn) {
		soapInputNodes.add(mfn);
	}

	public void addTimeoutControlNode(MessageFlowNode mfn) {
		timeoutControlNodes.add(mfn);
	}

	public void addTimeoutNotificationNode(MessageFlowNode mfn) {
		timeoutNotificationNodes.add(mfn);
	}

	public void addTryCatchNode(MessageFlowNode mfn) {
		tryCatchNodes.add(mfn);
	}

	public void addImsRequestNode(MessageFlowNode mfn) {
		imsRequestNodes.add(mfn);
	}

	public void addTraceNode(MessageFlowNode mfn) {
		traceNodes.add(mfn);
	}

	public void addFilterNode(MessageFlowNode mfn) {
		filterNodes.add(mfn);
	}

	public void addLabelNode(MessageFlowNode mfn) {
		labelNodes.add(mfn);
	}

	public void addRouteToLabelNode(MessageFlowNode mfn) {
		routeToLabelNodes.add(mfn);
		
	}

	public void addAggregateControlNodes(MessageFlowNode mfn) {
		aggregateControlNodes.add(mfn);
	}

	public void addDatabaseNode(MessageFlowNode mfn) {
		databaseNodes.add(mfn);
	}

	public void addRouteNode(MessageFlowNode mfn) {
		routeNodes.add(mfn);
	}

	public void addMiscellaneousNode(MessageFlowNode mfn) {
		miscellaneousNodes.add(mfn);
	}

	public void addConnection(MessageFlowConnection conection) {
		connections.add(conection);
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription=shortDescription;
		
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void addComment(MessageFlowCommentNote msgFlowComment) {
		comments.add(msgFlowComment);
	}
	
	
}