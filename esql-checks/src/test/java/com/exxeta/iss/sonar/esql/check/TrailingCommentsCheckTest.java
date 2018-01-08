/**
 * This java class is created to implement the logic for checking if the line contains both code and comments, 
 * if it contains both then trailing comments should be removed.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class TrailingCommentsCheckTest {
  @Test
  public void test() {
		
		 EsqlCheckVerifier.issues(new TrailingCommentsCheck(), new File("src/test/resources/trailingComments.esql"))
		    .next().atLine(7).withMessage("The line contains both code and comments. Trailing comments are discouraged.")
		    .next().atLine(12).withMessage("The line contains both code and comments. Trailing comments are discouraged.")
		    .next().atLine(15).withMessage("The line contains both code and comments. Trailing comments are discouraged.")
		    .next().atLine(16).withMessage("The line contains both code and comments. Trailing comments are discouraged.")
		 .noMore();
	        
	  }
   
    
  }

