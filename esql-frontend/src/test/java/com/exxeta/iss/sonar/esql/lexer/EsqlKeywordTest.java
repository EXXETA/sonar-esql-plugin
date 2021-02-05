/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	    assertThat(EsqlNonReservedKeyword.values().length).isEqualTo(213);
	    assertThat(EsqlNonReservedKeyword.keywordValues().length).isEqualTo(EsqlNonReservedKeyword.values().length);

	    for (EsqlNonReservedKeyword keyword : EsqlNonReservedKeyword.values()) {
	      assertThat(keyword.getName()).isEqualTo(keyword.name());
	    }
	  }

}
