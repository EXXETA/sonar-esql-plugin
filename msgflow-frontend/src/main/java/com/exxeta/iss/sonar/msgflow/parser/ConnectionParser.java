package com.exxeta.iss.sonar.msgflow.parser;

import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.tree.impl.MessageFlowConnectionImpl;

public class ConnectionParser {

	public MessageFlowConnection parse(Element element) {
		//      <connections xmi:type="eflow:FCMConnection" xmi:id="FCMConnection_1" targetNode="FCMComposite_1_2" sourceNode="FCMComposite_1_1" sourceTerminalName="OutTerminal.failure" targetTerminalName="InTerminal.in"/>
		
		String sourceNode = element.getAttribute("sourceNode");
		String targetNode = element.getAttribute("targetNode");
		String sourceTerminal = element.getAttribute("sourceTerminalName");
		String targetTerminal = element.getAttribute("targetTerminalName");

		return new MessageFlowConnectionImpl(element, sourceNode, sourceTerminal, targetNode, targetTerminal);
	}

}
