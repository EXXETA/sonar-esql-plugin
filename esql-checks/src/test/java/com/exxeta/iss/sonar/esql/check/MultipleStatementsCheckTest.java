/**
 * This java class is created to implement the logic for checking if multiple statements are present on the same line,
 * if it is present then it should be removed.
 * 
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import org.junit.Test;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class MultipleStatementsCheckTest {
  @Test
  public void test() {
		 
		 
		 EsqlCheckVerifier.issues(new MultipleStatementsCheck(), new File("src/test/resources/multipleStatements.esql"))
		 .next().atLine(5).withMessage("Multiple statements on the same line.")
		 .next().atLine(8).withMessage("Multiple statements on the same line.")
		 .noMore();
	        
	  }
   
    
  }

