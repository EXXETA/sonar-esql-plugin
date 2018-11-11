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
import org.sonar.api.config.Configuration;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.squidbridge.ProgressReport;
import org.sonar.squidbridge.api.AnalysisException;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitor;
import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.esql.check.ParsingErrorCheck;
import com.exxeta.iss.sonar.iib.IibPlugin;
import com.exxeta.iss.sonar.msgflow.api.CustomMsgflowRulesDefinition;
import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.visitors.Issue;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.exxeta.iss.sonar.msgflow.check.MsgflowCheckList;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParser;
import com.exxeta.iss.sonar.msgflow.parser.MsgflowParserBuilder;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowImpl;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.RecognitionException;

public class MsgflowSensor implements Sensor {
	@VisibleForTesting
	protected interface ProductDependentExecutor {
		void executeCoverageSensors();

		List<MsgflowVisitor> getProductDependentTreeVisitors();

	}

	private static class SonarQubeProductExecutor implements ProductDependentExecutor {
		private static void executeCoverageSensors(final SensorContext context,
				final Map<InputFile, Set<Integer>> executableLines) {
			final Configuration configuration = context.config();

			final String[] traces = configuration.getStringArray(IibPlugin.TRACE_PATHS_PROPERTY);

		}

		private final SensorContext context;
		// private MetricsVisitor metricsVisitor;

		SonarQubeProductExecutor(final SensorContext context) {
			this.context = context;
		}

		@Override
		public void executeCoverageSensors() {
			/*
			 * if (metricsVisitor == null) { throw new
			 * IllegalStateException("Before starting coverage computation, metrics should have been calculated."
			 * ); } executeCoverageSensors(context, metricsVisitor.executableLines());
			 */
		}

		@Override
		public List<MsgflowVisitor> getProductDependentTreeVisitors() {

			// metricsVisitor = new MetricsVisitor(
			// context,
			// fileLinesContextFactory);
			return Arrays.asList(
			// metricsVisitor,
			// new NoSonarVisitor(noSonarFilter),
			// new CpdVisitor(context)
			);
		}

	}

	private static final Logger LOG = Loggers.get(MsgflowSensor.class);

