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
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

/**
 * Note that implementation differs from AbstractNestedIfCheck - see SONARPLUGINS-1855 and SONARPLUGINS-2178
 */
@Rule(
  key = "NestedIfDepth",
  priority = Priority.MINOR,
  description="nested if description")
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MINOR)
@NoSqale
public class NestedIfDepthCheck extends SquidCheck<Grammar> {

  private int nestingLevel;

  private static final int DEFAULT_MAXIMUM_NESTING_LEVEL = 5;

  @RuleProperty(
    key = "maximumNestingLevel",
    description = "the maxmimum if depth.",
    defaultValue = "" + DEFAULT_MAXIMUM_NESTING_LEVEL)
  public int maximumNestingLevel = DEFAULT_MAXIMUM_NESTING_LEVEL;

  public int getMaximumNestingLevel() {
    return maximumNestingLevel;
  }

  @Override
  public void init() {
    subscribeTo(EsqlGrammar.ifStatement);
  }

  @Override
  public void visitFile(AstNode astNode) {
    nestingLevel = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isElseIf(astNode)) {
      nestingLevel++;
      if (nestingLevel == getMaximumNestingLevel() + 1) {
        getContext().createLineViolation(this, "This if has a nesting level of {0}, which is higher than the maximum allowed {1}.", astNode,
            nestingLevel,
            getMaximumNestingLevel());
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (!isElseIf(astNode)) {
      nestingLevel--;
    }
  }

  private boolean isElseIf(AstNode astNode) {
    return astNode.getParent().getPreviousSibling() != null
        && astNode.getParent().getPreviousSibling().getTokenValue().equals("ELSEIF");
  }

}
