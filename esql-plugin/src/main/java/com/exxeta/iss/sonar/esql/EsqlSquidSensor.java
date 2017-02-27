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

import com.exxeta.iss.sonar.esql.api.CustomEsqlRulesDefinition;
import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.esql.check.ParsingErrorCheck;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;

import java.io.File;
import java.io.InterruptedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import org.sonar.api.SonarProduct;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.squidbridge.ProgressReport;

public class EsqlSquidSensor implements Sensor {

	  private static final Logger LOG = Loggers.get(EsqlSquidSensor.class);

	  private final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = { 1, 2, 4, 6, 8, 10, 12, 20, 30 };
	private final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20, 30, 60, 90 };

	  private final EsqlChecks checks;
	  private final FileLinesContextFactory fileLinesContextFactory;
	  private final FileSystem fileSystem;
	  private final NoSonarFilter noSonarFilter;
	  private final FilePredicate mainFilePredicate;
	  private final ActionParser<Tree> parser;
	  // parsingErrorRuleKey equals null if ParsingErrorCheck is not activated
	  private RuleKey parsingErrorRuleKey = null;

	  public EsqlSquidSensor(
	    CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, FileSystem fileSystem, NoSonarFilter noSonarFilter) {
	    this(checkFactory, fileLinesContextFactory, fileSystem, noSonarFilter, null);
	  }

	  public EsqlSquidSensor(
	    CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, FileSystem fileSystem, NoSonarFilter noSonarFilter,
	    @Nullable CustomEsqlRulesDefinition[] customRulesDefinition
	  ) {

	    this.checks = EsqlChecks.createEsqlCheck(checkFactory)
	      .addChecks(CheckList.REPOSITORY_KEY, CheckList.getChecks())
	      .addCustomChecks(customRulesDefinition);
	    this.fileLinesContextFactory = fileLinesContextFactory;
	    this.fileSystem = fileSystem;
	    this.noSonarFilter = noSonarFilter;
	    this.mainFilePredicate = fileSystem.predicates().and(
	      fileSystem.predicates().hasType(InputFile.Type.MAIN),
	      fileSystem.predicates().hasLanguage(EsqlLanguage.KEY));
	    this.parser = EsqlParserBuilder.createParser(getEncoding());
	  }

	  
	  private Charset getEncoding() {
	    return fileSystem.encoding();
	  }
