/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.compat;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.Version;

public class CompatibilityHelper {
	public static final Version V6_0 = Version.create(6, 0);
	public static final Version V6_2 = Version.create(6, 2);

	public static CompatibleInputFile wrap(InputFile inputFile) {
		return new CompatibleInputFile(inputFile);
	}

	public static Collection<CompatibleInputFile> wrap(Iterable<InputFile> inputFiles, SensorContext context) {
		Version version = context.getSonarQubeVersion();
		if (version.isGreaterThanOrEqual(V6_2)) {
			return inputFileStream(inputFiles).map(CompatibleInputFile::new).collect(Collectors.toList());
		}
		if (version.isGreaterThanOrEqual(V6_0)) {
			return inputFileStream(inputFiles).map(InputFileV60Compat::new).collect(Collectors.toList());
		}
		return inputFileStream(inputFiles).map(f -> new InputFileV56Compat(f, context)).collect(Collectors.toList());
	}

	private static Stream<InputFile> inputFileStream(Iterable<InputFile> inputFiles) {
		return StreamSupport.stream(inputFiles.spliterator(), false);
	}

}
