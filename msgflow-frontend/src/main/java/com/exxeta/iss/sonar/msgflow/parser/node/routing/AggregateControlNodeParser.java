package com.exxeta.iss.sonar.msgflow.parser.node.routing;

import javax.xml.xpath.XPathExpressionException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.parser.node.NodeParser;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.routing.AggregateControlNodeImpl;

public class AggregateControlNodeParser extends NodeParser<AggregateControlNodeImpl> {

	private static final String NODE_TYPE = "ComIbmAggregateControl.msgnode";
	private static final Logger LOGGER = Loggers.get(AggregateControlNodeParser.class);

	@Override
	public String getNodeType() {
		return NODE_TYPE;
	}

	@Override
	public AggregateControlNodeImpl internalParse(final Element nodeElement) throws XPathExpressionException {

			return new AggregateControlNodeImpl(nodeElement, getId(nodeElement), getName(nodeElement),
					getLocationX(nodeElement), getLocationY(nodeElement), nodeElement.getAttribute("timeoutLocation"),
					nodeElement.getAttribute("timeoutInterval"), nodeElement.getAttribute("aggregateName"));

	}

}
