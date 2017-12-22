/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author Sapna Singh
 *
 */
public class SubElementNameCheckTest {
	
	@Test
	public void test() {
		EsqlCheck check = new SubElementNameCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/SubElementName.esql"))
		.next().atLine(5).withMessage("sub-elements should be in UpperCamel-case and elements containing simple value should be in lowercase.")
		.next().atLine(6).withMessage("sub-elements should be in UpperCamel-case and elements containing simple value should be in lowercase.").noMore();
	}
}
