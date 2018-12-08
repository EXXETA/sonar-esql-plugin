package com.exxeta.iss.sonar.esql.api.tree.symbol;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import static org.assertj.core.api.Assertions.assertThat;

public class TypeTest {

	@Test
	public void test() {
		assertThat(Type.Kind.values()).hasSize(14);
		assertThat(Type.Callability.values()).hasSize(3);
	}
}
