package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class LoopWithoutLeaveCheckTest {
	@Test
	  public void loopTest() {
		 EsqlCheckVerifier.issues(new LoopWithoutLeaveCheck(), new File("src/test/resources/loopWithoutLeave.esql"))
	        .next().atLine(9).withMessage("\"LOOP\"-statements without \"LEAVE\" will never terminate.")
	        .noMore();
	  }
}
