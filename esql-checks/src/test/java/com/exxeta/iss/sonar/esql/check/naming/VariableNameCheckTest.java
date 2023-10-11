/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.check.naming;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class VariableNameCheckTest {
	@Test
	void test() throws Exception {
		VariableNameCheck check = new VariableNameCheck();
		 EsqlCheckVerifier.issues(check, new File(
				"src/test/resources/variableNames.esql"))
				.next()
				.atLine(5)
				.withMessage(
						"Rename variable \"Ref1\" to match the regular expression ^[a-z][a-zA-Z0-9]{0,30}$.")
				.next()
				.atLine(7)
				.withMessage(
						"Rename variable \"b777_777\" to match the regular expression ^[a-z][a-zA-Z0-9]{0,30}$.")
				.noMore();
	}
}
