package com.exxeta.iss.sonar.esql.api.tree.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class BinaryExpressionTest extends EsqlTreeModelTest<BinaryExpressionTree> {

	@Test
	public void conditional_or() throws Exception {
		test_binary_expression("a OR b OR c", Kind.CONDITIONAL_OR, "OR");
	}

	@Test
	public void conditional_and() throws Exception {
		test_binary_expression("a AND b AND c", Kind.CONDITIONAL_AND, "AND");
	}

	@Test
	public void equality_equal_to() throws Exception {
		test_binary_expression("a = b = c", EsqlLegacyGrammar.equalityExpression, Kind.EQUAL_TO, "=");
	}

	@Test
	public void equality_not_equal_to() throws Exception {
		test_binary_expression("a <> b <> c", EsqlLegacyGrammar.equalityExpression, Kind.NOT_EQUAL_TO, "<>");
	}

	@Test
	public void relation_less_than() throws Exception {
		test_binary_expression("a < b < c", EsqlLegacyGrammar.relationalExpression, Kind.LESS_THAN, "<");
	}

	@Test
	public void relation_greater_than() throws Exception {
		test_binary_expression("a > b > c", EsqlLegacyGrammar.relationalExpression, Kind.GREATER_THAN, ">");
	}

	@Test
	public void relation_less_or_equal_to() throws Exception {
		test_binary_expression("a <= b <= c", EsqlLegacyGrammar.relationalExpression, Kind.LESS_THAN_OR_EQUAL_TO, "<=");
	}

	@Test
	public void relation_greater_or_equal_to() throws Exception {
		test_binary_expression("a >= b >= c", EsqlLegacyGrammar.relationalExpression, Kind.GREATER_THAN_OR_EQUAL_TO,
				">=");
	}

	@Test
	public void additive_plus() throws Exception {
		test_binary_expression("a + b + c", EsqlLegacyGrammar.additiveExpression, Kind.PLUS, "+");
	}

	@Test
	public void additive_minus() throws Exception {
		test_binary_expression("a - b - c", EsqlLegacyGrammar.additiveExpression, Kind.MINUS, "-");
	}

	@Test
	public void multiplicative_multiply() throws Exception {
		test_binary_expression("a * b * c", EsqlLegacyGrammar.multiplicativeExpression, Kind.MULTIPLY, "*");
	}

	@Test
	public void multiplicative_divide() throws Exception {
		test_binary_expression("a / b / c", EsqlLegacyGrammar.multiplicativeExpression, Kind.DIVIDE, "/");
	}

	@Test
	public void additive_concat() throws Exception {
		test_binary_expression("a || b || c", EsqlLegacyGrammar.additiveExpression, Kind.CONCAT, "||");
	}

	private void test_binary_expression(String str, Kind kind, String operator) throws Exception {
		test_binary_expression(str, kind, kind, operator);
	}

	private void test_binary_expression(String str, GrammarRuleKey rule, Kind kind, String operator) throws Exception {
		BinaryExpressionTree tree = parse(str, rule, kind);

		assertThat(tree.is(kind)).isTrue();
		assertThat(tree.leftOperand()).isNotNull();
		assertThat(tree.operator().text()).isEqualTo(operator);
		assertThat(tree.rightOperand()).isNotNull();

		tree = (BinaryExpressionTree) tree.leftOperand();

		assertThat(tree.is(kind)).isTrue();
		assertThat(tree.leftOperand()).isNotNull();
		assertThat(tree.operator().text()).isEqualTo(operator);
		assertThat(tree.rightOperand()).isNotNull();
	}
}
