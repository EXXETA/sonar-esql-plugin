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
package com.exxeta.iss.sonar.esql.check;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class CommentRegularExpressionCheckTest {
	@Test
	void test() {
		CommentRegularExpressionCheck check = new CommentRegularExpressionCheck();

		check.setRegularExpression("(?i).*TODO.*");
		check.setMessage("Avoid TODO");

		EsqlCheckVerifier.issues(check, new File("src/test/resources/empty.esql")).next().atLine(1)
				.withMessage("Avoid TODO").noMore();

	}

	@Test
	void exception() {
		CommentRegularExpressionCheck check = new CommentRegularExpressionCheck();

		try {
			check.setRegularExpression("[abc");
		} catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Unable to compile regular expression: [abc");
		}

	}

	@Test
	void emptyPattern() {
		CommentRegularExpressionCheck check = new CommentRegularExpressionCheck();

		check.setRegularExpression("");
		check.setMessage("Avoid TODO");

		EsqlCheckVerifier.issues(check, new File("src/test/resources/empty.esql")).noMore();

	}

	@Test
	void noMatch() {
		CommentRegularExpressionCheck check = new CommentRegularExpressionCheck();

		check.setRegularExpression("(?i).*FIXME.*");
		check.setMessage("Avoid TODO");

		EsqlCheckVerifier.issues(check, new File("src/test/resources/empty.esql")).noMore();

	}
}
