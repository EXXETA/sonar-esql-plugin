package com.exxeta.iss.sonar.esql.api.tree;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class DeclareStatementTest extends EsqlTreeModelTest<DeclareStatementTree>{

	@Test
	public void modelTest() throws Exception {
		DeclareStatementTreeImpl tree = parse("DECLARE deployEnvironment EXTERNAL CHARACTER 'Dev';", Kind.DECLARE_STATEMENT);
		
		assertNotNull(tree.declareToken());
		assertNotNull(tree.nameList());
		assertNotNull(tree.sharedExt());
		assertNull(tree.namesapce());
		assertNull(tree.constantKeyword());
		assertNotNull(tree.dataType());
		assertNotNull(tree.initialValueExpression());
		assertNotNull(tree.semi());
		
		parse("DECLARE aaa NAMESPACE 'com.exxeta.test';");
		parse("DECLARE Schema1 NAME 'Joe';");
	}
	
}
