package com.exxeta.iss.sonar.msgflow.parser;

import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.exxeta.iss.sonar.msgflow.parser.node.AbstractNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.mq.MQInputNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.mq.MQOutputNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.mq.MQReplyNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.routing.AggregateControlNodeParser;
import com.exxeta.iss.sonar.msgflow.parser.node.transformation.ComputeNodeParser;
import com.google.common.collect.ImmutableList;

public class ParserLists {
	private static final Logger LOGGER = Loggers.get(ParserLists.class);

	public static AbstractNodeParser<?> getNodeParser(final String type) {
		for (final AbstractNodeParser<?> parser : getNodeParsers()) {
			if (parser.getNodeType().equals(type)) {
				return parser;
			}
		}
		LOGGER.error("Cannot find node parser for " + type);
		return null;
	}

	public static List<AbstractNodeParser<?>> getNodeParsers() {
		return ImmutableList.<AbstractNodeParser<?>>of(new AggregateControlNodeParser(), new MQInputNodeParser(),
				new MQOutputNodeParser(), new MQReplyNodeParser(), new ComputeNodeParser());
	}

	private ParserLists() {

	}
}
