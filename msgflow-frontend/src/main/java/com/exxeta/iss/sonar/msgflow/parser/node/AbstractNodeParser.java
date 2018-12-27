package com.exxeta.iss.sonar.msgflow.parser.node;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.getXPathString;

import javax.xml.xpath.XPathExpressionException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.tree.impl.AbstractMessageFlowNode;

public abstract class AbstractNodeParser<T extends AbstractMessageFlowNode> extends AbstractTreeParser<T> {
	private static final Logger LOGGER = Loggers.get(AbstractNodeParser.class);

	

	public abstract String getNodeType();

	public T parseMessageFlowNode(Element nodeElement) {
		try {
		return internalParse(nodeElement);
		} catch (XPathExpressionException e){
			LOGGER.error("Cannot parse node", e);
			return null;
		}
	}

	protected abstract T internalParse(Element nodeElement) throws XPathExpressionException;

}
