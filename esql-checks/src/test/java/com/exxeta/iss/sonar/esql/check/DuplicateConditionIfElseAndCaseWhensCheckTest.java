package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class DuplicateConditionIfElseAndCaseWhensCheckTest {
	@Test
	  public void caseTest() {
		 EsqlCheckVerifier.issues(new DuplicateConditionIfElseAndCaseWhensCheck(), new File("src/test/resources/caseTest.esql"))
	        .next().atLine(8).withMessage("This WHEN duplicates the one on line 6.")
	        .next().atLine(20).withMessage("This WHEN duplicates the one on line 18.")
	        .noMore();
	  }
	@Test
	  public void ifTest() {
		 EsqlCheckVerifier.issues(new DuplicateConditionIfElseAndCaseWhensCheck(), new File("src/test/resources/ifTest.esql"))
	        .next().atLine(15).withMessage("This branch duplicates the one on line 11.")
	        .next().atLine(16).withMessage("This branch duplicates the one on line 7.")
	        .noMore();
	  }
}
