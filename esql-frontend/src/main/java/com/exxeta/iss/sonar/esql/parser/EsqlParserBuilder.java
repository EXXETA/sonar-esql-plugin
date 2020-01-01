/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.parser;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.sonar.sslr.api.typed.ActionParser;

public class EsqlParserBuilder {
	 private EsqlParserBuilder() {
	  }
	
	  public static ActionParser<Tree> createParser() {
	    return new EsqlParser();
	  }
	  
	  public static ActionParser<Tree> createParser(GrammarRuleKey rootRule){
		  return new EsqlParser(rootRule);
	  }
}

