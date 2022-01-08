package com.exxeta.iss.sonar.esql.api.tree.declaration;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.BrokerSchemaStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.IndexTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementNameTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementNamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class BrokerSchemaStatementTreeTest extends EsqlTreeModelTest<BrokerSchemaStatementTree>{

	
	@Test
	void fielReference() {
		assertThat(Kind.BROKER_SCHEMA_STATEMENT)
		.matches("BROKER SCHEMA a.a")
		.matches("BROKER SCHEMA a")
		;
		
	}
	
	@Test
	void model() throws Exception{
		BrokerSchemaStatementTree tree = parse("BROKER SCHEMA a.a", Kind.BROKER_SCHEMA_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.brokerKeyword());
		assertNotNull(tree.schemaKeyword());
		assertNotNull(tree.schemaName());
		assertNotNull(tree.schemaName().text());
		assertNotNull(tree.schemaName().schemaElements());
	}
}
