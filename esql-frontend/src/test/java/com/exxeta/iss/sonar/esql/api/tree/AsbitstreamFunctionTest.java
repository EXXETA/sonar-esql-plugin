package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class AsbitstreamFunctionTest {

	@Test
	public void asbitstreamFunction() {
		assertThat(Kind.ASBITSTREAM_FUNCTION)
			.matches("ASBITSTREAM(cursor OPTIONS options CCSID 1208)")
			.matches("ASBITSTREAM(Environment.Variables.MQRFH2.Data,,1208,,,,options)");
	}

}
