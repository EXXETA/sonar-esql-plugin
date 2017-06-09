package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class SleepCheckTest {

	@Test
	public void test(){
		EsqlCheckVerifier.verify(new SleepCheck(), new File("src/test/resources/sleep.esql"));
	}
}
