package com.exxeta.iss.sonar.esql.parser;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ParserTest {

	 @Test
	  public void realLife() {
	    assertThat(EsqlLegacyGrammar.PROGRAM).matches("");
	 }
	 
	 @Test
	  public void bugfixes() throws IOException {
		 File bugfixDir = new File("src/test/resources/bugfixes");
		 for (File testFile : bugfixDir.listFiles()){
			 String content = FileUtils.readFileToString(testFile, Charset.defaultCharset());
			 System.out.println("Parsing "+testFile.getAbsolutePath());
			 assertThat(EsqlLegacyGrammar.PROGRAM).matches(content.trim());
		 }
	    
	 }
	 
	 
	
}
