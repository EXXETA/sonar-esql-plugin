/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.checks.NoSonarFilter;
import org.sonar.api.component.Perspective;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.source.Highlightable;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.indexer.QueryByParent;
import org.sonar.squidbridge.indexer.QueryByType;

import com.exxeta.iss.sonar.esql.api.EsqlMetric;
import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.esql.colorizer.EsqlHighlighter;
import com.exxeta.iss.sonar.esql.core.Esql;
import com.exxeta.iss.sonar.esql.metrics.FileLinesVisitor;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.Grammar;

public class EsqlSquidSensor implements Sensor {

	private static final Logger LOG = LoggerFactory.getLogger(EsqlSquidSensor.class);
	private final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = { 1, 2, 4, 6, 8, 10, 12, 20, 30 };
	private final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20, 30, 60, 90 };

	private final Checks<SquidAstVisitor<Grammar>> checks;
	private final FileLinesContextFactory fileLinesContextFactory;
	private final FilePredicate mainFilePredicate;
	private final FileSystem fileSystem;
	private final ResourcePerspectives resourcePerspectives;
	private final Settings settings;
	private final NoSonarFilter noSonarFilter;
	private final PathResolver pathResolver;

	private SensorContext context;
	private AstScanner<Grammar> scanner;

	public EsqlSquidSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory,
			ResourcePerspectives perspectives, FileSystem fileSystem, NoSonarFilter noSonarFilter,
			PathResolver pathResolver, Settings settings) {
		this.checks = checkFactory.<SquidAstVisitor<Grammar>> create(CheckList.REPOSITORY_KEY).addAnnotatedChecks(
				CheckList.getChecks());
		this.fileLinesContextFactory = fileLinesContextFactory;
		this.fileSystem = fileSystem;
		this.resourcePerspectives = perspectives;
		this.noSonarFilter = noSonarFilter;
		this.pathResolver = pathResolver;
		this.settings = settings;
		this.mainFilePredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(Esql.KEY));
	}

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		FilePredicates p = fileSystem.predicates();
		return fileSystem.hasFiles(p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(Esql.KEY)));
	}

	@Override
	public void analyse(Project project, SensorContext context) {
		this.context = context;
		List<SquidAstVisitor<Grammar>> visitors = Lists.newArrayList(checks.all());

		visitors.add(new FileLinesVisitor(fileLinesContextFactory, fileSystem));

		scanner = EsqlAstScanner.create(createConfiguration(), visitors.toArray(new SquidAstVisitor[visitors.size()]));
		FilePredicates p = fileSystem.predicates();
		scanner.scanFiles(Lists.newArrayList(fileSystem.files(mainFilePredicate)));

		Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
		save(squidSourceFiles);
		highlight();
	}

	private void highlight() {
		EsqlHighlighter highlighter = new EsqlHighlighter(createConfiguration());
		for (InputFile inputFile : fileSystem.inputFiles(mainFilePredicate)) {
			highlighter.highlight(perspective(Highlightable.class, inputFile), inputFile.file());
		}
	}

	<P extends Perspective<?>> P perspective(Class<P> clazz, @Nullable InputFile file) {
		if (file == null) {
			throw new IllegalArgumentException("Cannot get " + clazz.getCanonicalName() + "for a null file");
		}
		P result = resourcePerspectives.as(clazz, file);
		if (result == null) {
			throw new IllegalStateException("Could not get " + clazz.getCanonicalName() + " for " + file);
		}
		return result;
	}

	private EsqlConfiguration createConfiguration() {
		return new EsqlConfiguration(fileSystem.encoding());
	}

	private void save(Collection<SourceCode> squidSourceFiles) {
		for (SourceCode squidSourceFile : squidSourceFiles) {
			SourceFile squidFile = (SourceFile) squidSourceFile;
			File sonarFile = context.getResource(File.create(pathResolver.relativePath(fileSystem.baseDir(),
					new java.io.File(squidFile.getKey()))));

			if (sonarFile != null) {
				noSonarFilter.addResource(sonarFile, squidFile.getNoSonarTagLines());
				saveFilesComplexityDistribution(sonarFile, squidFile);
				saveFunctionsComplexityDistribution(sonarFile, squidFile);
				saveMeasures(sonarFile, squidFile);
				saveIssues(sonarFile, squidFile);
			} else {
				LOG.warn(
						"Cannot save analysis information for file {}. Unable to retrieve the associated sonar resource.",
						squidFile.getKey());
			}

			InputFile inputFile = fileSystem
					.inputFile(fileSystem.predicates().is(new java.io.File(squidFile.getKey())));
		}
	}

	private void saveMeasures(File sonarFile, SourceFile squidFile) {
		context.saveMeasure(sonarFile, CoreMetrics.FILES, squidFile.getDouble(EsqlMetric.FILES));
		context.saveMeasure(sonarFile, CoreMetrics.LINES, squidFile.getDouble(EsqlMetric.LINES));
		context.saveMeasure(sonarFile, CoreMetrics.NCLOC, squidFile.getDouble(EsqlMetric.LINES_OF_CODE));
		context.saveMeasure(sonarFile, CoreMetrics.STATEMENTS, squidFile.getDouble(EsqlMetric.STATEMENTS));
		context.saveMeasure(sonarFile, CoreMetrics.COMPLEXITY, squidFile.getDouble(EsqlMetric.COMPLEXITY));
		context.saveMeasure(sonarFile, CoreMetrics.COMMENT_LINES, squidFile.getDouble(EsqlMetric.COMMENT_LINES));
		context.saveMeasure(sonarFile, CoreMetrics.FUNCTIONS, squidFile.getDouble(EsqlMetric.ROUTINES));

	}

	private void saveFunctionsComplexityDistribution(File sonarFile, SourceFile squidFile) {
		Collection<SourceCode> squidFunctionsInFile = scanner.getIndex().search(new QueryByParent(squidFile),
				new QueryByType(SourceFunction.class));
		RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(
				CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
		for (SourceCode squidFunction : squidFunctionsInFile) {
			complexityDistribution.add(squidFunction.getDouble(EsqlMetric.COMPLEXITY));
		}
		context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
	}

	private void saveFilesComplexityDistribution(File sonarFile, SourceFile squidFile) {
		RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(
				CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, FILES_DISTRIB_BOTTOM_LIMITS);
		complexityDistribution.add(squidFile.getDouble(EsqlMetric.COMPLEXITY));
		context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
	}

	private void saveIssues(File sonarFile, SourceFile squidFile) {
		Collection<CheckMessage> messages = squidFile.getCheckMessages();
		for (CheckMessage message : messages) {
			RuleKey ruleKey = checks.ruleKey((SquidAstVisitor<Grammar>) message.getCheck());
			Issuable issuable = resourcePerspectives.as(Issuable.class, sonarFile);
			if (issuable != null) {
				Issue issue = issuable.newIssueBuilder().ruleKey(ruleKey).line(message.getLine())
						.message(message.getText(Locale.ENGLISH)).build();
				issuable.addIssue(issue);
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
