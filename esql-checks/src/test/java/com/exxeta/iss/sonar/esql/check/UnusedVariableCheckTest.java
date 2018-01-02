/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;


/**
 * This java class is the test class.
 * @author Sapna Singh
 *
 */
public class UnusedVariableCheckTest  {
	@Test
	public void test() {
		EsqlCheck check = new UnusedVariableCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/UnusedVariable.esql"))
		
		.next().atLine(1).withMessage("Check Variable \"envRef\". Remove the unused Variable.").noMore();;
	
	}	

}

