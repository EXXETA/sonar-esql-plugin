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
package com.exxeta.iss.sonar.esql.api.tree.function;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.AsbitstreamFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class AsbitstreamFunctionTest extends EsqlTreeModelTest<AsbitstreamFunctionTree>{

	@Test
	public void asbitstreamFunction() {
		assertThat(Kind.ASBITSTREAM_FUNCTION)
		.matches("ASBITSTREAM(cursor OPTIONS options ENCODING enc CCSID 1208 SET set TYPE type FORMAT format)")
		.matches("ASBITSTREAM(cursor OPTIONS options CCSID 1208)")
		.matches("ASBITSTREAM(Environment.Variables.MQRFH2.Data,,,,,,)")
		.matches("ASBITSTREAM(Environment.Variables.MQRFH2.Data,,1208)")
		.matches("ASBITSTREAM(Environment.Variables.MQRFH2.Data,enc,1208, set, type, format, options)")
		.matches("ASBITSTREAM(Environment.Variables.MQRFH2.Data,enc)");
	}
	
	@Test
	public void modelTest() throws Exception {
		AsbitstreamFunctionTree tree = parse("ASBITSTREAM(cursor OPTIONS options CCSID 1208)", Kind.ASBITSTREAM_FUNCTION);
		assertNotNull(tree.asbitstreamKeyword());
		
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.fieldReference());
		
		assertNotNull(tree.optionsSeparator());
		assertNotNull(tree.optionsExpression());
		assertNull(tree.encodingSeparator());
		assertNull(tree.encodingExpression());
		assertNotNull(tree.ccsidSeparator());
		assertNotNull(tree.ccsidExpression());
		assertNull(tree.setSeparator());
		assertNull(tree.setExpression());
		assertNull(tree.typeSeparator());
		assertNull(tree.typeExpression());
		assertNull(tree.formatSeparator());
		assertNull(tree.formatExpression());
		
		assertNotNull(tree.closingParenthesis());
	}

}
