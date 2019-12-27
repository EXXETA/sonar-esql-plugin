package com.exxeta.iss.sonar.msgflow.parser.node.mq;

import com.exxeta.iss.sonar.msgflow.parser.node.AbstractNodeParser;
import com.exxeta.iss.sonar.msgflow.tree.impl.node.mq.MQHeaderNodeImpl;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathExpressionException;

public class MQHeaderNodeParser extends AbstractNodeParser<MQHeaderNodeImpl> {


    private static final String NODE_TYPE = "ComIbmMQHeader.msgnode";
    private static final Logger LOGGER = Loggers.get(MQInputNodeParser.class);

    @Override
    public String getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public MQHeaderNodeImpl internalParse(final Element nodeElement) throws XPathExpressionException {

        return new MQHeaderNodeImpl(nodeElement, getId(nodeElement), getName(nodeElement), getLocationX(nodeElement),
                getLocationY(nodeElement),
                nodeElement.getAttribute("mqmdAAAOptions"),
                nodeElement.getAttribute("mqmdCodedCharSetId"),
                nodeElement.getAttribute("mqmdVersion"),
                nodeElement.getAttribute("mqmdMsgType"),
                nodeElement.getAttribute("mqmdExpiry"),
                nodeElement.getAttribute("mqmdFeedback"),
                nodeElement.getAttribute("mqmdPriority"),
                nodeElement.getAttribute("mqmdPersistence"),
                nodeElement.getAttribute("mqmdMsgId"),
                nodeElement.getAttribute("mqmdCorrelId"),
                null, null, null
        );

    }

}