//	@Override
//	public boolean shouldExecuteOnProject(Project project) {
//		FilePredicates p = fileSystem.predicates();
//		return fileSystem.hasFiles(p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(Esql.KEY)));
//	}
//
//	@Override
//	public void analyse(Project project, SensorContext context) {
//		this.context = context;
//		List<SquidAstVisitor<Grammar>> visitors = Lists.newArrayList(checks.all());
//
//		visitors.add(new FileLinesVisitor(fileLinesContextFactory, fileSystem));
//
//		scanner = EsqlAstScanner.create(createConfiguration(), visitors.toArray(new SquidAstVisitor[visitors.size()]));
//		FilePredicates p = fileSystem.predicates();
//		scanner.scanFiles(Lists.newArrayList(fileSystem.files(mainFilePredicate)));
//
//		Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
//		save(squidSourceFiles);
//		highlight();
//	}
//
//	private void highlight() {
//		EsqlHighlighter highlighter = new EsqlHighlighter(createConfiguration());
//		for (InputFile inputFile : fileSystem.inputFiles(mainFilePredicate)) {
//			highlighter.highlight(perspective(Highlightable.class, inputFile), inputFile.file());
//		}
//	}
//
//	<P extends Perspective<?>> P perspective(Class<P> clazz, @Nullable InputFile file) {
//		if (file == null) {
//			throw new IllegalArgumentException("Cannot get " + clazz.getCanonicalName() + "for a null file");
//		}
//		P result = resourcePerspectives.as(clazz, file);
//		if (result == null) {
//			throw new IllegalStateException("Could not get " + clazz.getCanonicalName() + " for " + file);
//		}
//		return result;
//	}
//
//	private EsqlConfiguration createConfiguration() {
//		return new EsqlConfiguration(fileSystem.encoding());
//	}
//
//	private void save(Collection<SourceCode> squidSourceFiles) {
//		for (SourceCode squidSourceFile : squidSourceFiles) {
//			SourceFile squidFile = (SourceFile) squidSourceFile;
//			File sonarFile = context.getResource(File.create(pathResolver.relativePath(fileSystem.baseDir(),
//					new java.io.File(squidFile.getKey()))));
//
//			if (sonarFile != null) {
//				noSonarFilter.addResource(sonarFile, squidFile.getNoSonarTagLines());
//				saveFilesComplexityDistribution(sonarFile, squidFile);
//				saveFunctionsComplexityDistribution(sonarFile, squidFile);
//				saveMeasures(sonarFile, squidFile);
//				saveIssues(sonarFile, squidFile);
//			} else {
//				LOG.warn(
//						"Cannot save analysis information for file {}. Unable to retrieve the associated sonar resource.",
//						squidFile.getKey());
//			}
//
//			InputFile inputFile = fileSystem
//					.inputFile(fileSystem.predicates().is(new java.io.File(squidFile.getKey())));
//		}
//	}
//
//	private void saveMeasures(File sonarFile, SourceFile squidFile) {
//		context.saveMeasure(sonarFile, CoreMetrics.FILES, squidFile.getDouble(EsqlMetric.FILES));
//		context.saveMeasure(sonarFile, CoreMetrics.LINES, squidFile.getDouble(EsqlMetric.LINES));
//		context.saveMeasure(sonarFile, CoreMetrics.NCLOC, squidFile.getDouble(EsqlMetric.LINES_OF_CODE));
//		context.saveMeasure(sonarFile, CoreMetrics.STATEMENTS, squidFile.getDouble(EsqlMetric.STATEMENTS));
//		context.saveMeasure(sonarFile, CoreMetrics.COMPLEXITY, squidFile.getDouble(EsqlMetric.COMPLEXITY));
//		context.saveMeasure(sonarFile, CoreMetrics.COMMENT_LINES, squidFile.getDouble(EsqlMetric.COMMENT_LINES));
//		context.saveMeasure(sonarFile, CoreMetrics.FUNCTIONS, squidFile.getDouble(EsqlMetric.ROUTINES));
//
//	}
//
//	private void saveFunctionsComplexityDistribution(File sonarFile, SourceFile squidFile) {
//		Collection<SourceCode> squidFunctionsInFile = scanner.getIndex().search(new QueryByParent(squidFile),
//				new QueryByType(SourceFunction.class));
//		RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(
//				CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
//		for (SourceCode squidFunction : squidFunctionsInFile) {
//			complexityDistribution.add(squidFunction.getDouble(EsqlMetric.COMPLEXITY));
//		}
//		context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
//	}
//
//	private void saveFilesComplexityDistribution(File sonarFile, SourceFile squidFile) {
//		RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(
//				CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, FILES_DISTRIB_BOTTOM_LIMITS);
//		complexityDistribution.add(squidFile.getDouble(EsqlMetric.COMPLEXITY));
//		context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
//	}
//
//	private void saveIssues(File sonarFile, SourceFile squidFile) {
//		Collection<CheckMessage> messages = squidFile.getCheckMessages();
//		for (CheckMessage message : messages) {
//			RuleKey ruleKey = checks.ruleKey((SquidAstVisitor<Grammar>) message.getCheck());
//			Issuable issuable = resourcePerspectives.as(Issuable.class, sonarFile);
//			if (issuable != null) {
//				Issue issue = issuable.newIssueBuilder().ruleKey(ruleKey).line(message.getLine())
//						.message(message.getText(Locale.ENGLISH)).build();
//				issuable.addIssue(issue);
//			}
//		}
//	}
//
//	@Override
//	public String toString() {
//		return getClass().getSimpleName();
//	}


	  @Override
	  public void describe(SensorDescriptor descriptor) {
	    descriptor
	      .onlyOnLanguage(EsqlLanguage.KEY)
	      .name("Esql Squid Sensor")
	      .onlyOnFileType(Type.MAIN);
	  }

	  @Override
	  public void execute(SensorContext context) {
	    ProductDependentExecutor executor = createProductDependentExecutor(context);

	    List<TreeVisitor> treeVisitors = Lists.newArrayList();
	    treeVisitors.addAll(executor.getProductDependentTreeVisitors());
	    treeVisitors.add(new SeChecksDispatcher(checks.seChecks()));
	    treeVisitors.addAll(checks.visitorChecks());

	    for (TreeVisitor check : treeVisitors) {
	      if (check instanceof ParsingErrorCheck) {
	        parsingErrorRuleKey = checks.ruleKeyFor((EsqlCheck) check);
	        break;
	      }
	    }

	    Iterable<CompatibleInputFile> inputFiles = wrap(fileSystem.inputFiles(mainFilePredicate), context);
	    Collection<File> files = StreamSupport.stream(inputFiles.spliterator(), false)
	      .map(CompatibleInputFile::file)
	      .collect(Collectors.toList());

	    ProgressReport progressReport = new ProgressReport("Report about progress of Javascript analyzer", TimeUnit.SECONDS.toMillis(10));
	    progressReport.start(files);

	    analyseFiles(context, treeVisitors, inputFiles, executor, progressReport);

	    executor.executeCoverageSensors();
	  }

	  @VisibleForTesting
	  protected interface ProductDependentExecutor {
	    List<TreeVisitor> getProductDependentTreeVisitors();

	    void highlightSymbols(InputFile inputFile, TreeVisitorContext treeVisitorContext);

	    void executeCoverageSensors();
	  }

	  private static class SonarQubeProductExecutor implements ProductDependentExecutor {
	    private final SensorContext context;
	    private final NoSonarFilter noSonarFilter;
	    private final FileLinesContextFactory fileLinesContextFactory;
	    private final boolean isAtLeastSq62;
	    private MetricsVisitor metricsVisitor;

	    SonarQubeProductExecutor(SensorContext context, NoSonarFilter noSonarFilter, FileLinesContextFactory fileLinesContextFactory) {
	      this.context = context;
	      this.noSonarFilter = noSonarFilter;
	      this.fileLinesContextFactory = fileLinesContextFactory;
	      this.isAtLeastSq62 = context.getSonarQubeVersion().isGreaterThanOrEqual(V6_2);
	    }

	    @Override
	    public List<TreeVisitor> getProductDependentTreeVisitors() {
	      metricsVisitor = new MetricsVisitor(
	        context,
	        noSonarFilter,
	        context.settings().getBoolean(JavaScriptPlugin.IGNORE_HEADER_COMMENTS),
	        fileLinesContextFactory,
	        isAtLeastSq62);
	      return Arrays.asList(metricsVisitor, new HighlighterVisitor(context), new CpdVisitor(context));
	    }

	    @Override
	    public void highlightSymbols(InputFile inputFile, TreeVisitorContext treeVisitorContext) {
	      NewSymbolTable newSymbolTable = context.newSymbolTable().onFile(inputFile);
	      HighlightSymbolTableBuilder.build(newSymbolTable, treeVisitorContext);
	    }

	    @Override
	    public void executeCoverageSensors() {
	      if (metricsVisitor == null) {
	        throw new IllegalStateException("Before starting coverage computation, metrics should have been calculated.");
	      }
	      executeCoverageSensors(context, metricsVisitor.linesOfCode(), isAtLeastSq62);
	    }

	    private static void executeCoverageSensors(SensorContext context, Map<InputFile, Set<Integer>> linesOfCode, boolean isAtLeastSq62) {
	      Settings settings = context.settings();
	      if (isAtLeastSq62 && settings.getBoolean(EsqlPlugin.FORCE_ZERO_COVERAGE_KEY)) {
	        LOG.warn("Since SonarQube 6.2 property 'sonar.javascript.forceZeroCoverage' is removed and its value is not used during analysis");
	      }

	      if (isAtLeastSq62) {
	        logDeprecationForReportProperty(settings, EsqlPlugin.LCOV_UT_REPORT_PATH);
	        logDeprecationForReportProperty(settings, EsqlPlugin.LCOV_IT_REPORT_PATH);

	        String lcovReports = settings.getString(EsqlPlugin.LCOV_REPORT_PATHS);

	        if (lcovReports == null || lcovReports.isEmpty()) {
	          executeDeprecatedCoverageSensors(context, linesOfCode, true);

	        } else {
	          LOG.info("Test Coverage Sensor is started");
	          (new LCOVCoverageSensor()).execute(context, linesOfCode, true);
	        }

	      } else {
	        executeDeprecatedCoverageSensors(context, linesOfCode, false);
	      }
	    }

	    private static void logDeprecationForReportProperty(Settings settings, String propertyKey) {
	      String value = settings.getString(propertyKey);
	      if (value != null && !value.isEmpty()) {
	        LOG.warn("Since SonarQube 6.2 property '" + propertyKey + "' is deprecated. Use 'sonar.javascript.lcov.reportPaths' instead.");
	      }
	    }

	    private static void executeDeprecatedCoverageSensors(SensorContext context, Map<InputFile, Set<Integer>> linesOfCode, boolean isAtLeastSq62) {
	      LOG.info("Unit Test Coverage Sensor is started");
	      (new UTCoverageSensor()).execute(context, linesOfCode, isAtLeastSq62);
	      LOG.info("Integration Test Coverage Sensor is started");
	      (new ITCoverageSensor()).execute(context, linesOfCode, isAtLeastSq62);
	      LOG.info("Overall Coverage Sensor is started");
	      (new OverallCoverageSensor()).execute(context, linesOfCode, isAtLeastSq62);
	    }
	  }

	  @VisibleForTesting
	  protected static class SonarLintProductExecutor implements ProductDependentExecutor {
	    @Override
	    public List<TreeVisitor> getProductDependentTreeVisitors() {
	      return Collections.emptyList();
	    }

	    @Override
	    public void highlightSymbols(InputFile inputFile, TreeVisitorContext treeVisitorContext) {
	      // unnecessary in SonarLint context
	    }

	    @Override
	    public void executeCoverageSensors() {
	      // unnecessary in SonarLint context
	    }
	  }

	  private static boolean isSonarLint(SensorContext context) {
	    return context.getSonarQubeVersion().isGreaterThanOrEqual(V6_0) && context.runtime().getProduct() == SonarProduct.SONARLINT;
	  }

	  private static void saveLineIssue(SensorContext sensorContext, InputFile inputFile, RuleKey ruleKey, LineIssue issue) {
	    NewIssue newIssue = sensorContext.newIssue();

	    NewIssueLocation primaryLocation = newIssue.newLocation()
	      .message(issue.message())
	      .on(inputFile)
	      .at(inputFile.selectLine(issue.line()));

	    saveIssue(newIssue, primaryLocation, ruleKey, issue);
	  }

	  private static void saveFileIssue(SensorContext sensorContext, InputFile inputFile, RuleKey ruleKey, FileIssue issue) {
	    NewIssue newIssue = sensorContext.newIssue();

	    NewIssueLocation primaryLocation = newIssue.newLocation()
	      .message(issue.message())
	      .on(inputFile);

	    saveIssue(newIssue, primaryLocation, ruleKey, issue);
	  }

	  private static void saveIssue(NewIssue newIssue, NewIssueLocation primaryLocation, RuleKey ruleKey, Issue issue) {
	    newIssue
	      .forRule(ruleKey)
	      .at(primaryLocation);

	    if (issue.cost() != null) {
	      newIssue.gap(issue.cost());
	    }

	    newIssue.save();
	  }

}
