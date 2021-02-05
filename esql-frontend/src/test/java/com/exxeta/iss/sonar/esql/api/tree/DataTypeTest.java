/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

public class DataTypeTest {

	@Test
	public void realLife() {
		assertThat(EsqlLegacyGrammar.dataType)
			.matches("CHARACTER")
			.matches("BOOLEAN")
			.matches("CHAR")
			.matches("DECIMAL")
			.matches("INTERVAL YEAR TO MONTH")
			.matches("INTERVAL DAY TO HOUR")
			.matches("INTERVAL MINUTE TO SECOND")
			.matches("INTERVAL MONTH")
			.matches("REFERENCE")
			.notMatches("STRING");
		
	}
	
}
