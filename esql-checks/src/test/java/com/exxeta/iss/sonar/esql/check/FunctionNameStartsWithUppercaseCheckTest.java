/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
public class FunctionNameStartsWithUppercaseCheckTest {
	@Test
	public void testIgnoreMain() throws Exception {
		FunctionNameStartsWithUppercaseCheck check = new FunctionNameStartsWithUppercaseCheck();

		EsqlCheckVerifier.issues(check, new File("src/test/resources/functionNameStartsWithUppercase.esql"))
				.next().atLine(5)
				.withMessage("Rename function \"too_long_function_name_because_it_has_more_than_30_characters\""
						+ ". Function name should start with Uppercase.")
				.next().atLine(8)
				.withMessage("Rename function \"functionOk\""
						+ ". Function name should start with Uppercase.")
				.noMore();
	}

	@Test
	public void test() throws Exception {
		FunctionNameStartsWithUppercaseCheck check = new FunctionNameStartsWithUppercaseCheck();
		check.ignoreMain=false;

		EsqlCheckVerifier.issues(check, new File("src/test/resources/functionNameStartsWithUppercase.esql"))
		.next().atLine(2)
			.withMessage(
						"Rename function \"Badly_Named_Function\" Because function name should start with Uppercase.")
				.next().atLine(5)
				.withMessage("Rename function \"too_long_function_name_because_it_has_more_than_30_characters\" "
						+ "Function name should start with Uppercase.")
				//.next().atLine(12)
				//.withMessage("Rename function \"Main\" "
						//+ "Function name should start with Uppercase.")
				.noMore();
	}
	public static void  main(String [] args) throws Exception
	{
		new FunctionNameStartsWithUppercaseCheckTest().test();	
    }
}
