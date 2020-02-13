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

package com.exxeta.iss.sonar.esql.check;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;


@Rule(key = "TrailingWhitespace")
public class TrailingWhitespaceCheck extends SubscriptionVisitorCheck {

	 private static final String MESSAGE = "Remove the useless trailing whitespaces at the end of this line.";

	  @Override
	  public Set<Tree.Kind> nodesToVisit() {
	    return Collections.emptySet();
	  }

	  @Override
	  public void visitFile(Tree scriptTree) {
	    EsqlFile javaScriptFile = getContext().getEsqlFile();
	    List<String> lines = CheckUtils.readLines(javaScriptFile);

	    for (int i = 0; i < lines.size(); i++) {
	      String line = lines.get(i);

	      if (line.length() > 0 && Pattern.matches("[" + EsqlLexer.WHITESPACE + "]", line.subSequence(line.length() - 1, line.length()))) {
	        addIssue(new LineIssue(this, i + 1, MESSAGE));
	      }
	    }

	  }

	
}
