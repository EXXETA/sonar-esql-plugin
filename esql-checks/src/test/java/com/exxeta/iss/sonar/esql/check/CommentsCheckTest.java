/**
 * This java class is created to implement the logic for checking if comment is included or not, 
 * over every 20 lines of code.
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import org.junit.Test;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class CommentsCheckTest{
  @Test
  public void test() {
		
		 EsqlCheckVerifier.issues(new CommentsCheck(), new File("src/test/resources/comments.esql"))
		 .next().atLine(34).withMessage("Include comment after every 20 lines of code.")
		 .noMore();
  }
  
}