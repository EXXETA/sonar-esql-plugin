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
package com.exxeta.iss.sonar.iib.esql;

import com.exxeta.iss.sonar.esql.api.CustomEsqlRulesDefinition;
import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.esql.check.ParsingErrorCheck;
import com.exxeta.iss.sonar.esql.codecoverage.TraceSensor;
import com.exxeta.iss.sonar.esql.cpd.CpdVisitor;
import com.exxeta.iss.sonar.esql.highlighter.HighlightSymbolTableBuilder;
import com.exxeta.iss.sonar.esql.highlighter.HighlighterVisitor;
import com.exxeta.iss.sonar.esql.metrics.MetricsVisitor;
import com.exxeta.iss.sonar.esql.metrics.NoSonarVisitor;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.exxeta.iss.sonar.iib.IibPlugin;
import com.exxeta.iss.sonar.iib.esql.EsqlChecks;
import com.exxeta.iss.sonar.iib.esql.EsqlLanguage;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.api.batch.InstantiationStrategy;
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
import org.sonar.api.config.Configuration;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.squidbridge.ProgressReport;

import javax.annotation.Nullable;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ScannerSide
@InstantiationStrategy(InstantiationStrategy.PER_PROJECT)
public class EsqlSensor implements Sensor {

    private static final Logger LOG = Loggers.get(EsqlSensor.class);

    private final EsqlChecks checks;
    private final FileLinesContextFactory fileLinesContextFactory;
    private final FileSystem fileSystem;
    private final NoSonarFilter noSonarFilter;
    private final FilePredicate mainFilePredicate;
    private final ActionParser<Tree> parser;
    // parsingErrorRuleKey equals null if ParsingErrorCheck is not activated
    private RuleKey parsingErrorRuleKey = null;
	private MetricsVisitor metricsVisitor = null;

    public EsqlSensor(
            CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory, FileSystem fileSystem, NoSonarFilter noSonarFilter) {
        this(checkFactory, fileLinesContextFactory, fileSystem, noSonarFilter, null);
    }


