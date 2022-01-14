/**
 * This java class is created to implement the logic for checking if comment is included or not, 
 * over every 20 lines of code.
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import org.junit.jupiter.api.Test;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class CommentsCheckTest{
  @Test
  void test() {
		
		 EsqlCheckVerifier.issues(new CommentsCheck(), new File("src/test/resources/comments.esql"))
		 .next().atLine(34).withMessage("Include comment within the range of every 20 lines of code.")
		 .noMore();
  }
  
}