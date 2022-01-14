package com.exxeta.iss.sonar.esql.tree.symbols.type;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IsExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.PrefixExpressionTreeImpl;

class PrimitiveOperationsTest {
	@Test

	void test() {
		assertThat(PrimitiveOperations.getType(new PrefixExpressionTreeImpl(Tree.Kind.UNARY_MINUS, null,new IsExpressionTreeImpl(null, null, null, null, null) ))).isEqualTo(PrimitiveType.INTEGER);
		assertThat(PrimitiveOperations.getType(new PrefixExpressionTreeImpl(Tree.Kind.LOGICAL_COMPLEMENT, null,new IsExpressionTreeImpl(null, null, null, null, null) ))).isEqualTo(PrimitiveType.BOOLEAN);
		assertThat(PrimitiveOperations.getType(new PrefixExpressionTreeImpl(Tree.Kind.ARGUMENTS, null,new IsExpressionTreeImpl(null, null, null, null, null) ))).isEqualTo(null);
	}
	
}
