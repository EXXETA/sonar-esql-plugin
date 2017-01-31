/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
    // Only called to conpleted the grammar
    new ActionParser<AstNode>(Charsets.UTF_8, b, EsqlGrammar.class, new TreeFactory(), new AstNodeBuilder(), EsqlLegacyGrammar.SPACING);
    g = b.build();
  }

}
