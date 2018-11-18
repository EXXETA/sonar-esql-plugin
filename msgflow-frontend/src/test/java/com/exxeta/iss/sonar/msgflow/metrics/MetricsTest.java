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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;

public class MetricsTest extends MsgflowModelTest{

	@Test
	public void complexity() {
		final String path = "src/test/resources/metrics/complexity.msgflow";
		final Tree tree = parse(new File(path));
		assertThat(new ComplexityVisitor().getComplexity(tree)).isEqualTo(0);
	}

	@Test
	public void connections() {
		final String path = "src/test/resources/metrics/connections.msgflow";
		final Tree tree = parse(new File(path));
		assertThat(new CounterVisitor(tree).getConnectionNumber()).isEqualTo(0);
	}

	@Test
	public void nodes() {
		final String path = "src/test/resources/metrics/nodes.msgflow";
		final Tree tree = parse(new File(path));
		assertThat(new CounterVisitor(tree).getNodeNumber()).isEqualTo(1);
	}

	

}