	private static void checkInterrupted(final Exception e) {
		final Throwable cause = Throwables.getRootCause(e);
		if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
			throw new AnalysisException("Analysis cancelled", e);
		}
	}

	private static void processException(final Exception e, final SensorContext sensorContext,
			final InputFile inputFile) {
		sensorContext.newAnalysisError().onFile(inputFile).message(e.getMessage()).save();
	}

	private static void stopProgressReport(final ProgressReport progressReport, final boolean success) {
		if (success) {
			progressReport.stop();
		} else {
			progressReport.cancel();
		}
	}

	private final MsgflowChecks checks;

	private final FileSystem fileSystem;

	private final FilePredicate mainFilePredicate;

	private final MsgflowParser parser;

	// parsingErrorRuleKey equals null if ParsingErrorCheck is not activated
	private RuleKey parsingErrorRuleKey = null;

	public MsgflowSensor(final CheckFactory checkFactory, final FileSystem fileSystem) {
		this(checkFactory, fileSystem, null);
	}

	public MsgflowSensor(final CheckFactory checkFactory, final FileSystem fileSystem,
			@Nullable final CustomMsgflowRulesDefinition[] customRulesDefinition) {

		checks = MsgflowChecks.createMsgflowCheck(checkFactory)
				.addChecks(CheckList.REPOSITORY_KEY, MsgflowCheckList.getChecks())
				.addCustomChecks(customRulesDefinition);
		this.fileSystem = fileSystem;
		mainFilePredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(MsgflowLanguage.KEY));
		parser = MsgflowParserBuilder.createParser();
	}

	private void analyse(final SensorContext sensorContext, final InputFile inputFile,
			final ProductDependentExecutor executor, final List<TreeVisitor> visitors) {
		ProgramTree programTree;

		try {
			programTree = (ProgramTree) parser.parse(inputFile.contents());
			scanFile(sensorContext, inputFile, executor, visitors, programTree);
		} catch (final RecognitionException e) {
			checkInterrupted(e);
			LOG.error("Unable to parse file: " + inputFile.uri());
			LOG.error(e.getMessage());
			processRecognitionException(e, sensorContext, inputFile);
		} catch (final Exception e) {
			checkInterrupted(e);
			processException(e, sensorContext, inputFile);
			throw new AnalysisException("Unable to analyse file: " + inputFile.uri(), e);
		}
	}

	@VisibleForTesting
	protected void analyseFiles(final SensorContext context, final List<TreeVisitor> treeVisitors,
			final Iterable<InputFile> inputFiles, final ProductDependentExecutor executor,
			final ProgressReport progressReport) {
		boolean success = false;
		try {
			for (final InputFile inputFile : inputFiles) {
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
		} catch (final CancellationException e) {
			// do not propagate the exception
			LOG.debug(e.toString(), e);
		} finally {
			stopProgressReport(progressReport, success);
		}
	}

	private ProductDependentExecutor createProductDependentExecutor(final SensorContext context) {
		return new SonarQubeProductExecutor(context);
	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.onlyOnLanguage(MsgflowLanguage.KEY).name("Msgflow Squid Sensor").onlyOnFileType(Type.MAIN);
	}

	@Override
	public void execute(final SensorContext context) {
		final ProductDependentExecutor executor = createProductDependentExecutor(context);
		final List<TreeVisitor> treeVisitors = Lists.newArrayList();
		treeVisitors.addAll(executor.getProductDependentTreeVisitors());
		treeVisitors.addAll(checks.visitorChecks());

		for (final TreeVisitor check : treeVisitors) {
			if (check instanceof ParsingErrorCheck) {
				parsingErrorRuleKey = checks.ruleKeyFor((MsgflowCheck) check);
				break;
			}
		}

		final Iterable<InputFile> inputFiles = fileSystem.inputFiles(mainFilePredicate);
		final Collection<File> files = StreamSupport.stream(inputFiles.spliterator(), false).map(InputFile::file)
				.collect(Collectors.toList());

		final ProgressReport progressReport = new ProgressReport("Report about progress of ESQL analyzer",
				TimeUnit.SECONDS.toMillis(10));
		progressReport.start(files);

		analyseFiles(context, treeVisitors, inputFiles, executor, progressReport);

		// executor.executeCoverageSensors();
	}

	private void processRecognitionException(final RecognitionException e, final SensorContext sensorContext,
			final InputFile inputFile) {
		if (parsingErrorRuleKey != null) {
			final NewIssue newIssue = sensorContext.newIssue();

			final NewIssueLocation primaryLocation = newIssue.newLocation().message(ParsingErrorCheck.MESSAGE)
					.on(inputFile).at(inputFile.selectLine(e.getLine()));

			newIssue.forRule(parsingErrorRuleKey).at(primaryLocation).save();
		}

		sensorContext.newAnalysisError().onFile(inputFile).at(inputFile.newPointer(e.getLine(), 0))
				.message(e.getMessage()).save();
	}

	private void scanFile(final SensorContext sensorContext, final InputFile inputFile,
			final ProductDependentExecutor executor, final List<MsgflowVisitor> visitors, final MsgflowImpl msgflow) {
		final MsgflowVisitorContext context = new MsgflowVisitorContext(msgflow, inputFile);

		final List<Issue> fileIssues = new ArrayList<>();

		for (final MsgflowVisitor visitor : visitors) {
			if (visitor instanceof MsgflowCheck) {
				fileIssues.addAll(((MsgflowCheck) visitor).scanFile(context));
			} else {
				visitor.scanTree(context);
			}
		}

		saveFileIssues(sensorContext, fileIssues, inputFile);
	}

}
