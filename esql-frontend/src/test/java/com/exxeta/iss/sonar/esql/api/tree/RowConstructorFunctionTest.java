package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class RowConstructorFunctionTest {

	@Test
	public void rowConstructorFunction() {
		assertThat(Kind.ROW_CONSTRUCTOR_FUNCTION)
		.matches("ROW('granary' AS bread, 'riesling' AS wine, 'stilton' AS cheese)");
		assertThat(Kind.SET_STATEMENT)
		.matches("SET OutputRoot.XMLNS.Data = ROW('granary' AS bread, 'riesling' AS wine, 'stilton' AS cheese);");
	}
	
}
