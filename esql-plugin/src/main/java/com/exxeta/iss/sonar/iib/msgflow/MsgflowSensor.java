package com.exxeta.iss.sonar.iib.msgflow;

import java.io.File;
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

import javax.annotation.Nullable;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
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
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.squidbridge.ProgressReport;
import org.sonar.squidbridge.api.AnalysisException;

import com.exxeta.iss.sonar.iib.IibPlugin;
import com.exxeta.iss.sonar.iib.esql.EsqlChecks;
import com.exxeta.iss.sonar.iib.esql.EsqlLanguage;
import com.exxeta.iss.sonar.iib.esql.EsqlSensor;
import com.exxeta.iss.sonar.msgflow.api.CustomMsgflowRulesDefinition;
import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParserBuilder;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;

public class MsgflowSensor implements Sensor {
	private static final Logger LOG = Loggers.get(MsgflowSensor.class);

	private final MsgflowChecks checks;
	private final FileSystem fileSystem;
	private final FilePredicate mainFilePredicate;
	private final ActionParser<Object> parser;
	// parsingErrorRuleKey equals null if ParsingErrorCheck is not activated
	private RuleKey parsingErrorRuleKey = null;

	public MsgflowSensor(CheckFactory checkFactory, FileSystem fileSystem) {
		this(checkFactory, fileSystem, null);
	}

