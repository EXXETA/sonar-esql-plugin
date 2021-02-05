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
