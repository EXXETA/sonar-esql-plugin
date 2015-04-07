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
package com.exxeta.iss.sonar.esql.parser;

import java.util.ArrayList;




import org.sonar.sslr.parser.ParserAdapter;
import org.sonar.sslr.text.PreprocessorsChain;

import com.exxeta.iss.sonar.esql.EsqlConfiguration;
import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.events.ParsingEventListener;


public class EsqlParser {
	  private EsqlParser() {
	  }
	public static Parser<EsqlGrammar> create(EsqlConfiguration conf, ParsingEventListener... parsingEventListeners) {
		ArrayList<org.sonar.sslr.text.Preprocessor> list  = new ArrayList<org.sonar.sslr.text.Preprocessor>();
		list.add(new EsqlPreprocessor());
		return new ParserAdapter<EsqlGrammar>(conf.getCharset(), new EsqlGrammarImpl(),new PreprocessorsChain(list));	
		}

}
