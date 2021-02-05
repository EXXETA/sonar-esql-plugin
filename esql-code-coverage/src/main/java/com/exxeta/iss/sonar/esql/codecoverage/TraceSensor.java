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
package com.exxeta.iss.sonar.esql.codecoverage;

import static com.exxeta.iss.sonar.esql.codecoverage.CodeCoverageExtension.LOG;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

public class TraceSensor {

	public void execute(SensorContext context, Map<InputFile, Set<Integer>> executableLines,  String[] traces) {
		
		for (String trace : traces) {
			FileSystem fs = context.fileSystem();
			File report = fs.resolvePath(trace);
			if (report.isFile()) {
				new UnitTestsAnalyzer(report, executableLines).analyse(context);
			}else if(report.isDirectory()){
				for (File reportFile:report.listFiles()){
					new UnitTestsAnalyzer(reportFile, executableLines).analyse(context);
				}
			} else {
				LOG.info("Trace not found: '{}'", trace);
			}
		} 
	}



	static class UnitTestsAnalyzer extends AbstractAnalyzer {
		private final File trace;
		

		public UnitTestsAnalyzer(File trace, Map<InputFile, Set<Integer>> executableLines) {
			super(executableLines);
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
