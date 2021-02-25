package com.exxeta.iss.sonar.esql.codecoverage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;

import com.exxeta.iss.sonar.esql.codecoverage.TraceSensor.UnitTestsAnalyzer;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;

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
public class TraceSensorTest {
	private SensorContextTester context;
	private MapSettings settings;

	private File moduleBaseDir = new File("src/test/resources/codecoverage/");

	private Map<InputFile, Set<Integer>> linesOfCode;

	private TraceSensor sensor;

	@Before
	public void init() throws FileNotFoundException {
		settings = new MapSettings();
		settings.setProperty("sonar.esql.trace.reportPaths", "TraceSensorTest/trace.txt");
		context = SensorContextTester.create(moduleBaseDir);
		context.setSettings(settings);

		InputFile inputFile1 = inputFile("file1.esql", Type.MAIN);
		InputFile inputFile2 = inputFile("file2.esql", Type.MAIN);
		InputFile inputFile3 = inputFile("file3.esql", Type.MAIN);
		// inputFile("tests/file1.esql", Type.TEST);

		linesOfCode = new HashMap<>();
		linesOfCode.put(inputFile1, ImmutableSet.of(1, 2, 3, 4));
		linesOfCode.put(inputFile2, null);
		linesOfCode.put(inputFile3, ImmutableSet.of(1, 2, 3, 4));

		sensor = new TraceSensor();

	}

	private InputFile inputFile(String relativePath, Type type) throws FileNotFoundException {
		DefaultInputFile inputFile = new TestInputFileBuilder("moduleKey", relativePath)
				.setModuleBaseDir(moduleBaseDir.toPath()).setLanguage("esql").setType(type).setCharset(Charsets.UTF_8)
				.build();

		inputFile.setMetadata(new FileMetadata().readMetadata(new FileReader(inputFile.file())));
		context.fileSystem().add(inputFile);

		return inputFile;
	}

	@Test
	public void report_not_found() throws Exception {

		sensor.execute(context, linesOfCode, new String[] { "/fake/path/trace.txt" });

		assertThat(context.lineHits("moduleKey:file1.js", 1)).isNull();
	}

	@Test
	public void folder_test() {
		sensor.execute(context, linesOfCode, new String[] { "TraceSensorTest" });
		assertThat(sensor.toString()).isEqualTo("TraceSensor");
		assertThat(context.conditions("moduleKey:file1.esql", 1)).isEqualTo(1);
	}

	@Test
	public void test_overall_coverage() {
		sensor.execute(context, linesOfCode, new String[] { "TraceSensorTest/trace.txt" });

		Integer[] file1Expected = { 3, 3, 1, null };
		Integer[] file2Expected = { 5, 5, null, null };

		assertThat(context.conditions("moduleKey:file1.esql", 1)).isEqualTo(1);
		assertThat(context.coveredConditions("moduleKey:file1.esql", 2)).isEqualTo(0);
		assertThat(context.lineHits("moduleKey:file1.esql", 3)).isEqualTo(0);
		assertThat(context.lineHits("moduleKey:file1.esql", 4)).isEqualTo(1);
	}

	@Test
	public void regex_test() {
		UnitTestsAnalyzer analyzer = new UnitTestsAnalyzer(null, null);
		// (?i)\\s*CREATE\\s+(COMPUTE|FILTER|DATABASE)\\s+MODULE\\s+(.+)

		checkPattern(analyzer.pathPattern, "  BROKER SCHEMA a PATH x.y.z, r.t.z", 1, "a");
		checkPattern(analyzer.pathPattern, "BROKER SCHEMA foo.bar.test PATH for.bar2", 1, "foo.bar.test");
		checkPattern(analyzer.pathPattern, "BROKER SCHEMA foo.bar.test", 1, "foo.bar.test");
		checkPattern(analyzer.pathPattern, "BROKER SCHEMA foo.bar.test   ", 1, "foo.bar.test");
		checkPattern(analyzer.modulePattern, "CREATE COMPUTE MODULE helloWorld", 2, "helloWorld");
		checkPattern(analyzer.routinePattern, "CREATE FUNCTION aaa BEGIN", 2, "aaa");
		checkPattern(analyzer.endModulePattern, "END MODULE;", 0, "END MODULE;");

	}

	private void checkPattern(Pattern pattern, String input, int group, String expectedResult) {
		Matcher matcher = pattern.matcher(input);
		assertThat(matcher.matches()).isTrue();
		assertThat(matcher.group(group)).isEqualTo(expectedResult);
	}

}
