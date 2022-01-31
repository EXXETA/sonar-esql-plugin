package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.NullableTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class NullableTreeTest extends EsqlTreeModelTest<NullableTreeImpl> {

	@Test
	void nullable() {

		assertThat(Kind.NULLABLE).matches("NULLABLE").matches("NOT NULL");

	}

	@Test
	void modelTest() throws Exception {
		NullableTreeImpl tree = parse("NOT NULL", Kind.NULLABLE);
		assertNotNull(tree.notKeyword());
		assertEquals("NOT", tree.notKeyword().text());

		assertNull(tree.nullableKeyword());

		assertNotNull(tree.nullKeyword());
		assertEquals("NULL", tree.nullKeyword().text());

	}

}
