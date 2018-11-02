package com.exxeta.iss.sonar.esql.api.tree.declaration;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.DecimalDataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class DecimalDataTypeTreeTest extends EsqlTreeModelTest<DecimalDataTypeTree> {
	@Test
	public void fielReference() {
		assertThat(Kind.DECIMAL_DATA_TYPE)
		.matches("DECIMAL(1,2)")
		.matches("DECIMAL")
		;
		
	}
	
	@Test
	public void model() throws Exception{
		DecimalDataTypeTree tree = parse("DECIMAL(1,2)", Kind.DECIMAL_DATA_TYPE);
		assertNotNull(tree);
		assertNotNull(tree.decimalKeyword());
		assertNotNull(tree.openParen());
		assertNotNull(tree.precision());
		assertNotNull(tree.comma());
		assertNotNull(tree.scale());
		assertNotNull(tree.closeParen());
	}
}
