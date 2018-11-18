package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateModuleStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class CreateModuleStatementTest  extends EsqlTreeModelTest<CreateModuleStatementTreeImpl>{
	
	@Test
	public void createModule(){
		assertThat(Kind.CREATE_MODULE_STATEMENT)
		.matches("CREATE COMPUTE MODULE AB\n END MODULE;")
		;
	}
	
	@Test
	public void modelTest() throws Exception {
		CreateModuleStatementTree tree = parse("CREATE COMPUTE MODULE abc END MODULE ;", Kind.CREATE_MODULE_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.createKeyword());
		assertEquals(tree.createKeyword().text(), "CREATE");
		assertNotNull(tree.moduleType());
		assertEquals(tree.moduleType().text(), "COMPUTE");
		assertNotNull(tree.moduleKeyword());
		assertEquals(tree.moduleKeyword().text(), "MODULE");
		assertNotNull(tree.moduleName());
		assertEquals(tree.moduleName().firstToken().text(), "abc");
		assertNotNull(tree.moduleStatementsList());
		assertEquals(tree.moduleStatementsList().statements().size(), 0);
		assertNotNull(tree.endKeyword());
		assertEquals(tree.endKeyword().text(), "END");
		assertNotNull(tree.moduleKeyword2());
		assertEquals(tree.moduleKeyword2().text(), "MODULE");
		assertNotNull(tree.semi());
		assertEquals(tree.semi().text(), ";");
		
		
	}
}
