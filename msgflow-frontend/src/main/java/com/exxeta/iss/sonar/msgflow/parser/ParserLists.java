package com.exxeta.iss.sonar.msgflow.parser;

import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.google.common.collect.ImmutableList;

public class ParserLists {
	private static final Logger LOGGER = Loggers.get(ParserLists.class);
	private ParserLists() {

	}

	public static List<NodeParser<?>> getNodeParsers() {
		return ImmutableList.<NodeParser<?>>of(new AggregateControlParser());
	}

	public static NodeParser<?> getNodeParser(String type) {
		for (NodeParser<?> parser : getNodeParsers()) {
			if (parser.getNodeType().equals(type)) {
				return parser;
			}
		}
		LOGGER.error("Cannot find parser for "+type);
		return null;
	}
}
