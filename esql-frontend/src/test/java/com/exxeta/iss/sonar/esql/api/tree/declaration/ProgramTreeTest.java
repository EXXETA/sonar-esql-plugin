package com.exxeta.iss.sonar.esql.api.tree.declaration;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ProgramTreeTest extends EsqlTreeModelTest<ProgramTree> {
	@Test
	public void fielReference() {
		assertThat(EsqlLegacyGrammar.PROGRAM)
		.matches("BROKER SCHEMA a.a")
		.matches("BROKER SCHEMA a")
		;
		
		assertThat(Kind.PATH_CLAUSE)
		.matches("PATH A")
		;
		
	}
	
	@Test
	public void model() throws Exception{
		ProgramTree tree = parse("BROKER SCHEMA a.a PATH A;");
		assertNotNull(tree);
		assertNotNull(tree.brokerSchemaStatement());

		assertNotNull(tree.pathClause());
		assertNotNull(tree.pathClause().schemaNames());
		assertNotNull(tree.pathClause().pathToken());
		
		assertNotNull(tree.semiToken());
		
		assertNotNull(tree.esqlContents());
		
		assertNotNull(tree.EOFToken());
	}
}
