/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.msgflow.metrics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;
import com.exxeta.iss.sonar.msgflow.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowSubscriptionVisitor;

public class CounterVisitor extends MsgflowSubscriptionVisitor {

	private static final Kind[] NODES = { Kind.AGGREGATE_CONTROL, Kind.MQ_INPUT, Kind.MQ_OUTPUT};
	private int nodeCounter = 0;
	private int connectionCounter = 0;

	public CounterVisitor(final Tree tree) {
		scanTree(tree);
	}

	public int getConnectionNumber() {
		return connectionCounter;
	}

	public int getNodeNumber() {
		return nodeCounter;
	}

	@Override
	public Set<Kind> nodesToVisit() {

		final Set<Kind> result = new HashSet<>();
		result.addAll(Arrays.asList(NODES));
		result.add(Kind.CONNECTION);
		return result;
	}

	@Override
	public void visitNode(final Tree tree) {
		if (tree.is(NODES)) {
			nodeCounter++;
		} else if (tree.is(Kind.CONNECTION)) {
			connectionCounter++;
		}
	}
}
