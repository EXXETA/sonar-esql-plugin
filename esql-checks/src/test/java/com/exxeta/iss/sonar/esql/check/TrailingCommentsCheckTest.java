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
		    .next().atLine(7).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(12).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(15).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(16).withMessage("Move this trailing comment on the previous empty line.")
		 .noMore();
	        
	  }
  
  @Test
  public void withoutPattern(){
	  TrailingCommentsCheck check = new TrailingCommentsCheck();
	  check.setLegalCommentPattern("");
		 EsqlCheckVerifier.issues(check, new File("src/test/resources/trailingComments.esql"))
		    .next().atLine(7).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(12).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(14).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(15).withMessage("Move this trailing comment on the previous empty line.")
		    .next().atLine(16).withMessage("Move this trailing comment on the previous empty line.")
		 .noMore();
	        

  }
   
    
  }

