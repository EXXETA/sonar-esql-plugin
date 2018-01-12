package com.exxeta.iss.sonar.esql.api.tree.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ParenthesisedExpressionTest extends EsqlTreeModelTest<ParenthesisedExpressionTree> {
	 @Test
	  public void test() throws Exception {
	    ParenthesisedExpressionTree tree = parse("(a)", Kind.PARENTHESISED_EXPRESSION);

	    assertThat(tree.is(Kind.PARENTHESISED_EXPRESSION)).isTrue();
	    assertThat(tree.openParenthesis().text()).isEqualTo("(");
	    assertThat(tree.expression()).isNotNull();
	    assertThat(tree.closeParenthesis().text()).isEqualTo(")");
	  }
}