	public MsgflowSensor(CheckFactory checkFactory, FileSystem fileSystem,
			@Nullable CustomMsgflowRulesDefinition[] customRulesDefinition) {

		this.checks = MsgflowChecks.createMsgflowCheck(checkFactory)
				.addChecks(CheckList.REPOSITORY_KEY, CheckList.getChecks()).addCustomChecks(customRulesDefinition);
		this.fileSystem = fileSystem;
		this.mainFilePredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(MsgflowLanguage.KEY));
		this.parser = MsgflowParserBuilder.createParser();
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.onlyOnLanguage(MsgflowLanguage.KEY).name("Msgflow Squid Sensor").onlyOnFileType(Type.MAIN);
	}

	@Override
	public void execute(SensorContext context) {
		ProductDependentExecutor executor = createProductDependentExecutor(context);
		List<TreeVisitor> treeVisitors = Lists.newArrayList();
		treeVisitors.addAll(executor.getProductDependentTreeVisitors());
		treeVisitors.addAll(checks.visitorChecks());

		for (TreeVisitor check : treeVisitors) {
			if (check instanceof ParsingErrorCheck) {
				parsingErrorRuleKey = checks.ruleKeyFor((MsgflowCheck) check);
				break;
			}
		}

		Iterable<InputFile> inputFiles = fileSystem.inputFiles(mainFilePredicate);
		Collection<File> files = StreamSupport.stream(inputFiles.spliterator(), false).map(InputFile::file)
				.collect(Collectors.toList());

		ProgressReport progressReport = new ProgressReport("Report about progress of ESQL analyzer",
				TimeUnit.SECONDS.toMillis(10));
		progressReport.start(files);

		analyseFiles(context, treeVisitors, inputFiles, executor, progressReport);

		// executor.executeCoverageSensors();
	}

	private ProductDependentExecutor createProductDependentExecutor(SensorContext context) {
		return new SonarQubeProductExecutor(context);
	}

	@VisibleForTesting
	protected interface ProductDependentExecutor {
		List<TreeVisitor> getProductDependentTreeVisitors();

		void highlightSymbols(InputFile inputFile, TreeVisitorContext treeVisitorContext);

		void executeCoverageSensors();
	}

	private void analyse(SensorContext sensorContext, InputFile inputFile, ProductDependentExecutor executor,
			List<TreeVisitor> visitors) {
		ProgramTree programTree;

		try {
			programTree = (ProgramTree) parser.parse(inputFile.contents());
			scanFile(sensorContext, inputFile, executor, visitors, programTree);
		} catch (RecognitionException e) {
			checkInterrupted(e);
			LOG.error("Unable to parse file: " + inputFile.uri());
			LOG.error(e.getMessage());
			processRecognitionException(e, sensorContext, inputFile);
		} catch (Exception e) {
			checkInterrupted(e);
			processException(e, sensorContext, inputFile);
			throw new AnalysisException("Unable to analyse file: " + inputFile.uri(), e);
		}
	}

	private static void processException(Exception e, SensorContext sensorContext, InputFile inputFile) {
		sensorContext.newAnalysisError().onFile(inputFile).message(e.getMessage()).save();
	}

	private void processRecognitionException(RecognitionException e, SensorContext sensorContext, InputFile inputFile) {
		if (parsingErrorRuleKey != null) {
			NewIssue newIssue = sensorContext.newIssue();

			NewIssueLocation primaryLocation = newIssue.newLocation().message(ParsingErrorCheck.MESSAGE).on(inputFile)
					.at(inputFile.selectLine(e.getLine()));

			newIssue.forRule(parsingErrorRuleKey).at(primaryLocation).save();
		}

		sensorContext.newAnalysisError().onFile(inputFile).at(inputFile.newPointer(e.getLine(), 0))
				.message(e.getMessage()).save();
	}

	private static void checkInterrupted(Exception e) {
		Throwable cause = Throwables.getRootCause(e);
		if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
			throw new AnalysisException("Analysis cancelled", e);
		}
	}

	private void scanFile(SensorContext sensorContext, InputFile inputFile, ProductDependentExecutor executor,
			List<TreeVisitor> visitors, ProgramTree programTree) {
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
		executor.highlightSymbols(inputFile, context);
	}

	@VisibleForTesting
	protected void analyseFiles(SensorContext context, List<TreeVisitor> treeVisitors, Iterable<InputFile> inputFiles,
			ProductDependentExecutor executor, ProgressReport progressReport) {
		boolean success = false;
		try {
			for (InputFile inputFile : inputFiles) {
				// check for cancellation of the analysis (by SonarQube or SonarLint). See
				// SONARJS-761.
				if (context.isCancelled()) {
					throw new CancellationException(
							"Analysis interrupted because the SensorContext is in cancelled state");
				}
				analyse(context, inputFile, executor, treeVisitors);
				progressReport.nextFile();
			}
			success = true;
		} catch (CancellationException e) {
			// do not propagate the exception
			LOG.debug(e.toString(), e);
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

	private static class SonarQubeProductExecutor implements ProductDependentExecutor {
		private final SensorContext context;
		// private MetricsVisitor metricsVisitor;

		SonarQubeProductExecutor(SensorContext context) {
			this.context = context;
		}

		@Override
		public List<TreeVisitor> getProductDependentTreeVisitors() {

			// metricsVisitor = new MetricsVisitor(
			// context,
			// fileLinesContextFactory);
			return Arrays.asList(
					// metricsVisitor,
					// new NoSonarVisitor(noSonarFilter),
					new CpdVisitor(context));
		}

		@Override
		public void highlightSymbols(InputFile inputFile, TreeVisitorContext treeVisitorContext) {
			NewSymbolTable newSymbolTable = context.newSymbolTable().onFile(inputFile);
			HighlightSymbolTableBuilder.build(newSymbolTable, treeVisitorContext);
		}

		@Override
		public void executeCoverageSensors() {
			/*
			 * if (metricsVisitor == null) { throw new
			 * IllegalStateException("Before starting coverage computation, metrics should have been calculated."
			 * ); } executeCoverageSensors(context, metricsVisitor.executableLines());
			 */
		}

		private static void executeCoverageSensors(SensorContext context,
				Map<InputFile, Set<Integer>> executableLines) {
			Configuration configuration = context.config();

			String[] traces = configuration.getStringArray(IibPlugin.TRACE_PATHS_PROPERTY);

			(new TraceSensor()).execute(context, executableLines, traces);

		}

	}

}
