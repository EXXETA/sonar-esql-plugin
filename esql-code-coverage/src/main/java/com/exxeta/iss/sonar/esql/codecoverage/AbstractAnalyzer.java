package com.exxeta.iss.sonar.esql.codecoverage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;

import com.google.common.io.Files;
import static com.exxeta.iss.sonar.esql.codecoverage.CodeCoverageExtension.LOG;

public abstract class AbstractAnalyzer implements ExecutionDataVisitor {

	private Pattern pathPattern = Pattern.compile("(?i)\\s*CREATE\\s+SCHEMA\\s+([\\w\\.]+)\\s+PATH");
	private Pattern modulePattern = Pattern.compile("(?i)\\s*CREATE\\s+(COMPUTE|FILTER|DATABASE)\\s+MODULE\\s+(.+)");
	private Pattern routinePattern = Pattern.compile("(?i)\\s*CREATE\\s+(FUNCTION|PROCEDURE)\\s+([\\w]+)\\W+.*");
	private Pattern endModulePattern = Pattern.compile("(?i)\\s*END\\s+MODULE;.*");

	private HashMap<String, CodePosition> offsetCache = null;
	private HashMap<InputFile, HashSet<Integer>> executedLines = new HashMap<>();
	private final Map<InputFile, Set<Integer>> executableLines;

	public AbstractAnalyzer(Map<InputFile, Set<Integer>> executableLines) {
		this.executableLines = executableLines;
	}

	public final void analyse(SensorContext context) {
		FileSystem fs = context.fileSystem();
		FilePredicates p = fs.predicates();
		Iterable<InputFile> files = fs.inputFiles(p.hasLanguage("esql"));
		if (offsetCache == null) {
			offsetCache = new HashMap<>();
			for (InputFile file : files) {
				fillOffsetCache(file);
			}
		}

		new TraceFileReader(getTrace()).readTrace(this);

		// Create new coverages for all InputFiles
		for (InputFile file : files) {
			NewCoverage coverage = context.newCoverage().onFile(file).ofType(CoverageType.UNIT);
			Set<Integer> fileExecutableLines = executableLines.get(file);
			Set<Integer> fileExecutedLines = executedLines.get(file);
			if (fileExecutableLines != null && fileExecutedLines == null) {
				LOG.info("File has not been executed " + file.absolutePath());
				for (int line : fileExecutableLines) {
					coverage.lineHits(line, 0);
					coverage.conditions(line, 1, 0);
				}
				coverage.save();
			} else if (fileExecutableLines == null) {
				LOG.warn("File has not been parsed " + file.absolutePath());
			} else {
				String lineHits = "";
				for (int line : fileExecutableLines) {
					if (fileExecutedLines.contains(line)) {
						coverage.lineHits(line, 1);
						coverage.conditions(line, 1, 1);
						lineHits+=" "+line;
					} else {
						coverage.lineHits(line, 0);
						coverage.conditions(line, 1, 0);
					}
				}
					LOG.info("Saving execution data found for " + file.absolutePath()+lineHits);
				coverage.save();
			}
		}

	}

	@Override
	public void visitModuleExecution(ModuleExecutionData data) {
		// For each execution try to find the file and the offset of the
		// function
		for (LineExecutionData lineExecution : data.getLineExecutions()) {
			if (offsetCache.containsKey(lineExecution.getFunction())) {
				int line = offsetCache.get(lineExecution.getFunction()).getLine()
						+ Integer.parseInt(lineExecution.getRelativeLine());
				InputFile file = offsetCache.get(lineExecution.getFunction()).getFile();

				if (executedLines.get(file) == null) {
					executedLines.put(file, new HashSet<>());
				}

				executedLines.get(file).add(line);
			} else {
				LOG.warn("No offsetCache for " + lineExecution.getFunction());
			}

		}
	}

	/**
	 * Parse ESQL InputFile and write line-offsets for each routine to the
	 * offsetCache
	 * 
	 * @param file
	 */
	private void fillOffsetCache(InputFile file) {
		String esqlSchema = "";
		String moduleName = "";
		String routineName = "";
		try {
			String contents = Files.toString(file.file(), Charset.defaultCharset());
			int lineNumber = 0;
			for (String line : contents.split("\\r?\\n")) {
				lineNumber++;
				Matcher pathMatcher = pathPattern.matcher(line);
				if (pathMatcher.matches()) {
					esqlSchema = pathMatcher.group(1);
					continue;
				}
				Matcher moduleMatcher = modulePattern.matcher(line);
				if (moduleMatcher.matches()) {
					moduleName = moduleMatcher.group(2).trim();
					continue;
				}
				Matcher endModuleMatcher = endModulePattern.matcher(line);
				if (endModuleMatcher.matches()) {
					moduleName = "";
				}
				Matcher routineMatcher = routinePattern.matcher(line);
				if (routineMatcher.matches()) {
					routineName = routineMatcher.group(2).trim();

					offsetCache.put(getEsqlSchameAndModuleAndFunction(esqlSchema, moduleName, routineName),
							new CodePosition(file, lineNumber - 1));

					LOG.debug("Added offsetData for " + esqlSchema + " " + moduleName + " " + routineName);

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getEsqlSchameAndModuleAndFunction(String esqlSchema, String moduleName, String routineName) {
		String esqlSchemaAndModuleAndFunction;
		if (esqlSchema.length() > 0) {
			if (moduleName.length() > 0) {
				esqlSchemaAndModuleAndFunction = esqlSchema + "." + moduleName + "." + routineName;
			} else {
				esqlSchemaAndModuleAndFunction = esqlSchema + "." + routineName;
			}
		} else {
			if (moduleName.length() > 0) {
				esqlSchemaAndModuleAndFunction = "." + moduleName + "." + routineName;
			} else {
				esqlSchemaAndModuleAndFunction = "." + routineName;
			}
		}
		return esqlSchemaAndModuleAndFunction;
	}

	protected abstract File getTrace();

}
