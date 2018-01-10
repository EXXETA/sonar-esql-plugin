/**
 * This java class is created to implement the logic for checking if commented esql code is present, 
 * if it is present then it should be removed before code checkin.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import org.junit.Test;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class CommentedOutEsqlCodeCheckTest{
  @Test
  public void test() {
		
		 EsqlCheckVerifier.issues(new CommentedOutEsqlCodeCheck(), new File("src/test/resources/commentedOutEsqlCode.esql"))
		 .next().atLine(6).withMessage("Esql code has been commented out.It should be removed before code checkin.")
		 .next().atLine(9).withMessage("Esql code has been commented out.It should be removed before code checkin.")
		 .noMore();
  }
  
}