package com.exxeta.iss.sonar.esql.api.tree.expression;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.Assertions;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class CallExpressionTest extends EsqlTreeModelTest<CallExpressionTree> {

	@Test
	public void expression() {
		Assertions.assertThat(Kind.CALL_EXPRESSION)
		.matches("a(a)")
		.matches("a")
		;
	}
	
	@Test
	public void modelTest() throws Exception {
		CallExpressionTree tree = parse("a(b,c,d,e)", Kind.CALL_EXPRESSION);

		assertThat(tree.is(Kind.CALL_EXPRESSION)).isTrue();
		assertThat(tree.function()).isNull();
		assertThat(tree.functionName()).isNotNull();
		assertThat(tree.parameters()).isNotNull();
		assertThat(tree.parameters().openParenthesis().text()).isEqualTo("(");
		assertThat(tree.parameters().parameters().size()).isEqualTo(4);
		assertThat(tree.parameters().closeParenthesis().text()).isEqualTo(")");
	}
}
