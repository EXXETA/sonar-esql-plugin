package com.exxeta.iss.sonar.msgflow.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MsgflowParserTest {
	@Test
	public void parseAll() throws IOException {
		File resourcesDir = new File("src/test/resources");
		for (File testFile : resourcesDir.listFiles()) {
			System.out.println("Parsing " + testFile.getAbsolutePath());
			MsgflowParser parser = MsgflowParserBuilder.createParser();
			assertThat(parser.parse(testFile)).isNotNull();
		}

	}

}
