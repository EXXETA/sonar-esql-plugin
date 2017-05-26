package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

import static org.junit.Assert.assertTrue;

public class HardcodedURICheckTest {

	@Test
	public void test(){
		 EsqlCheckVerifier.verify(new HardcodedURICheck(), new File("src/test/resources/hardCodedURI.esql"));
	}
	
	@Test
	public void regexTest(){
		assertTrue(HardcodedURICheck.PATH_DELIMETERS_PATTERN.matcher("'/'").find());
		assertTrue(HardcodedURICheck.PATH_DELIMETERS_PATTERN.matcher("'//'").find());
		assertTrue(HardcodedURICheck.PATH_DELIMETERS_PATTERN.matcher("'\\'").find());
		assertTrue(HardcodedURICheck.PATH_DELIMETERS_PATTERN.matcher("'\\\\'").find());
	}
	
}
