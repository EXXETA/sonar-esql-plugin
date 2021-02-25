/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.ce.measure.RangeDistributionBuilder;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFileImpl;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.google.common.collect.ImmutableSet;

public class MetricsVisitor extends SubscriptionVisitor {

	private static final Number[] LIMITS_COMPLEXITY_FUNCTIONS = { 1, 2, 4, 6, 8, 10, 12, 20, 30 };
	private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20, 30, 60, 90 };

	private final SensorContext sensorContext;
	private InputFile inputFile;
	private final Boolean ignoreHeaderComments;
	private FileLinesContextFactory fileLinesContextFactory;
	private Map<InputFile, Set<Integer>> projectExecutableLines;

	private int moduleComplexity;
	private int functionComplexity;
	private RangeDistributionBuilder functionComplexityDistribution;
	private RangeDistributionBuilder fileComplexityDistribution;

	public MetricsVisitor(SensorContext context, Boolean ignoreHeaderComments,
			FileLinesContextFactory fileLinesContextFactory) {
		this.sensorContext = context;
		this.ignoreHeaderComments = ignoreHeaderComments;
		this.fileLinesContextFactory = fileLinesContextFactory;
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
		return ImmutableSet.of(Kind.CREATE_MODULE_STATEMENT);
	}

	@Override
	public void leaveFile(Tree scriptTree) {
		saveComplexityMetrics(getContext());
		saveCounterMetrics(getContext());
		saveLineMetrics(getContext());
	}

	@Override
	public void visitNode(Tree tree) {
		if (tree.is(Kind.CREATE_MODULE_STATEMENT)) {
			moduleComplexity += new ComplexityVisitor().getComplexity(tree);

		}
	}

	@Override
	public void visitFile(Tree scriptTree) {
		this.inputFile = ((EsqlFileImpl) getContext().getEsqlFile()).inputFile();
		init();
	}

	private void init() {
		moduleComplexity = 0;
		functionComplexityDistribution = new RangeDistributionBuilder(LIMITS_COMPLEXITY_FUNCTIONS);
		fileComplexityDistribution = new RangeDistributionBuilder(FILES_DISTRIB_BOTTOM_LIMITS);
	}

	private void saveCounterMetrics(TreeVisitorContext context) {
		CounterVisitor counter = new CounterVisitor(context.getTopTree());
		saveMetricOnFile(CoreMetrics.FUNCTIONS, counter.getFunctionsNumber());
		saveMetricOnFile(CoreMetrics.STATEMENTS, counter.getStatementsNumber());
		saveMetricOnFile(EsqlMetrics.MODULES, counter.getModulesNumber());
		saveMetricOnFile(EsqlMetrics.PROCEDURES, counter.getProceduresNumber());
	}

	private void saveComplexityMetrics(TreeVisitorContext context) {
		int fileComplexity = new ComplexityVisitor().getComplexity(context.getTopTree());

		saveMetricOnFile(CoreMetrics.COMPLEXITY, fileComplexity);
		saveMetricOnFile(EsqlMetrics.MODULE_COMPLEXITY, moduleComplexity);
		saveMetricOnFile(CoreMetrics.COMPLEXITY_IN_FUNCTIONS, functionComplexity);

		sensorContext.<String>newMeasure().on(inputFile).forMetric(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION)
				.withValue(functionComplexityDistribution.build()).save();

		fileComplexityDistribution.add(fileComplexity);

		sensorContext.<String>newMeasure().on(inputFile).forMetric(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION)
				.withValue(fileComplexityDistribution.build()).save();
	}

	private void saveLineMetrics(TreeVisitorContext context) {
		LineVisitor lineVisitor = new LineVisitor(context.getTopTree());
		Set<Integer> linesOfCode = lineVisitor.getLinesOfCode();

		saveMetricOnFile(CoreMetrics.NCLOC, lineVisitor.getLinesOfCodeNumber());

		CommentLineVisitor commentVisitor = new CommentLineVisitor(context.getTopTree(), ignoreHeaderComments);
		Set<Integer> commentLines = commentVisitor.getCommentLines();

		saveMetricOnFile(CoreMetrics.COMMENT_LINES, commentVisitor.getCommentLineNumber());

		FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(this.inputFile);

		linesOfCode.forEach(line -> fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1));
		commentLines.forEach(line -> fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, 1));

		Set<Integer> executableLines = new ExecutableLineVisitor(context.getTopTree()).getExecutableLines();
		projectExecutableLines.put(inputFile, executableLines);

		executableLines.stream()
				.forEach(line -> fileLinesContext.setIntValue(CoreMetrics.EXECUTABLE_LINES_DATA_KEY, line, 1));
		fileLinesContext.save();
	}

	private <T extends Serializable> void saveMetricOnFile(Metric metric, T value) {
		sensorContext.<T>newMeasure().withValue(value).forMetric(metric).on(inputFile).save();
	}

}
