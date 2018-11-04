/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.FromClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ParseClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ValuesClauseTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;


public class CreateStatementTest  extends EsqlTreeModelTest<CreateStatementTreeImpl>{
	@Test
	public void domainClause(){
		assertThat(Kind.REPEAT_CLAUSE)
		.matches("REPEAT");
	}	
	
	@Test
	public void valueClause(){
		assertThat(Kind.VALUES_CLAUSE)
		.matches("VALUE 'Element 2 Value'")
		.matches("IDENTITY(JSON.Object)");
	}
	
	@Test
	public void createStatement(){
		assertThat(Kind.CREATE_STATEMENT)
		.matches("CREATE FIELD OutputRoot.XMLNS.Data;")
		.matches("CREATE LASTCHILD OF OutputRoot.XMLNS.TestCase.Root IDENTITY (XML.Element)NSpace1:Element1[2] VALUE 'Element 2 Value';")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM');")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream, inEncoding, inCCSID, 'DP3UK14002001', 'TestCase', 'XML1', options);")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream ENCODING inEncoding CCSID inCCSID);")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream SET 'abc' TYPE 'TestCase' FORMAT 'XML1');")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream OPTIONS options);")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN ('SOAP') PARSE (InputLocalEnvironment.Variables.Pas.Request CCSID 1208 OPTIONS RootBitStream );")
		.matches("CREATE LASTCHILD OF OutputRoot.XMLNS.TestCase.Root IDENTITY (XML.Element)NSpace1:Element1[2] VALUE 'Element 2 Value';");
		;
	}
	
	@Test
	public void parseClause(){
		assertThat(Kind.PARSE_CLAUSE)
		.matches("PARSE(inBitStream, inEncoding, inCCSID, 'DP3UK14002001', 'TestCase', 'XML1', options)")
		.matches("PARSE(inBitStream, inEncoding, inCCSID,,,, options)")
		.matches("PARSE(inBitStream, inEncoding, inCCSID,,,'XML1')")
		.matches("PARSE(inBitStream)");
		;
	}
	@Test
	public void fromClause(){
		assertThat(Kind.FROM_CLAUSE)
		.matches("FROM InputRoot.A")
		;
	}
	
	@Test
	public void valuesClause(){
		assertThat(Kind.VALUES_CLAUSE)
		.matches("TYPE Name  NAMESPACE n NAME 'abc'")
		;
	}
	
	@Test
	public void modelTest() throws Exception {
		CreateStatementTreeImpl tree = parse("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream, inEncoding, inCCSID, 'DP3UK14002001', 'TestCase', 'XML1', options);", Kind.CREATE_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.createKeyword());
		assertEquals(tree.createKeyword().text(), "CREATE");
		assertNotNull(tree.qualifierName());
		assertEquals(tree.qualifierName().text(), "LASTCHILD");
		assertNotNull(tree.qualifierOfKeyword());
		assertEquals(tree.qualifierOfKeyword().text(), "OF");
		assertNotNull(tree.target());
		assertNull(tree.asKeyword());
		assertNull(tree.aliasFieldReference());
		assertNotNull(tree.domainKeyword());
		assertEquals(tree.domainKeyword().text(), "DOMAIN");
		assertNotNull(tree.domainExpression());
		assertTrue(tree.domainExpression().is(Kind.PARENTHESISED_EXPRESSION));
		assertNull(tree.repeatClause());
		assertNull(tree.valuesClause());
		assertNull(tree.fromClause());
		assertNotNull(tree.parseClause());
		assertNotNull(tree.semi());
		assertEquals(tree.semi().text(), ";");
		
		//ParseClause
		ParseClauseTreeImpl parseClause = tree.parseClause();
		assertNotNull(parseClause);
		assertNotNull(parseClause.parseKeyword());
		assertEquals(parseClause.parseKeyword().text(), "PARSE");
		assertNotNull(parseClause.openingParenthesis());
		assertEquals(parseClause.openingParenthesis().text(), "(");
		
		assertNotNull(parseClause.expression());
		assertNotNull(parseClause.optionsSeparator());
		assertNotNull(parseClause.optionsExpression());
		assertNotNull(parseClause.encodingSeparator());
		assertNotNull(parseClause.encodingExpression());
		assertNotNull(parseClause.ccsidSeparator());
		assertNotNull(parseClause.ccsidExpression());
		assertNotNull(parseClause.setSeparator());
		assertNotNull(parseClause.setExpression());
		assertNotNull(parseClause.typeSeparator());
		assertNotNull(parseClause.typeExpression());
		assertNotNull(parseClause.formatSeparator());
		assertNotNull(parseClause.formatExpression());
		
		
		assertNotNull(parseClause.closingParenthesis());
		assertEquals(parseClause.closingParenthesis().text(), ")");

		assertTrue(parseClause.isCommaSeparated());
		
		tree = parse("CREATE LASTCHILD OF OutputRoot From InputRoot.A;", Kind.CREATE_STATEMENT);
		
		assertNotNull(tree.fromClause());
		FromClauseTreeImpl fromClause = tree.fromClause();
		assertNotNull(fromClause.fromKeyword());
		assertNotNull(fromClause.fieldReference());

			
		tree = parse("CREATE FIELD OutputRoot.XMLNS.TestCase.Root.Attribute IDENTITY (XML.Attribute)NSpace1:Attribute VALUE 'Attrib Value';",Kind.CREATE_STATEMENT);
		ValuesClauseTreeImpl valuesClause = tree.valuesClause();
		
		assertNotNull(valuesClause.identityKeyword());
		assertNotNull(valuesClause.identity());
		assertNull(valuesClause.typeKeyword());
		assertNull(valuesClause.type());
		assertNull(valuesClause.namespaceKeyword());
		assertNull(valuesClause.namespace());
		assertNull(valuesClause.nameKeyword());
		assertNull(valuesClause.name());
		
		
		assertNotNull(valuesClause.valueKeyword());
		assertNotNull(valuesClause.value());
		
		tree = parse("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream, inEncoding,);", Kind.CREATE_STATEMENT);
		assertNull(tree.parseClause().formatSeparator());
		assertNull(tree.parseClause().formatExpression());
		assertNull(tree.parseClause().ccsidExpression());
	}