    public EsqlSensor(
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
            SensorContext context, List<TreeVisitor> treeVisitors, Iterable<InputFile> inputFiles,
            ProgressReport progressReport
    ) {
        boolean success = false;
        try {
            for (InputFile inputFile : inputFiles) {
                // check for cancellation of the analysis (by SonarQube or SonarLint). See SONARJS-761.
                if (context.isCancelled()) {
                    throw new CancellationException("Analysis interrupted because the SensorContext is in cancelled state");
                }
                analyse(context, inputFile, treeVisitors);
                progressReport.nextFile();
            }
            success = true;
        } catch (CancellationException e) {
            // do not propagate the exception
            LOG.debug("Error while file analysis in code coverage" + e.toString(), e);
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

    private void analyse(SensorContext sensorContext, InputFile inputFile, List<TreeVisitor> visitors) {
        ProgramTree programTree;

        try {
            programTree = (ProgramTree) parser.parse(inputFile.contents());
            scanFile(sensorContext, inputFile, visitors, programTree);
        } catch (RecognitionException e) {
            checkInterrupted(e);
            LOG.error("Unable to parse file: " + inputFile.uri());
            LOG.error(e.getMessage());
            processRecognitionException(e, sensorContext, inputFile);
        } catch (Exception e) {
            checkInterrupted(e);
            processException(e, sensorContext, inputFile);
            LOG.error("Unable to analyse file: " + inputFile.uri(), e);
        }
    }

    private static void checkInterrupted(Exception e) {
        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
            throw new AnalysisException("Analysis cancelled", e);
        }
    }

    private void processRecognitionException(RecognitionException e, SensorContext sensorContext, InputFile inputFile) {
        if (parsingErrorRuleKey != null) {
            NewIssue newIssue = sensorContext.newIssue();

            NewIssueLocation primaryLocation = newIssue.newLocation()
                    .message(ParsingErrorCheck.MESSAGE)
                    .on(inputFile)
                    .at(inputFile.selectLine(e.getLine()));

            newIssue
                    .forRule(parsingErrorRuleKey)
                    .at(primaryLocation)
                    .save();
        }

        sensorContext.newAnalysisError()
                .onFile(inputFile)
                .at(inputFile.newPointer(e.getLine(), 0))
                .message(e.getMessage())
                .save();
    }

    private static void processException(Exception e, SensorContext sensorContext, InputFile inputFile) {
        sensorContext.newAnalysisError()
                .onFile(inputFile)
                .message(e.getMessage())
                .save();
    }

    private void scanFile(SensorContext sensorContext, InputFile inputFile, List<TreeVisitor> visitors, ProgramTree programTree) {
        LOG.debug("scanning file " + inputFile.filename());
        EsqlVisitorContext context = new EsqlVisitorContext(programTree, inputFile, sensorContext.config());

        List<Issue> fileIssues = new ArrayList<>();

        for (TreeVisitor visitor : visitors) {
            if (visitor instanceof EsqlCheck) {
                fileIssues.addAll(((EsqlCheck) visitor).scanFile(context));
            } else {
                visitor.scanTree(context);
            }
        }

        saveFileIssues(sensorContext, fileIssues, inputFile);
        highlightSymbols(inputFile, context, sensorContext);
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

    public void execute(SensorContext context) {
        LOG.warn("ESQL sensor execute");
        List<TreeVisitor> treeVisitors = Lists.newArrayList();
        treeVisitors.addAll(getTreeVisitors(context));
        treeVisitors.addAll(checks.visitorChecks());

        for (TreeVisitor check : treeVisitors) {
            if (check instanceof ParsingErrorCheck) {
                parsingErrorRuleKey = checks.ruleKeyFor((EsqlCheck) check);
                break;
            }
        }

        Iterable<InputFile> inputFiles = fileSystem.inputFiles(mainFilePredicate);
        Collection<String> files = StreamSupport.stream(inputFiles.spliterator(), false)
                .map(InputFile::toString)
                .collect(Collectors.toList());

        ProgressReport progressReport = new ProgressReport("Report about progress of ESQL analyzer", TimeUnit.SECONDS.toMillis(10));
        progressReport.start(files);

        analyseFiles(context, treeVisitors, inputFiles, progressReport);

        executeCoverageSensors(context);
    }


    public List<TreeVisitor> getTreeVisitors(SensorContext context) {
        boolean ignoreHeaderComments = ignoreHeaderComments(context);

        metricsVisitor = new MetricsVisitor(
                context,
                ignoreHeaderComments,
                fileLinesContextFactory);
        return Arrays.asList(
                metricsVisitor,
                new NoSonarVisitor(noSonarFilter, ignoreHeaderComments),
                new HighlighterVisitor(context),
                new CpdVisitor(context));
    }

    public void highlightSymbols(InputFile inputFile, TreeVisitorContext treeVisitorContext, SensorContext context) {
        NewSymbolTable newSymbolTable = context.newSymbolTable().onFile(inputFile);
        HighlightSymbolTableBuilder.build(newSymbolTable, treeVisitorContext);
    }

    public void executeCoverageSensors(SensorContext context) {
    //Removed re-initialization of metriccvisitor as executable lines were empty
      if (metricsVisitor == null) {
	  	LOG.debug("metricVisitor null");
      } else {
         executeCoverageSensors(context, metricsVisitor.executableLines());
      }
    }

    private static void executeCoverageSensors(SensorContext context, Map<InputFile, Set<Integer>> executableLines) {
        Configuration configuration = context.config();

        String[] traces = configuration.getStringArray(IibPlugin.TRACE_PATHS_PROPERTY);

        (new TraceSensor()).execute(context, executableLines, traces);

    }

    private static boolean ignoreHeaderComments(SensorContext context) {
        return context.config().getBoolean(IibPlugin.IGNORE_HEADER_COMMENTS).orElse(IibPlugin.IGNORE_HEADER_COMMENTS_DEFAULT_VALUE);
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

    static class AnalysisException extends RuntimeException {
        AnalysisException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
