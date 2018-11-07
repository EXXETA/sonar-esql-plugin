package com.exxeta.iss.sonar.msgflow.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

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
			assertNotNull(parser.parse(testFile));
		}

	}

	@Test
	public void parse() throws IOException {
		File testFile = new File("src/test/resources/AggregateControl.msgflow");
		System.out.println("Parsing " + testFile.getAbsolutePath());
		MsgflowParser parser = MsgflowParserBuilder.createParser();
		Msgflow msgflow = parser.parse(testFile);
		assertNotNull(msgflow);
		assertThat(msgflow.getAggregateControlNodes()).isNotEmpty();
	}
}
