package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class CaseWithTooManyWhensCheckTest {

	@Test
	public void test(){
		CaseWithTooManyWhensCheck test = new CaseWithTooManyWhensCheck();
		test.maximumCases=2;
		EsqlCheckVerifier.verify(test, new File("src/test/resources/caseWithTooManyWhens.esql"));
	}
}
