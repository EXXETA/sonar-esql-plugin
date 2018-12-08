package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.NullableTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class NullableTreeTest extends EsqlTreeModelTest<NullableTreeImpl> {

	@Test
	public void nullable() {

		assertThat(Kind.NULLABLE).matches("NULLABLE").matches("NOT NULL");

	}

	@Test
	public void modelTest() throws Exception {
		NullableTreeImpl tree = parse("NOT NULL", Kind.NULLABLE);
		assertNotNull(tree.notKeyword());
		assertEquals(tree.notKeyword().text(), "NOT");

		assertNull(tree.nullableKeyword());

		assertNotNull(tree.nullKeyword());
		assertEquals(tree.nullKeyword().text(), "NULL");

	}

}
