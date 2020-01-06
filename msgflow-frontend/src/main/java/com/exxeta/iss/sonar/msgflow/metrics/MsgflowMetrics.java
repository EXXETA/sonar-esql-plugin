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

import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import com.google.common.collect.Lists;

public class MsgflowMetrics implements Metrics {
	public static final String NODE_KEY = "nodes";
	public static final Metric<Integer> NODES = new Metric.Builder(NODE_KEY, "Nodes",
			Metric.ValueType.INT).setDescription("Nodes").setDirection(Metric.DIRECTION_WORST)
					.setQualitative(false).setDomain(CoreMetrics.DOMAIN_SIZE).create();
	public static final String CONNECTIONS_KEY = "connections";
	public static final Metric<Integer> CONNECTIONS = new Metric.Builder(CONNECTIONS_KEY, "Connections", Metric.ValueType.INT)
			.setDescription("Connections").setDirection(Metric.DIRECTION_WORST).setQualitative(false)
			.setDomain(CoreMetrics.DOMAIN_SIZE).create();
	
	private static final List<Metric> metrics = Lists.newArrayList(NODES, CONNECTIONS);
	@Override
	public List<Metric> getMetrics() {
		return metrics;
	}
}
