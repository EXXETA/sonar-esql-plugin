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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class NonReservedKeywordCheckTest {
	@Test
	public void test() throws Exception {
		NonReservedKeywordCheck check = new NonReservedKeywordCheck();
		 EsqlCheckVerifier.issues(check, new File("src/test/resources/lowerCaseKeyword.esql"))
			.next()
			.atLine(4)
			.withMessage(
					"ESQL keyword \"BLOB\" should not be used as an identifier.")
			.next()
			.atLine(4)
			.withMessage(
					"ESQL keyword \"COUNT\" should not be used as an identifier.")
				.noMore();
	}
}
