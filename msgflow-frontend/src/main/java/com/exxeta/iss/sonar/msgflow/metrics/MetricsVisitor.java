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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.ce.measure.RangeDistributionBuilder;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;
import com.exxeta.iss.sonar.msgflow.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowFileImpl;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowSubscriptionVisitor;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.google.common.collect.ImmutableSet;

public class MetricsVisitor extends MsgflowSubscriptionVisitor {

	private static final Number[] LIMITS_COMPLEXITY_FUNCTIONS = { 1, 2, 4, 6, 8, 10, 12, 20, 30 };
	private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20, 30, 60, 90 };

	/*
	 * private static final Kind[] CLASS_NODES = { Kind.CLASS_DECLARATION,
	 * Kind.CLASS_EXPRESSION };
	 */

	private final SensorContext sensorContext;
	private InputFile inputFile;
	private final Boolean ignoreHeaderComments;
	private Map<InputFile, Set<Integer>> projectExecutableLines;

	private int moduleComplexity;
	private int functionComplexity;
	private RangeDistributionBuilder functionComplexityDistribution;
	private RangeDistributionBuilder fileComplexityDistribution;

	public MetricsVisitor(SensorContext context, Boolean ignoreHeaderComments) {
		this.sensorContext = context;
		this.ignoreHeaderComments = ignoreHeaderComments;
		this.projectExecutableLines = new HashMap<>();
	}

	/**
	 * Returns executable lines of code for files in project
	 */
	public Map<InputFile, Set<Integer>> executableLines() {
		return projectExecutableLines;
	}

	@Override
	public Set<Kind> nodesToVisit() {
		return ImmutableSet.of(Kind.MQ_INPUT, Kind.AGGREGATE_CONTROL);
	}

	@Override
	public void leaveFile(Tree scriptTree) {
		saveComplexityMetrics(getContext());
		saveCounterMetrics(getContext());
	}

	@Override
	public void visitNode(Tree tree) {
		if (tree.is(Kind.MQ_INPUT)) {
			moduleComplexity += new ComplexityVisitor().getComplexity(tree);

		}
	}

	@Override
	public void visitFile(Tree scriptTree) {
		this.inputFile = ((MsgflowFileImpl) getContext().getMsgflowFile()).inputFile();
		init();
	}

	private void init() {
		moduleComplexity = 0;
		functionComplexityDistribution = new RangeDistributionBuilder(LIMITS_COMPLEXITY_FUNCTIONS);
		fileComplexityDistribution = new RangeDistributionBuilder(FILES_DISTRIB_BOTTOM_LIMITS);
	}

	private void saveCounterMetrics(MsgflowVisitorContext context) {
		CounterVisitor counter = new CounterVisitor(context.getMsgflow());
		saveMetricOnFile(MsgflowMetrics.CONNECTIONS, counter.getConnectionNumber());
		saveMetricOnFile(MsgflowMetrics.NODES, counter.getNodeNumber());
	}

	private void saveComplexityMetrics(MsgflowVisitorContext context) {
		int fileComplexity = new ComplexityVisitor().getComplexity(context.getMsgflow());

		saveMetricOnFile(CoreMetrics.COMPLEXITY, fileComplexity);

	}

	private <T extends Serializable> void saveMetricOnFile(Metric metric, T value) {
		sensorContext.<T>newMeasure().withValue(value).forMetric(metric).on(inputFile).save();
	}

}
