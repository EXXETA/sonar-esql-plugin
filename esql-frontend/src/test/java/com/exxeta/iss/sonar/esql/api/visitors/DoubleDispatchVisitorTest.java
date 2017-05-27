package com.exxeta.iss.sonar.esql.api.visitors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.sonar.sslr.api.typed.ActionParser;

public class DoubleDispatchVisitorTest {

	@Test
	public void test_visit_token() throws Exception {
		assertNumberOfVisitedTokens("DECLARE a, b CONSTANT CHAR 'A';", 9);
		assertNumberOfVisitedTokens("DECLARE a CONSTANT CHAR 'A';", 7);
	}

	@Test
	public void test_visit_comment() throws Exception {
		assertNumberOfVisitedComments("-- comment1 \n DECLARE a CONSTANT CHAR 'A';-- comment2 \n/*comment3*/", 3);
	}

	private class TestVisitor extends DoubleDispatchVisitor {
		int tokenCounter;
		int commentCounter;

		@Override
		public void visitProgram(ProgramTree tree) {
			tokenCounter = 0;
			commentCounter = 0;
			super.visitProgram(tree);
		}

		@Override
		public void visitToken(SyntaxToken token) {
			tokenCounter++;
			super.visitToken(token);
		}

		@Override
		public void visitComment(SyntaxTrivia commentToken) {
			commentCounter++;
			super.visitComment(commentToken);
		}
	}

	private void assertNumberOfVisitedTokens(String code, int expectedLiteralsNumber) {
		assertThat(getTestVisitor(code).tokenCounter).isEqualTo(expectedLiteralsNumber);
	}

	private void assertNumberOfVisitedComments(String code, int expectedLiteralsNumber) {
		assertThat(getTestVisitor(code).commentCounter).isEqualTo(expectedLiteralsNumber);
	}

	private TestVisitor getTestVisitor(String code) {
		ActionParser<Tree> p = EsqlParserBuilder.createParser();
		TestVisitor testVisitor = new TestVisitor();
		testVisitor.visitProgram((ProgramTree) p.parse(code));
		return testVisitor;
	}

}
