package com.exxeta.iss.sonar.msgflow.parser.node.transformation;

import javax.xml.xpath.XPathExpressionException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.parser.node.AbstractNodeParser;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.transformation.ComputeNodeImpl;

public class ComputeNodeParser extends AbstractNodeParser<ComputeNodeImpl> {

	private static final String NODE_TYPE = "ComIbmCompute.msgnode";
	private static final Logger LOGGER = Loggers.get(ComputeNodeParser.class);

	@Override
	public String getNodeType() {
		return NODE_TYPE;
	}

	@Override
	public ComputeNodeImpl internalParse(final Element nodeElement) throws XPathExpressionException {

			return new ComputeNodeImpl(nodeElement, getId(nodeElement), getName(nodeElement),
					getLocationX(nodeElement), getLocationY(nodeElement), getDataSource(nodeElement));

	}

	private String getDataSource(final Element nodeElement)throws XPathExpressionException  {
		return nodeElement.getAttribute("dataSource");
	}
}
