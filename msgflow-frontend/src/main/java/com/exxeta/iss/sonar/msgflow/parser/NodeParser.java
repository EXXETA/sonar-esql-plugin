package com.exxeta.iss.sonar.msgflow.parser;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.getXPathString;

import javax.xml.xpath.XPathExpressionException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

public abstract class NodeParser<T extends MessageFlowNode> {
	private static final Logger LOGGER = Loggers.get(NodeParser.class);
	public abstract String getNodeType();

	public abstract T parseMessageFlowNode(Element nodeElement);

	protected String getId(Element nodeElement) throws XPathExpressionException {
		return nodeElement.getAttribute("id");
	}
	protected String getLocation(Element nodeElement) throws XPathExpressionException {
		return nodeElement.getAttribute("location");
	}

	protected String getName(Element nodeElement) throws XPathExpressionException {
		return getXPathString(nodeElement, "./translation/@string");
	}

	
}
