package com.exxeta.iss.sonar.msgflow.parser;

import static com.exxeta.iss.sonar.msgflow.parser.ParserUtils.getXPathString;

import java.util.ArrayList;

import javax.xml.xpath.XPathExpressionException;

import com.google.common.collect.Lists;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Element;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowCommentNote;
import com.exxeta.iss.sonar.msgflow.parser.node.AbstractTreeParser;
import com.exxeta.iss.sonar.msgflow.tree.impl.MessageFlowCommentNoteImpl;

public class StickeyNoteParser extends AbstractTreeParser<MessageFlowCommentNoteImpl>{
	private static final Logger LOGGER = Loggers.get(StickeyNoteParser.class);
	public MessageFlowCommentNote parse(Element element){
		
		try {
			ArrayList<String> associations = Lists.newArrayList(element.getAttribute("association").split(" "));
			String comment = getXPathString(element, "./body/@string");

				
				final int locationX = getLocationX(element);
				final int locationY = getLocationY(element);
				final MessageFlowCommentNoteImpl msgFlowComment = new MessageFlowCommentNoteImpl(element, associations,
						comment, locationX, locationY);
				return msgFlowComment;
		} catch (XPathExpressionException e) {
			LOGGER.error("Cannot parse stickey note",e);
			return null;
		}
	}

}
