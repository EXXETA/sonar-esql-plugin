package com.exxeta.iss.sonar.esql.utils;

import org.junit.Before;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

import com.exxeta.iss.sonar.esql.parser.EsqlGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.parser.TreeFactory;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.typed.ActionParser;
import com.sonar.sslr.api.typed.AstNodeBuilder;

public class LegacyParserTest {

  protected LexerlessGrammar g;

  @Before
  public void setUp() throws Exception {
    LexerlessGrammarBuilder b = EsqlLegacyGrammar.createGrammarBuilder();
    // Only called to completed the grammar
    new ActionParser<AstNode>(Charsets.UTF_8, b, EsqlGrammar.class, new TreeFactory(), new AstNodeBuilder(), EsqlLegacyGrammar.SPACING);
    g = b.build();
  }

}
