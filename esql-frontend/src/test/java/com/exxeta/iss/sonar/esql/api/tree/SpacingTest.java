package com.exxeta.iss.sonar.esql.api.tree;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

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
      .matches("/* comment \n */");
  }

}
