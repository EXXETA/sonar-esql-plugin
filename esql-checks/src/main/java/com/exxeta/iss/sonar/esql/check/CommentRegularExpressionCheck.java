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
 */package com.exxeta.iss.sonar.esql.check;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.RuleTemplate;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

@Rule(  key = "CommentRegularExpression" )
public class CommentRegularExpressionCheck  extends SubscriptionVisitorCheck {

  private static final String DEFAULT_REGULAR_EXPRESSION = "";
  private static final String DEFAULT_MESSAGE = "The regular expression matches this comment";

  @RuleProperty(
    key = "regularExpression",
    description = "regular expression",
    defaultValue = "" + DEFAULT_REGULAR_EXPRESSION)
  public String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "message",
    description = "The issue message",
    defaultValue = "" + DEFAULT_MESSAGE)
  public String message = DEFAULT_MESSAGE;

  private Pattern pattern = null;

  public void init() {
    checkNotNull(regularExpression, "getRegularExpression() should not return null");

    if (!Strings.isNullOrEmpty(regularExpression)) {
      try {
        pattern = Pattern.compile(regularExpression, Pattern.DOTALL);
      } catch (RuntimeException e) {
        throw new IllegalStateException("Unable to compile regular expression: " + regularExpression, e);
      }

    } else {
      pattern = null;
    }
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setRegularExpression(String regularExpression) {
    this.regularExpression = regularExpression;
    init();
  }

  @Override
  public void visitNode(Tree tree) {
    if (pattern != null) {
      SyntaxToken token = (SyntaxToken) tree;
      for (SyntaxTrivia trivia : token.trivias()) {
        if (pattern.matcher(trivia.text()).matches()) {
          addIssue(new LineIssue(this, trivia.line(), message));
        }
      }
    }
  }

  @Override
  public Set<Kind> nodesToVisit() {
    return ImmutableSet.of(Kind.TOKEN);
  }

}
