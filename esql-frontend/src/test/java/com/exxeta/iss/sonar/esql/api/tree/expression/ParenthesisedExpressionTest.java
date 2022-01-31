package com.exxeta.iss.sonar.esql.api.tree.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.tree.symbols.type.RoutineType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class ParenthesisedExpressionTest extends EsqlTreeModelTest<ParenthesisedExpressionTree> {
	 @Test
	  void test() throws Exception {
	    ParenthesisedExpressionTree tree = parse("(a)", Kind.PARENTHESISED_EXPRESSION);

	    assertThat(tree.is(Kind.PARENTHESISED_EXPRESSION)).isTrue();
	    assertThat(tree.openParenthesis().text()).isEqualTo("(");
	    assertThat(tree.expression()).isNotNull();
	    assertThat(tree.closeParenthesis().text()).isEqualTo(")");
	    Assertions.assertThat(tree.types()).isEmpty();
		((TypableTree)tree).add( RoutineType.create());
		Assertions.assertThat(tree.types()).hasSize(1);
	  }
}
