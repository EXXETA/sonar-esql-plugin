package com.exxeta.iss.sonar.msgflow.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.exxeta.iss.sonar.msgflow.parser.node.AbstractNodeParser;

public class ParserListsTest {

	/**
	 * Enforces that each parser is declared in list.
	 */
	@Test
	public void count() {
		int count = 0;
		List<File> files = (List<File>) FileUtils.listFiles(new File("src/main/java/com/exxeta/iss/sonar/msgflow/parser/node/"),
				new String[] { "java" }, true);
		for (File file : files) {
			 String name = file.getName();
		      if (name.endsWith("Parser.java") && !name.startsWith("Abstract")) {
				count++;
			}
		}
		assertThat(ParserLists.getNodeParsers().size()).isEqualTo(count);
	}
	
	/**
	 * Enforces that each check has a test.
	 */
	@Test
	public void test() {

		
		
		List<AbstractNodeParser<?>> parsers = ParserLists.getNodeParsers();

	    for (AbstractNodeParser<?> parser : parsers) {
	        String testName = '/' + parser.getClass().getName().replace('.', '/') + "Test.class";
	        assertThat(getClass().getResource(testName))
	          .overridingErrorMessage("No test for " + parser.getClass().getSimpleName())
	          .isNotNull();
	    }

	    
	}
	
}
