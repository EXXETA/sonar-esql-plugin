package com.exxeta.iss.sonar.esql.api.tree.impl.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ParameterTreeTest extends EsqlTreeModelTest<ParameterTree> {

	@Test
	public void moveStatement(){
		
		assertThat(Kind.PARAMETER)
		.matches("INOUT aaa")
		.matches("OUT bbb CONSTANT INTEGER");
	}
	
	@Test
	public void modelTest () throws Exception{
		ParameterTree tree = parse("IN aaa NAMESPACE", Kind.PARAMETER);
		assertNotNull(tree.directionIndicator());
		assertNotNull(tree.identifier());
		assertNull(tree.constantKeyword());
		assertNotNull(tree.nameOrNamesapceKeyword());
		assertNull(tree.dataType());

	}
	
}
