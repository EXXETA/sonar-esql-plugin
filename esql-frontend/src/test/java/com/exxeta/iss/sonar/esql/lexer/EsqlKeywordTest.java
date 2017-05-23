package com.exxeta.iss.sonar.esql.lexer;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;

import static org.assertj.core.api.Assertions.assertThat;
public class EsqlKeywordTest {

	  @Test
	  public void esqlReservedKeyword() {
	    assertThat(EsqlReservedKeyword.values().length).isEqualTo(12);
	    assertThat(EsqlReservedKeyword.keywordValues().length).isEqualTo(EsqlReservedKeyword.values().length);

	    for (EsqlReservedKeyword keyword : EsqlReservedKeyword.values()) {
	      assertThat(keyword.getName()).isEqualTo(keyword.name());
	    }
	  }

	  @Test
	  public void esqlNonReservedKeyword() {
	    assertThat(EsqlNonReservedKeyword.values().length).isEqualTo(212);
	    assertThat(EsqlNonReservedKeyword.keywordValues().length).isEqualTo(EsqlNonReservedKeyword.values().length);

	    for (EsqlNonReservedKeyword keyword : EsqlNonReservedKeyword.values()) {
	      assertThat(keyword.getName()).isEqualTo(keyword.name());
	    }
	  }

}
