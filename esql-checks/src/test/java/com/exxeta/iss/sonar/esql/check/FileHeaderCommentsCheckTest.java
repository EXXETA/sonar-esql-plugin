package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Java class is a test class to check function header comments
 * @author 
 *
 */
public class FileHeaderCommentsCheckTest {

	  @Rule
	  public ExpectedException thrown = ExpectedException.none();

	  @Test
	  public void test_plain() {	 
		  
		  EsqlCheckVerifier.issues(new FileHeaderCommentsCheck(), new File("src/test/resources/FileHeaderComments.esql"))
	        .next().atLine(1).withMessage("Add or update the header at the start of this file.")	             
	        .noMore();
	    	   
	  }	
		
}
