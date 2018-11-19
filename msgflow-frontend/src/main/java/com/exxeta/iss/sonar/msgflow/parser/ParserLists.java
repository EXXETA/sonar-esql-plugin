package com.exxeta.iss.sonar.msgflow.parser;

import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.exxeta.iss.sonar.msgflow.parser.node.NodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.mq.MQInputNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.mq.MQOutputNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.routing.AggregateControlNodeParser;
import com.google.common.collect.ImmutableList;

public class ParserLists {
	private static final Logger LOGGER = Loggers.get(ParserLists.class);

	public static NodeParser<?> getNodeParser(final String type) {
		for (final NodeParser<?> parser : getNodeParsers()) {
			if (parser.getNodeType().equals(type)) {
				return parser;
			}
		}
		LOGGER.error("Cannot find node parser for " + type);
		return null;
	}

	public static List<NodeParser<?>> getNodeParsers() {
		return ImmutableList.<NodeParser<?>>of(new AggregateControlNodeParser(), new MQInputNodeParser(), new MQOutputNodeParser());
	}

	private ParserLists() {

	}
}