	@Test
	public void modelTest2() throws Exception {
		CreateStatementTreeImpl tree = parse("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream ENCODING inEncoding CCSID inCCSID SET 'DP3UK14002001' TYPE 'TestCase' FORMAT 'XML1' OPTIONS options);", Kind.CREATE_STATEMENT);
		ParseClauseTreeImpl parseClause = tree.parseClause();
		assertNotNull(parseClause);
		assertNotNull(parseClause.parseKeyword());
		assertEquals(parseClause.parseKeyword().text(), "PARSE");
		assertNotNull(parseClause.openingParenthesis());
		assertEquals(parseClause.openingParenthesis().text(), "(");
		
		assertNotNull(parseClause.expression());
		assertNotNull(parseClause.optionsSeparator());
		assertNotNull(parseClause.optionsExpression());
		assertNotNull(parseClause.encodingSeparator());
		assertNotNull(parseClause.encodingExpression());
		assertNotNull(parseClause.ccsidSeparator());
		assertNotNull(parseClause.ccsidExpression());
		assertNotNull(parseClause.setSeparator());
		assertNotNull(parseClause.setExpression());
		assertNotNull(parseClause.typeSeparator());
		assertNotNull(parseClause.typeExpression());
		assertNotNull(parseClause.formatSeparator());
		assertNotNull(parseClause.formatExpression());
		
		tree = parse("CREATE FIELD OutputRoot.JSON.Data.Item[1] IDENTITY(JSON.Object);", Kind.CREATE_STATEMENT);
		assertNotNull(tree);
		
		tree = parse("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream ENCODING inEncoding CCSID inCCSID SET 'DP3UK14002001' TYPE 'TestCase' FORMAT 'XML1');", Kind.CREATE_STATEMENT);
		assertNull(tree.parseClause().optionsExpression());
		assertNull(tree.parseClause().optionsSeparator());
		tree = parse("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream);", Kind.CREATE_STATEMENT);
		assertNull(tree.parseClause().optionsExpression());
		tree = parse("CREATE NEXTSIBLING OF targetCursor AS targetCursor REPEAT;", Kind.CREATE_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.repeatClause());
		assertNotNull(tree.repeatClause().repeatKeyword());
		assertNull(tree.repeatClause().valueKeyword());
		assertNull(tree.repeatClause().expression());
		Assertions.assertThat(tree.repeatClause().getKind()).isEqualTo(Kind.REPEAT_CLAUSE);
		
	}
	
}
