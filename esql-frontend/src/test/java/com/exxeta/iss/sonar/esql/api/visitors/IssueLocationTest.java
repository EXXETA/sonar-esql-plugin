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
package com.exxeta.iss.sonar.esql.api.visitors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;

public class IssueLocationTest {

	@Test
	public void several_lines_token() throws Exception {
		String tokenValue = "\"first line\\\n" + "second\"";

		IssueLocation location = new IssueLocation(createToken(3, 2, tokenValue));
		assertThat(location.endLine()).isEqualTo(4);
		assertThat(location.endLineOffset()).isEqualTo(7);
	}

	@Test
	public void several_lines_comment() throws Exception {
		String tokenValue = "/*first line\n" + "second*/";

		IssueLocation location = new IssueLocation(createToken(3, 2, tokenValue));
		assertThat(location.endLine()).isEqualTo(4);
		assertThat(location.endLineOffset()).isEqualTo(8);
	}

	private Tree createToken(int line, int column, String tokenValue) {
		return new InternalSyntaxToken(line, column, tokenValue, ImmutableList.<SyntaxTrivia>of(), 0, false);
	}

}
