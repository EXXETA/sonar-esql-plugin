/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import java.util.Set;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.google.common.collect.ImmutableSet;

/**
 * This java class is created to implement the logic for checking if the line
 * contains both code and comments, if it contains both then trailing comments
 * should be removed.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

@Rule(key = "TrailingComments")
public class TrailingCommentsCheck extends SubscriptionVisitorCheck {

	  private static final String MESSAGE = "Move this trailing comment on the previous empty line.";

	  private static final String DEFAULT_LEGAL_COMMENT_PATTERN = "^--NOSONAR\\s*";

	  @RuleProperty(
	    key = "legalCommentPattern",
	    description = "Pattern for text of trailing comments that are allowed.",
	    defaultValue = DEFAULT_LEGAL_COMMENT_PATTERN)
	  private String legalCommentPattern = DEFAULT_LEGAL_COMMENT_PATTERN;

	  private Pattern pattern;
	  private int previousTokenLine;

	  @Override
	  public Set<Tree.Kind> nodesToVisit() {
	    return ImmutableSet.of(Tree.Kind.TOKEN);
	  }

	  @Override
	  public void visitFile(Tree tree) {
	    previousTokenLine = -1;
	    pattern = Pattern.compile(legalCommentPattern);
	  }

	  @Override
	  public void visitNode(Tree tree) {
	    SyntaxToken token = (SyntaxToken) tree;
	    for (SyntaxTrivia trivia : token.trivias()) {
	      if (trivia.line() == previousTokenLine) {
	        String comment = trivia.text();
	        if (comment.startsWith("--") && !pattern.matcher(comment).matches()) {
	          addIssue(trivia, MESSAGE);
	        }
	      }
	    }
	    previousTokenLine = token.line();
	  }

	  public void setLegalCommentPattern(String pattern) {
	    this.legalCommentPattern = pattern;
	  }
	
}
