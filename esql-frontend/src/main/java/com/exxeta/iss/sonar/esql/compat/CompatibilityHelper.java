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
