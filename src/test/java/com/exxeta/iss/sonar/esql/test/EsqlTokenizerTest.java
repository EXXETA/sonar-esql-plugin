/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql.test;

import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokens;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.cpd.EsqlTokenizer;

import java.io.File;
import java.nio.charset.Charset;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EsqlTokenizerTest {

	@Test
  public void test() {
   EsqlTokenizer tokenizer = new EsqlTokenizer(Charset.forName("UTF-8"));
    SourceCode source = mock(SourceCode.class);
    when(source.getFileName()).thenReturn(new File(
			"src/test/resources/test.esql").getAbsolutePath());
    Tokens tokens = new Tokens();
    tokenizer.tokenize(source, tokens);
    assertThat(tokens.getTokens().size()).isGreaterThan(1);
    assertThat(tokens.getTokens().get(tokens.size() - 1)).isEqualTo(TokenEntry.getEOF());
  }
}
