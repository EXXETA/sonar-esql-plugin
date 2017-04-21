package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class OverlayFunctionTest {

	@Test
	public void overlayFunction() {
		assertThat(Kind.OVERLAY_FUNCTION)
			.matches("OVERLAY ('ABCDEFGHIJ' PLACING '1234' FROM 4 FOR 3)");
	}

}
