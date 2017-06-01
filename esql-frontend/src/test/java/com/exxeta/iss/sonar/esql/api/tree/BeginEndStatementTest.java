package com.exxeta.iss.sonar.esql.api.tree;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.BeginEndStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class BeginEndStatementTest extends EsqlTreeModelTest<BeginEndStatementTree>{

	@Test
	public void modelTest() throws Exception {
		BeginEndStatementTreeImpl tree = parse("BEGIN CALL A(); END;", Kind.BEGIN_END_STATEMENT);
		
		assertNotNull(tree.beginKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.semiToken());
		
	}
	
}
