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
import org.sonar.squidbridge.api.AnalysisException;

import static com.exxeta.iss.sonar.esql.compat.CompatibilityHelper.wrap;

public class EsqlSquidSensor implements Sensor {

  private static final Logger LOG = Loggers.get(EsqlSquidSensor.class);

  private static final Version V6_0 = Version.create(6, 0);
  private static final Version V6_2 = Version.create(6, 2);

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
    this.parser = EsqlParserBuilder.createParser();
  }

  @VisibleForTesting
  protected void analyseFiles(
    SensorContext context, List<TreeVisitor> treeVisitors, Iterable<CompatibleInputFile> inputFiles,
    ProductDependentExecutor executor, ProgressReport progressReport
  ) {
    boolean success = false;
    try {
      for (CompatibleInputFile inputFile : inputFiles) {
        // check for cancellation of the analysis (by SonarQube or SonarLint). See SONARJS-761.
        if (context.getSonarQubeVersion().isGreaterThanOrEqual(V6_0) && context.isCancelled()) {
          throw new CancellationException("Analysis interrupted because the SensorContext is in cancelled state");
        }
				analyse(context, inputFile, executor, treeVisitors);
		        progressReport.nextFile();
      }
      success = true;
    } catch (CancellationException e) {
      // do not propagate the exception
      LOG.debug(e.toString());
    } finally {
      stopProgressReport(progressReport, success);
    }
  }

  private static void stopProgressReport(ProgressReport progressReport, boolean success) {
    if (success) {
      progressReport.stop();
    } else {
      progressReport.cancel();
    }
  }

  private void analyse(SensorContext sensorContext, CompatibleInputFile inputFile, ProductDependentExecutor executor, List<TreeVisitor> visitors) {
    ProgramTree programTree;

    try {
      programTree = (ProgramTree) parser.parse(inputFile.contents());
      scanFile(sensorContext, inputFile, executor, visitors, programTree);
    } catch (RecognitionException e) {
      checkInterrupted(e);
      LOG.error("Unable to parse file: " + inputFile.absolutePath());
      LOG.error(e.getMessage());
      processRecognitionException(e, sensorContext, inputFile);
    } catch (Exception e) {
      checkInterrupted(e);
      processException(e, sensorContext, inputFile);
      throw new AnalysisException("Unable to analyse file: " + inputFile.absolutePath(), e);
    }
  }

  private static void checkInterrupted(Exception e) {
    Throwable cause = Throwables.getRootCause(e);
    if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
      throw new AnalysisException("Analysis cancelled", e);
    }
  }

  private void processRecognitionException(RecognitionException e, SensorContext sensorContext, CompatibleInputFile inputFile) {
    if (parsingErrorRuleKey != null) {
      NewIssue newIssue = sensorContext.newIssue();

      NewIssueLocation primaryLocation = newIssue.newLocation()
        .message(e.getMessage())
        .on(inputFile.wrapped())
        .at(inputFile.selectLine(e.getLine()));

      newIssue
        .forRule(parsingErrorRuleKey)
        .at(primaryLocation)
        .save();
    }

    if (sensorContext.getSonarQubeVersion().isGreaterThanOrEqual(V6_0)) {
      sensorContext.newAnalysisError()
        .onFile(inputFile.wrapped())
        .at(inputFile.newPointer(e.getLine(), 0))
        .message(e.getMessage())
        .save();
    }
  }

  private static void processException(Exception e, SensorContext sensorContext, CompatibleInputFile inputFile) {
    if (sensorContext.getSonarQubeVersion().isGreaterThanOrEqual(V6_0)) {
      sensorContext.newAnalysisError()
        .onFile(inputFile.wrapped())
        .message(e.getMessage())
        .save();
    }
  }

  private void scanFile(SensorContext sensorContext, CompatibleInputFile inputFile, ProductDependentExecutor executor, List<TreeVisitor> visitors, ProgramTree programTree) {
    EsqlVisitorContext context = new EsqlVisitorContext(programTree, inputFile, sensorContext.settings());

    List<Issue> fileIssues = new ArrayList<>();

    for (TreeVisitor visitor : visitors) {
      if (visitor instanceof EsqlCheck) {
        fileIssues.addAll(((EsqlCheck) visitor).scanFile(context));
      } else {
        visitor.scanTree(context);
      }
    }

    saveFileIssues(sensorContext, fileIssues, inputFile.wrapped());
    executor.highlightSymbols(inputFile.wrapped(), context);
  }

  private void saveFileIssues(SensorContext sensorContext, List<Issue> fileIssues, InputFile inputFile) {
    for (Issue issue : fileIssues) {
      RuleKey ruleKey = ruleKey(issue.check());
      if (issue instanceof FileIssue) {
        saveFileIssue(sensorContext, inputFile, ruleKey, (FileIssue) issue);
      } else if (issue instanceof LineIssue) {
        saveLineIssue(sensorContext, inputFile, ruleKey, (LineIssue) issue);
      } else {
        savePreciseIssue(sensorContext, inputFile, ruleKey, (PreciseIssue) issue);
      }
    }
  }

  private static void savePreciseIssue(SensorContext sensorContext, InputFile inputFile, RuleKey ruleKey, PreciseIssue issue) {
    NewIssue newIssue = sensorContext.newIssue();

    newIssue
      .forRule(ruleKey)
      .at(newLocation(inputFile, newIssue, issue.primaryLocation()));

    if (issue.cost() != null) {
      newIssue.gap(issue.cost());
    }

    for (IssueLocation secondary : issue.secondaryLocations()) {
      newIssue.addLocation(newLocation(inputFile, newIssue, secondary));
    }
    newIssue.save();
  }

  private static NewIssueLocation newLocation(InputFile inputFile, NewIssue issue, IssueLocation location) {
    TextRange range = inputFile.newRange(
      location.startLine(), location.startLineOffset(), location.endLine(), location.endLineOffset());

    NewIssueLocation newLocation = issue.newLocation()
      .on(inputFile)
      .at(range);

    if (location.message() != null) {
      newLocation.message(location.message());
    }
    return newLocation;
  }

  private RuleKey ruleKey(EsqlCheck check) {
    Preconditions.checkNotNull(check);
    RuleKey ruleKey = checks.ruleKeyFor(check);
    if (ruleKey == null) {
      throw new IllegalStateException("No rule key found for a rule");
    }
    return ruleKey;
  }


  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
      .onlyOnLanguage(EsqlLanguage.KEY)
      .name("ESQL Squid Sensor")
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

  private ProductDependentExecutor createProductDependentExecutor(SensorContext context) {
    if (isSonarLint(context)) {
      return new SonarLintProductExecutor();
    }
    return new SonarQubeProductExecutor(context, noSonarFilter, fileLinesContextFactory);
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
        context.settings().getBoolean(EsqlPlugin.IGNORE_HEADER_COMMENTS),
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
