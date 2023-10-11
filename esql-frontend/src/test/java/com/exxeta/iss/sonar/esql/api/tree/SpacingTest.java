/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

public class SpacingTest extends com.exxeta.iss.sonar.esql.utils.LegacyParserTest {

  @Test
  public void ok() {
    assertThat(g.rule(EsqlLegacyGrammar.SPACING))
      // must allow empty matches, otherwise "optional(SPACING)" will be used everywhere in grammar,
      // which leads to dramatic degradation of performance
      .matches("")

      .as("Whitespace")
      .matches(" ")
      .matches("\n")
      .matches("\r")
      .matches("\r\n")
      .notMatches("aaa")

      .as("SingleLineComment")
      .matches("-- comment")
      .matches("-- comment \n")

      .as("MultiLineComment")
            .matches("/* comment */")
            .matches("/* /* comment */ */")
            .matches("/* comment \n */")
            .notMatches("/* /* comment */");
  }

}
