package com.exxeta.iss.sonar.esql.codecoverage;

import static com.exxeta.iss.sonar.esql.codecoverage.CodeCoverageExtension.LOG;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

public class TraceSensor {

	public TraceSensor() {
	}

	public void execute(SensorContext context, Map<InputFile, Set<Integer>> executableLines,  String[] traces) {
		
		for (String trace : traces) {
			FileSystem fs = context.fileSystem();
			File report = fs.resolvePath(trace);
			if (!report.isFile()) {
					LOG.info("Trace not found: '{}'", trace);
			} else {
				new UnitTestsAnalyzer(report).analyse(context);
			}
		} 
	}



	class UnitTestsAnalyzer extends AbstractAnalyzer {
		private final File trace;

		public UnitTestsAnalyzer(File trace) {
			super();
			this.trace = trace;
		}

		@Override
		protected File getTrace() {
			return trace;
		}

	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
