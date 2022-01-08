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
package com.exxeta.iss.sonar.esql.check.naming;

import java.io.File;

import com.exxeta.iss.sonar.esql.check.naming.FunctionNameCheck;
import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class FunctionNameCheckTest {
	@Test
	void testIgnoreMain() throws Exception {
		FunctionNameCheck check = new FunctionNameCheck();

		EsqlCheckVerifier.issues(check, new File("src/test/resources/functionName.esql")).next().atLine(2)
				.withMessage(
						"Rename function \"Badly_Named_Function\" to match the regular expression ^[a-z][a-zA-Z0-9]{1,30}$.")
				.next().atLine(5)
				.withMessage("Rename function \"too_long_function_name_because_it_has_more_than_30_characters\" "
						+ "to match the regular expression ^[a-z][a-zA-Z0-9]{1,30}$.")
				.noMore();
	}

	@Test
	void test() throws Exception {
		FunctionNameCheck check = new FunctionNameCheck();
		check.ignoreMain=false;

		EsqlCheckVerifier.issues(check, new File("src/test/resources/functionName.esql")).next().atLine(2)
				.withMessage(
						"Rename function \"Badly_Named_Function\" to match the regular expression ^[a-z][a-zA-Z0-9]{1,30}$.")
				.next().atLine(5)
				.withMessage("Rename function \"too_long_function_name_because_it_has_more_than_30_characters\" "
						+ "to match the regular expression ^[a-z][a-zA-Z0-9]{1,30}$.")
				.next().atLine(12)
				.withMessage("Rename function \"Main\" "
						+ "to match the regular expression ^[a-z][a-zA-Z0-9]{1,30}$.")
				.noMore();
	}
}
