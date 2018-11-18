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
package com.exxeta.iss.sonar.msgflow.parser;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.createDocument;
import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.parseNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.exxeta.iss.sonar.msgflow.tree.impl.MessageFlowCommentNoteImpl;
import com.exxeta.iss.sonar.msgflow.tree.impl.MessageFlowConnectionImpl;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowImpl;
import com.sonar.sslr.api.RecognitionException;

public final class MsgflowParser {

	private static final Logger LOGGER = Loggers.get(MsgflowParser.class);

	public MsgflowParser() {

	}

	private MsgflowImpl parse(final Document document) {
		final MsgflowImpl msgflow = new MsgflowImpl();
		try {
			final XPathExpression nodesXPath = XPathFactory.newInstance().newXPath().compile("//nodes");
			final NodeList nodes = (NodeList) nodesXPath.evaluate(document, XPathConstants.NODESET);
			// numberOfNodes =
			// XPathFactory.newInstance().newXPath().compile("count(//nodes)");

			// int non = 0;//Integer.parseInt((String) numberOfNodes.evaluate(document,
			// XPathConstants.STRING));

			for (int i = 0; i < nodes.getLength(); i++) {
				final Element element = (Element) nodes.item(i);

				LOGGER.debug("Prepare expressions - START");

				/*
				 * XPathExpression buildTreeUsingSchemaExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@parserXmlnscBuildTreeUsingXMLSchema"); XPathExpression
				 * mixedContentRetainModeExpr = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[" + non + "]/@parserXmlnscMixedContentRetainMode");
				 * XPathExpression commentsRetainModeExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@parserXmlnscCommentsRetainMode"); XPathExpression validateMasterExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@validateMaster"); XPathExpression messageDomainPropertyExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@messageDomainProperty"); XPathExpression messageSetPropertyExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@messageSetProperty"); XPathExpression requestMsgLocationInTreeExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@requestMsgLocationInTree");
				 *
				 * XPathExpression messageDomainExpr = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[" + non + "]/@messageDomain"); XPathExpression
				 * messageSetExpr = XPathFactory.newInstance().newXPath() .compile("//nodes[" +
				 * non + "]/@messageSet"); XPathExpression recordDefinitionExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@recordDefinition"); XPathExpression resetMessageDomainExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@resetMessageDomain"); XPathExpression resetMessageSetExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@resetMessageSet"); XPathExpression resetMessageTypeExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@resetMessageType"); XPathExpression resetMessageFormatExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/@resetMessageFormat"); XPathExpression monitoringEventsExpr =
				 * XPathFactory.newInstance().newXPath() .compile("count(//nodes[" + non +
				 * "]/monitorEvents)"); XPathExpression monitoringEventsEventEnabledExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[" + non +
				 * "]/monitorEvents/@eventEnabled");
				 */
				LOGGER.debug("Prepare expressions - END");
				LOGGER.debug("Evaluate expressions - START");

//				String id = (String) idExpr.evaluate(document, XPathConstants.STRING);
//				String name = (String) nameExpr.evaluate(document, XPathConstants.STRING);

//				LOGGER.debug("id: " + id);
//				LOGGER.debug("name: " + name);
//				LOGGER.debug("type: " + type);
//commenting the below code to add subflow nodes as a part of Miscellaneous nodelist
//			if (type.contains("ComIbm") == false) {
//				/* if the node is not a ComIbm node */
//				LOGGER.debug("omitted node of type " + type);
//				continue;
//			}
				/*
				 * String messageDomainProperty = (String)
				 * messageDomainPropertyExpr.evaluate(document, XPathConstants.STRING); String
				 * messageSetProperty = (String) messageSetPropertyExpr.evaluate(document,
				 * XPathConstants.STRING); String requestMsgLocationInTree = (String)
				 * requestMsgLocationInTreeExpr.evaluate(document, XPathConstants.STRING);
				 * String messageDomain = (String) messageDomainExpr.evaluate(document,
				 * XPathConstants.STRING); String messageSet = (String)
				 * messageSetExpr.evaluate(document, XPathConstants.STRING); String
				 * recordDefinition = (String) recordDefinitionExpr.evaluate(document,
				 * XPathConstants.STRING); // added condition to store subflow types with the
				 * extention if (type.contains("ComIbm")) { type = type.substring(0,
				 * type.indexOf(".")).replace("ComIbm", ""); } else { type = type.substring(0,
				 * type.indexOf(":")); } boolean buildTreeUsingSchema = Boolean
				 * .parseBoolean((String) buildTreeUsingSchemaExpr.evaluate(document,
				 * XPathConstants.STRING)); boolean mixedContentRetainMode = ((String)
				 * mixedContentRetainModeExpr.evaluate(document,
				 * XPathConstants.STRING)).equals("all"); boolean commentsRetainMode = ((String)
				 * commentsRetainModeExpr.evaluate(document, XPathConstants.STRING))
				 * .equals("all"); boolean validateMaster = ((String)
				 * validateMasterExpr.evaluate(document, XPathConstants.STRING))
				 * .equals("contentAndValue"); boolean resetMessageDomain = Boolean
				 * .parseBoolean((String) resetMessageDomainExpr.evaluate(document,
				 * XPathConstants.STRING)); boolean resetMessageSet = Boolean
				 * .parseBoolean((String) resetMessageSetExpr.evaluate(document,
				 * XPathConstants.STRING)); boolean resetMessageType = Boolean
				 * .parseBoolean((String) resetMessageTypeExpr.evaluate(document,
				 * XPathConstants.STRING)); boolean resetMessageFormat = Boolean
				 * .parseBoolean((String) resetMessageFormatExpr.evaluate(document,
				 * XPathConstants.STRING));
				 *
				 * int monitoringEvents = Integer .parseInt((String)
				 * monitoringEventsExpr.evaluate(document, XPathConstants.STRING)); String
				 * monitoringEventsEventEnabled = (String)
				 * monitoringEventsEventEnabledExpr.evaluate(document, XPathConstants.STRING);
				 * boolean areMonitoringEventsEnabled = true;
				 */
				/*
				 * monitoring events are enabled unless defined otherwise
				 *
				 * - monitoring events are missing - existing monitoring events are disabled
				 */
				/*
				 * if (monitoringEvents == 0 || monitoringEventsEventEnabled.equals("false")) {
				 * areMonitoringEventsEnabled = false; }
				 *
				 * XPathExpression numberOfInputTerminals =
				 * XPathFactory.newInstance().newXPath()
				 * .compile("count(//connections[@targetNode='" + id + "'])"); int noit =
				 * Integer.parseInt((String) numberOfInputTerminals.evaluate(document,
				 * XPathConstants.STRING));
				 *
				 * XPathExpression numberOfOutputTerminals =
				 * XPathFactory.newInstance().newXPath()
				 * .compile("count(//connections[@sourceNode='" + id + "'])"); int noot =
				 * Integer.parseInt((String) numberOfOutputTerminals.evaluate(document,
				 * XPathConstants.STRING));
				 *
				 * ArrayList<String> inputTerminals = new ArrayList<String>(); ArrayList<String>
				 * outputTerminals = new ArrayList<String>();
				 *
				 * for (; noit > 0; noit--) { XPathExpression inputTerminalExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//connections[@targetNode='"
				 * + id + "'][" + noit + "]/@targetTerminalName"); inputTerminals.add(((String)
				 * inputTerminalExpr.evaluate(document, XPathConstants.STRING))); }
				 *
				 * for (; noot > 0; noot--) { XPathExpression outputTerminalExpr =
				 * XPathFactory.newInstance().newXPath() .compile("//connections[@sourceNode='"
				 * + id + "'][" + noot + "]/@sourceTerminalName"); outputTerminals.add(((String)
				 * outputTerminalExpr.evaluate(document, XPathConstants.STRING))); }
				 */
				/**
				 * Added to extract the values of the node specific properties and the values
				 */
				/*
				 * Map<String, Object> properties = new HashMap<>(); if (type.equals("MQInput")
				 * || type.equals("MQOutput") || type.equals("MQGet") || type.equals("MQReply"))
				 * { if (type.equals("MQInput") || type.equals("MQOutput") ||
				 * type.equals("MQGet")) { XPathExpression queueNameExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@queueName"); String queueName = (String) queueNameExp.evaluate(document,
				 * XPathConstants.STRING);
				 *
				 * properties.put("queueName", queueName); }
				 *
				 * XPathExpression txnModeExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@transactionMode"); String txnMode =
				 * (String) txnModeExp.evaluate(document, XPathConstants.STRING);
				 *
				 * properties.put("transactionMode", txnMode); } else if
				 * (type.equals("IMSRequest")) { XPathExpression shortDescriptionIMSExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/shortDescription/@string"); String shortDescriptionIMS = (String)
				 * shortDescriptionIMSExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("shortDescription", shortDescriptionIMS);
				 *
				 * XPathExpression longDescriptionIMSExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/longDescription/@string"); String
				 * longDescriptionIMS = (String) longDescriptionIMSExp.evaluate(document,
				 * XPathConstants.STRING); properties.put("longDescription",
				 * longDescriptionIMS);
				 *
				 * XPathExpression useNodePropertiesExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@useNodeProperties"); String
				 * useNodeProperties = (String) useNodePropertiesExp.evaluate(document,
				 * XPathConstants.STRING); properties.put("useNodeProperties",
				 * useNodeProperties);
				 *
				 * XPathExpression configurableServiceExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@configurableService"); String configurableService = (String)
				 * configurableServiceExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("configurableService", configurableService);
				 *
				 * XPathExpression commitModeExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@commitMode"); String commitMode =
				 * (String) commitModeExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("commitMode", commitMode);
				 *
				 * } else if (type.equals("WSReply")) { XPathExpression
				 * ignoreTransportFailuresExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@ignoreTransportFailures"); String
				 * ignoreTransportFailures = (String)
				 * ignoreTransportFailuresExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("ignoreTransportFailures", ignoreTransportFailures);
				 * XPathExpression generateDefaultHttpHeadersExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@generateDefaultHttpHeaders"); String generateDefaultHttpHeaders =
				 * (String) generateDefaultHttpHeadersExp.evaluate(document,
				 * XPathConstants.STRING); properties.put("generateDefaultHttpHeaders",
				 * generateDefaultHttpHeaders); } else if (type.equals("SOAPRequest")) {
				 * XPathExpression requestTimeoutExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@requestTimeout"); String requestTimeout
				 * = (String) requestTimeoutExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("requestTimeout", requestTimeout); } else if
				 * (type.equals("AggregateControl")) { XPathExpression timeoutIntervalExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@timeoutInterval"); String timeoutInterval = (String)
				 * timeoutIntervalExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("timeoutInterval", timeoutInterval); } else if
				 * (type.equals("Compute")) { XPathExpression computeExpressionExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@computeExpression"); String computeExpression = (String)
				 * computeExpressionExp.evaluate(document, XPathConstants.STRING);
				 * computeExpression =
				 * computeExpression.substring(computeExpression.indexOf("#") + 1,
				 * computeExpression.indexOf(".Main")); properties.put("computeExpression",
				 * computeExpression);
				 *
				 * String computeExpressionFull = (String)
				 * computeExpressionExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("computeExpressionFull", computeExpressionFull);
				 *
				 * XPathExpression dataSourceExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@dataSource"); String dataSource =
				 * (String) dataSourceExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("dataSource", dataSource);
				 *
				 * } else if (type.equals("Filter")) { XPathExpression filterExpressionExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@filterExpression"); String filterExpression = (String)
				 * filterExpressionExp.evaluate(document, XPathConstants.STRING);
				 * filterExpression = filterExpression.substring(filterExpression.indexOf("#") +
				 * 1, filterExpression.indexOf(".Main")); properties.put("filterExpression",
				 * filterExpression); } else if (type.equals("Database")) { XPathExpression
				 * statementExp = XPathFactory.newInstance().newXPath() .compile("//nodes[@id='"
				 * + id + "']/@statement"); String statement = (String)
				 * statementExp.evaluate(document, XPathConstants.STRING); statement =
				 * statement.substring(statement.indexOf("#") + 1, statement.indexOf(".Main"));
				 * properties.put("statement", statement); } else if (type.equals("Route")) {
				 * XPathExpression filterTableCountExp = XPathFactory.newInstance().newXPath()
				 * .compile("count(//nodes[@id='" + id + "']/filterTable)"); int nof =
				 * Integer.parseInt((String) filterTableCountExp.evaluate(document,
				 * XPathConstants.STRING)); ArrayList<String> routeTable = new
				 * ArrayList<String>(); // System.out.println(noFil); for (; nof > 0; nof--) {
				 * XPathExpression outTerminalExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/filterTable[" + nof +
				 * "]/@routingOutputTerminal"); String outTerminal = (String)
				 * outTerminalExp.evaluate(document, XPathConstants.STRING);
				 * routeTable.add(outTerminal); } properties.put("routeTerminals", routeTable);
				 * }
				 *
				 * if (type.equals("MQInput") || type.equals("FileInput") ||
				 * type.equals("WSInput") || type.equals("SOAPInput")) { XPathExpression
				 * componentLevelExp = XPathFactory.newInstance().newXPath()
				 * .compile("//nodes[@id='" + id + "']/@componentLevel"); String componentLevel
				 * = (String) componentLevelExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("componentLevel", componentLevel);
				 *
				 * XPathExpression additionalInstancesExp =
				 * XPathFactory.newInstance().newXPath() .compile("//nodes[@id='" + id +
				 * "']/@additionalInstances"); String additionalInstances = (String)
				 * additionalInstancesExp.evaluate(document, XPathConstants.STRING);
				 * properties.put("additionalInstances", additionalInstances); }
				 * LOGGER.debug("Evaluate expressions - END");
				 * LOGGER.debug("Fill nodes - START");
				 */

//				/* create new MessageFlowNode using values extracted from msgflow file */
//				MessageFlowNode mfn = new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode,
//						commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty,
//						requestMsgLocationInTree, messageDomain, messageSet, recordDefinition, resetMessageDomain,
//						resetMessageSet, resetMessageType, resetMessageFormat, areMonitoringEventsEnabled,
//						inputTerminals, outputTerminals, properties);
//
//				if (type.equals("Collector")) {
//					/* Collector */
//					LOGGER.debug("Collector");
//
//					msgflow.addCollectorNode(mfn);
//				} else if (type.equals("Compute")) {
//					/* Compute */
//					LOGGER.debug("Compute");
//
//					msgflow.addComputeNode(mfn);
//				} else if (type.equals("FileInput")) {
//					LOGGER.debug("FileInput");
//
//					/* FileInput */
//					msgflow.addFileInputNode(mfn);
//				} else if (type.equals("FileOutput")) {
//					LOGGER.debug("FileOutput");
//
//					/* FileOutput */
//					msgflow.addFileOutputNode(mfn);
//				} else if (type.equals("WSInput")) {
//					LOGGER.debug("WSInput");
//
//					/* HTTPInput */
//					msgflow.addHttpInputNode(mfn);
//				} else if (type.equals("WSRequest")) {
//					LOGGER.debug("WSRequest");
//
//					/* HTTPRequest */
//					msgflow.addHttpRequestNode(mfn);
//				} else if (type.equals("WSReply")) {
//					LOGGER.debug("WSReply");
//
//					/* HTTPReply */
//					msgflow.addHttpReplyNode(mfn);
//				} else if (type.equals("MQInput")) {
//					LOGGER.debug("MQInput");
//
//					/* MQInput */
//					msgflow.addMqInputNode(mfn);
//				} else if (type.equals("MQOutput")) {
//					LOGGER.debug("MQOutput");
//
//					/* MQOutput */
//					msgflow.addMqOutputNode(mfn);
//				} else if (type.equals("MQGet")) {
//					LOGGER.debug("MQGet");
//
//					/* MQGet */
//					msgflow.addMqGetNode(mfn);
//				} else if (type.equals("MQHeader")) {
//					LOGGER.debug("MQHeader");
//
//					/* MQHeader */
//					msgflow.addMqHeaderNodes(mfn);
//				} else if (type.equals("MQReply")) {
//					LOGGER.debug("MQReply");
//
//					/* MQReply */
//					msgflow.addMqReplyNodes(mfn);
//				} else if (type.equals("ResetContentDescriptor")) {
//					LOGGER.debug("ResetContentDescriptor");
//
//					/* ResetContentDescriptor */
//					msgflow.addResetContentDescriptorNodes(mfn);
//				} else if (type.equals("SOAPInput")) {
//					LOGGER.debug("SOAPInput");
//
//					/* SOAPInput */
//					msgflow.addSoapInputNode(mfn);
//				} else if (type.equals("SOAPRequest")) {
//					LOGGER.debug("SOAPRequest");
//
//					/* SOAPRequest */
//					msgflow.addSoapRequestNode(mfn);
//				} else if (type.equals("TimeoutControl")) {
//					LOGGER.debug("TimeoutControl");
//
//					/* TimeoutControl */
//					msgflow.addTimeoutControlNode(mfn);
//				} else if (type.equals("TimeoutNotification")) {
//					LOGGER.debug("TimeoutNotification");
//
//					/* TimeoutNotification */
//					msgflow.addTimeoutNotificationNode(mfn);
//				} else if (type.equals("TryCatch")) {
//					LOGGER.debug("TryCatch");
//
//					/* TryCatch */
//					msgflow.addTryCatchNode(mfn);
//				} else if (type.equals("IMSRequest")) {
//					LOGGER.debug("IMSRequest");
//
//					/* IMS Request */
//					msgflow.addImsRequestNode(mfn);
//				} else if (type.equals("Filter")) {
//					LOGGER.debug("Filter");
//
//					/* Filter */
//					msgflow.addFilterNode(mfn);
//				} else if (type.equals("Trace")) {
//					LOGGER.debug("Trace");
//
//					/* Trace */
//					msgflow.addTraceNode(mfn);
//				} else if (type.equals("Label")) {
//					LOGGER.debug("Label");
//
//					/* Label */
//					msgflow.addLabelNode(mfn);
//				} else if (type.equals("RouteToLabel")) {
//					LOGGER.debug("RouteToLabel");
//
//					/* RouteToLabel */
//					msgflow.addRouteToLabelNode(mfn);
//				} else if (type.equals("AggregateControl")) {
//					LOGGER.debug("AggregateControl");
//
//					/* AggregateControl */
//					msgflow.addAggregateControlNodes(mfn);
//				} else if (type.equals("Database")) {
//					LOGGER.debug("Database");
//
//					/* Database */
//					msgflow.addDatabaseNode(mfn);
//				} else if (type.equals("Route")) {
//					LOGGER.debug("Route");
//
//					/* routeNodes */
//					msgflow.addRouteNode(mfn);
//				} else {
//					LOGGER.debug("Miscellaneous");
//
//					/* Miscellaneous */
//					msgflow.addMiscellaneousNode(mfn);
//				}

				msgflow.addNode(parseNode(element));

				LOGGER.debug("Fill nodes - END");
				/**
				 * Added the below snippet to get short and long description of the message flow
				 */
				final XPathExpression shortDescriptionExp = XPathFactory.newInstance().newXPath()
						.compile("//eClassifiers/shortDescription/@string");
				final XPathExpression longDescriptionExp = XPathFactory.newInstance().newXPath()
						.compile("//eClassifiers/longDescription/@string");
				msgflow.setShortDescription((String) shortDescriptionExp.evaluate(document, XPathConstants.STRING));
				msgflow.setLongDescription((String) longDescriptionExp.evaluate(document, XPathConstants.STRING));

				/**
				 * Added to identify all the connections for the message flow change starts
				 */
				final XPathExpression numberOfConnections = XPathFactory.newInstance().newXPath()
						.compile("count(//connections)");
				int noc = Integer.parseInt((String) numberOfConnections.evaluate(document, XPathConstants.STRING));

				for (; noc > 0; noc--) {

					final XPathExpression srcNodeExp = XPathFactory.newInstance().newXPath()
							.compile("//connections[" + noc + "]/@sourceNode");
					final XPathExpression targetNodeExp = XPathFactory.newInstance().newXPath()
							.compile("//connections[" + noc + "]/@targetNode");
					final XPathExpression srcTeminalExp = XPathFactory.newInstance().newXPath()
							.compile("//connections[" + noc + "]/@sourceTerminalName");
					final XPathExpression targetTerminalExp = XPathFactory.newInstance().newXPath()
							.compile("//connections[" + noc + "]/@targetTerminalName");

					final String srcNode = (String) srcNodeExp.evaluate(document, XPathConstants.STRING);
					final String targetNode = (String) targetNodeExp.evaluate(document, XPathConstants.STRING);
					final String srcTerminal = (String) srcTeminalExp.evaluate(document, XPathConstants.STRING);
					final String targetTerminal = (String) targetTerminalExp.evaluate(document, XPathConstants.STRING);

					final XPathExpression srcNodeNameExp = XPathFactory.newInstance().newXPath()
							.compile("//nodes[@id='" + srcNode + "']/translation/@string");
					final XPathExpression targetNodeNameExp = XPathFactory.newInstance().newXPath()
							.compile("//nodes[@id='" + targetNode + "']/translation/@string");

					final String srcNodeName = (String) srcNodeNameExp.evaluate(document, XPathConstants.STRING);
					final String targetNodeName = (String) targetNodeNameExp.evaluate(document, XPathConstants.STRING);

					final MessageFlowConnectionImpl conection = new MessageFlowConnectionImpl(null, srcNodeName,
							targetNode, targetNodeName, srcTerminal, targetTerminal);
					msgflow.addConnection(conection);
				}

				/**
				 * Added to identify the comment notes and the contents of it for the message
				 * flow
				 */
				final XPathExpression numberOfStickyNotes = XPathFactory.newInstance().newXPath()
						.compile("count(//stickyNote)");
				int nos = Integer.parseInt((String) numberOfStickyNotes.evaluate(document, XPathConstants.STRING));

				for (; nos > 0; nos--) {
					final XPathExpression associationExp = XPathFactory.newInstance().newXPath()
							.compile("//stickyNote[" + nos + "]/@association");
					final XPathExpression commentExp = XPathFactory.newInstance().newXPath()
							.compile("//stickyNote[" + nos + "]/body/@string");
					final XPathExpression locationExp = XPathFactory.newInstance().newXPath()
							.compile("//stickyNote[" + nos + "]/@location");
					final String associationList = (String) associationExp.evaluate(document, XPathConstants.STRING);
					final ArrayList<String> association = new ArrayList<>();
					for (final String nodeId : associationList.split(" ")) {
						association.add(nodeId);
					}
					final String comment = (String) commentExp.evaluate(document, XPathConstants.STRING);
					final int locationX = Integer
							.parseInt(((String) locationExp.evaluate(document, XPathConstants.STRING)).split(",")[0]);
					final int locationY = Integer
							.parseInt(((String) locationExp.evaluate(document, XPathConstants.STRING)).split(",")[1]);
					final MessageFlowCommentNoteImpl msgFlowComment = new MessageFlowCommentNoteImpl(association,
							comment, locationX, locationY);
					msgflow.addComment(msgFlowComment);
				}

				/**
				 * Changes ends
				 */

				LOGGER.debug("END");
			}
		} catch (final Exception e) {
			LOGGER.error("Cannot parse msgflow", e);
		}
		return msgflow;
	}

	public MsgflowImpl parse(final File file) {
		try {
			return parse(createDocument(file));
		} catch (IOException | SAXException | ParserConfigurationException e) {
			return null;
		}
	}

	public MsgflowImpl parse(final String source) {
		Document document;
		try {
			document = createDocument(source);
			return parse(document);
		} catch (SAXParseException e) {
			throw new RecognitionException(e.getLineNumber(), e.getMessage());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			
			
			return null;
		}
	}

}