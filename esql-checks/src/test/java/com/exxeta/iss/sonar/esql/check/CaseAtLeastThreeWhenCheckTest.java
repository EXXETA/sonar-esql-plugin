package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class CaseAtLeastThreeWhenCheckTest {
	@Test
	  public void test() {
		 EsqlCheckVerifier.issues(new CaseAtLeastThreeWhenCheck(), new File("src/test/resources/caseTest.esql"))
	        .next().atLine(5).withMessage("Replace this \"case\" statement by \"if\" statements to increase readability.")
	        .noMore();
	  }
}
