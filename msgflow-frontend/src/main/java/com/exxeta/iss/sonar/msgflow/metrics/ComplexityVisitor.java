/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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

import java.util.ArrayList;
import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.api.tree.Tree;
import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;

public class ComplexityVisitor extends DoubleDispatchMsgflowVisitor {

	private List<Tree> complexityTrees;

	private void add(final Tree tree) {
		complexityTrees.add(tree);
	}

	public List<Tree> complexityTrees(final Tree tree) {
		complexityTrees = new ArrayList<>();
		scan(tree);
		return complexityTrees;
	}

	public int getComplexity(final Tree tree) {
		return complexityTrees(tree).size();
	}

	@Override
	public void visitConnection(final MessageFlowConnection tree) {
		add(tree);
		super.visitConnection(tree);
	}

}
