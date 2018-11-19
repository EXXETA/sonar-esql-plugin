package com.exxeta.iss.sonar.msgflow.parser.node.mq;

import javax.xml.xpath.XPathExpressionException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.parser.node.NodeParser;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.mq.MQReplyNodeImpl;

public class MQReplyNodeParser extends NodeParser<MQReplyNodeImpl> {

	private static final String NODE_TYPE = "ComIbmMQReply.msgnode";
	private static final Logger LOGGER = Loggers.get(MQReplyNodeParser.class);

	@Override
	public String getNodeType() {
		return NODE_TYPE;
	}

	@Override
	public MQReplyNodeImpl internalParse(final Element nodeElement) throws XPathExpressionException {

			return new MQReplyNodeImpl(nodeElement, getId(nodeElement), getName(nodeElement), getLocationX(nodeElement),
					getLocationY(nodeElement), nodeElement.getAttribute("queueName"));

	}

}
