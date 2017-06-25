package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class TooManyParametersCheckTest {
	@Test
	public void test() {
		TooManyParametersCheck check = new TooManyParametersCheck();
		check.maximum = 3;
		EsqlCheckVerifier.verify(check, new File("src/test/resources/tooManyParameters.esql"));
	}

}
