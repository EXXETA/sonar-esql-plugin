package com.exxeta.iss.sonar.esql.api.tree.symbol.type;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.tree.symbols.type.PrimitiveType;
public class ExpressionTypeTest extends TypeTest{

	  @Before
	  public void setUp() throws Exception {
	    super.setUp("expression.esql");
	  }
	  @Test
	  public void parenthesised() {
	    Symbol par = getSymbol("par1");
	    assertThat(par.types().containsOnlyAndUnique(Type.Kind.FUNCTION)).isFalse();
	    par = getSymbol("par2");
	    //assertThat(par.types()).containsOnly(PrimitiveType.UNKNOWN);
	  }

	  
}
