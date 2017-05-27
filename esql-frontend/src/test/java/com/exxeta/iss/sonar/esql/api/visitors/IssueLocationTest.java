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
