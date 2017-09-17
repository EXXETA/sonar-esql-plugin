package com.exxeta.iss.sonar.esql.codecoverage;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeCoverageExtensionTest {
	@Test
	  public void testExtensions() {
	    assertThat(CodeCoverageExtension.getExtensions().size()).isEqualTo(1);
	  }
}
