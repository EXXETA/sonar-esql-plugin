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
package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
public class SelectFunctionTest extends EsqlTreeModelTest<SelectFunctionTree>{

	@Test
	public void selectFunction() {
		assertThat(Kind.SELECT_FUNCTION)
		.matches("SELECT P.PartNumber AS a,  P.Description,  P.Price FROM PartsTable.Part[]")
		.matches("SELECT * FROM Database.Datasource.SchemaName.Table As A")
		.matches("SELECT P.PartNumber AS a,  P.Description,  P.Price FROM PartsTable.Part[] AS P")
		.matches("SELECT COUNT(*) FROM Database.{UDP_Schemaname}.DATA AS DAT WHERE CON.a=a  AND CON.b NOT IN('C', 'I')")
		.matches("SELECT FIELDVALUE(v.a) FROM vehicle.fb:VEHICLE[] AS v WHERE v.fb:c = Environment.Variables.F")
		.matches("SELECT * FROM Employer AS e, Manager AS m")
		.matches("SELECT ITEM NAME FROM Employer AS e")
		;
	}
	
	@Test
	public void selectClause(){
		assertThat(Kind.SELECT_CLAUSE)
		.matches("P.PartNumber,  P.Description,  P.Price")
		.matches("COUNT(*)")
		.matches("FIELDVALUE(v.a)")
		;
	}

	@Test
	public void fromClause(){
		assertThat(Kind.FROM_CLAUSE_EXPRESSION)
		.matches("FROM PartsTable.Part[] AS P")
		.matches("FROM Database.Datasource.SchemaName.Table As A")
		.matches("FROM Database.{UDP_Schemaname}.DATA AS DATN")
		;
	}

	@Test
	public void whereClause(){
		assertThat(Kind.WHERE_CLAUSE)
		.matches("WHERE A = B")
		.matches("WHERE CON.a=a  AND CON.b NOT IN('C', 'I')");
	}
	
	@Test
	public void modelTest() throws Exception{
		SelectFunctionTree tree = parse("SELECT * FROM Database.Datasource.SchemaName.Table As A Where A.A>10", Kind.SELECT_FUNCTION);
		assertNotNull(tree);
		assertNotNull(tree.selectKeyword());
		assertNotNull(tree.selectClause());
		assertNotNull(tree.fromClause());
		assertNotNull(tree.whereClause());
		assertTrue(tree.selectClause().aliasedFieldReferenceList().get(0).expression().is(Kind.FIELD_REFERENCE));
		FieldReferenceTree field = (FieldReferenceTree)tree.selectClause().aliasedFieldReferenceList().get(0).expression();
		assertEquals(field.pathElement().name().star().text(),"*");
	}

}
