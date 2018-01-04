/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.sonar.sslr.api.typed.ActionParser;

public final class EsqlParser extends ActionParser<Tree>{
	  public EsqlParser() {
		  this(EsqlLegacyGrammar.PROGRAM);
	  }
	  
	  public EsqlParser(GrammarRuleKey rootRule) {
		    super(
		      Charset.defaultCharset(),
		      EsqlLegacyGrammar.createGrammarBuilder(),
		      EsqlGrammar.class,
		      new TreeFactory(),
		      new EsqlNodeBuilder(),
		      rootRule);
		  }

		  @Override
		  public Tree parse(File file) {
		    return setParents(super.parse(file));
		  }

		  @Override
		  public Tree parse(String source) {
		    return setParents(super.parse(source));
		  }

		  private static Tree setParents(Tree tree) {
		    EsqlTree jsTree = (EsqlTree) tree;
		    Iterator<Tree> childrenIterator = jsTree.childrenIterator();
		    while (childrenIterator.hasNext()) {
		      EsqlTree child = (EsqlTree) childrenIterator.next();
		      if (child != null) {
		        child.setParent(tree);
		        if (!child.isLeaf()) {
		          setParents(child);
		        }
		      }
		    }
		    return tree;
		  }


}