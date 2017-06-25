package com.exxeta.iss.sonar.esql.api.tree;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.BeginEndStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class BeginEndStatementTest extends EsqlTreeModelTest<BeginEndStatementTree> {

	@Test
	public void modelTest() throws Exception {
		BeginEndStatementTreeImpl tree = parse("BEGIN CALL A(); END;", Kind.BEGIN_END_STATEMENT);

		assertNotNull(tree.beginKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.semiToken());

	}

	@Test
	public void modelTestWithLabel() throws Exception {
		BeginEndStatementTreeImpl tree = parse("A: BEGIN CALL A(); END A;", Kind.BEGIN_END_STATEMENT);

		assertNotNull(tree.labelName1());
		assertNotNull(tree.colon());
		assertNotNull(tree.beginKeyword());
		assertNull(tree.notKeyword());
		assertNull(tree.atomicKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.labelName2());
		assertNotNull(tree.semiToken());
	}

}
