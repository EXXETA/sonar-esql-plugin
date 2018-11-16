package com.exxeta.iss.sonar.msgflow.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MsgflowParserTest {
	@Test
	public void parseAll() throws IOException {
		final File resourcesDir = new File("src/test/resources");
		for (final File testFile : resourcesDir.listFiles()) {
			if (testFile.isFile()) {
				System.out.println("Parsing " + testFile.getAbsolutePath());
				final MsgflowParser parser = MsgflowParserBuilder.createParser();
				assertThat(parser.parse(testFile)).isNotNull();
			}
		}

	}

}
