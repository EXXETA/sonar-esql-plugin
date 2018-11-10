package com.exxeta.iss.sonar.msgflow.parser;

import javax.xml.xpath.XPathExpressionException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

public class AggregateControlParser extends NodeParser<AggregateControlNode> {

	private static final String NODE_TYPE = "ComIbmAggregateControl.msgnode";
	private static final Logger LOGGER = Loggers.get(AggregateControlParser.class);

	@Override
	public String getNodeType() {
		return NODE_TYPE;
	}

	@Override
	public AggregateControlNode parseMessageFlowNode(final Element nodeElement) {

		try {
			return new AggregateControlNode(nodeElement, getId(nodeElement), getName(nodeElement),
					getLocation(nodeElement), nodeElement.getAttribute("timeoutLocation"),
					nodeElement.getAttribute("timeoutInterval"), nodeElement.getAttribute("aggregateName"));
		} catch (final XPathExpressionException e) {
			LOGGER.error("cannot parse AggregateControlNode", e);
		}

		return null;
	}

}
