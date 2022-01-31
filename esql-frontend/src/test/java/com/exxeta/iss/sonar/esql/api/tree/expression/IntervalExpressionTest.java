package com.exxeta.iss.sonar.esql.api.tree.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.tree.symbols.type.RoutineType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class IntervalExpressionTest extends EsqlTreeModelTest<IntervalExpressionTree> {
	@Test
	void test() throws Exception {
		IntervalExpressionTree tree = parse("(CURRENT_GMTTIME - InputRoot.MQMD.PutTime) MINUTE TO SECOND", Kind.INTERVAL_EXPRESSION);

		assertThat(tree.is(Kind.INTERVAL_EXPRESSION)).isTrue();
		assertThat(tree.openParenToken().text()).isEqualTo("(");
		assertThat(tree.additiveExpression()).isNotNull();
		assertThat(tree.closeParenToken().text()).isEqualTo(")");
		assertThat(tree.intervalQualifier().from()).isNotNull();
		assertThat(tree.intervalQualifier().toKeyword().text()).isEqualTo("TO");
		assertThat(tree.intervalQualifier().to()).isNotNull();
		assertThat(tree.types()).isEmpty();
		((TypableTree)tree).add( RoutineType.create());
		assertThat(tree.types()).hasSize(1);
	}
}
