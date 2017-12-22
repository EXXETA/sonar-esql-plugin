/**
 * This java class is created to implement the logic for checking if the code is present after return or throw statement, 
 * if it is present then that code is unreachable.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class UnreachableCodeCheckTest {
	

  @Test
  public void test() {
		 EsqlCheckVerifier.issues(new UnreachableCodeCheck(), new File("src/test/resources/unreachableCode.esql"))
	        .next().atLine(19).withMessage("Code is unreachable following RETURN or THROW statement.")
	        .noMore();
	  }
   
    
  }

