package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class IfConditionalAlwaysTrueOrFalseCheckTest {
	 @Test
	  public void test() {
		 EsqlCheckVerifier.issues(new IfConditionalAlwaysTrueOrFalseCheck(), new File("src/test/resources/ifTest.esql"))
	        .next().atLine(5).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(6).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(7).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(8).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(12).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(16).withMessage("Remove this \"IF\" statement.")
	        .noMore();
	  }
}
