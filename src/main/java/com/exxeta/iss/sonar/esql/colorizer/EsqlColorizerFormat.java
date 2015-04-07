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

package com.exxeta.iss.sonar.esql.colorizer;

import com.exxeta.iss.sonar.esql.api.EsqlKeyword;
import com.exxeta.iss.sonar.esql.core.Esql;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;
import com.google.common.collect.ImmutableList;

import org.sonar.api.web.CodeColorizerFormat;
import org.sonar.colorizer.*;

import java.util.List;

public class EsqlColorizerFormat extends CodeColorizerFormat {

  public EsqlColorizerFormat() {
    super(Esql.KEY);
  }

  @Override
  public List<Tokenizer> getTokenizers() {
    return ImmutableList.of(
    	new RegexpTokenizer("<span class=\"s\">", "</span>", EsqlLexer.COMMENT),
        new StringTokenizer("<span class=\"s\">", "</span>"),
        new CDocTokenizer("<span class=\"cd\">", "</span>"),
        new JavadocTokenizer("<span class=\"cppd\">", "</span>"),
        new CppDocTokenizer("<span class=\"cppd\">", "</span>"),
        new CaseInsensitiveKeywordsTokenizer("<span class=\"k\">", "</span>", EsqlKeyword.keywordValues()));
  }

}
