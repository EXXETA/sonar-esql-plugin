package com.exxeta.iss.sonar.esql.compat;

import org.sonar.api.batch.fs.InputFile;

public class CompatibilityHelper {

	public static CompatibleInputFile wrap(InputFile inputFile) {
		return new CompatibleInputFile(inputFile);
	}

	
}
