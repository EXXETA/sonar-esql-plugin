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

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;

@BelongsToProfile(title=CheckList.SONAR_WAY_PROFILE,priority=Priority.MINOR)
@Rule(
  key = "LineLength",
  priority = Priority.MINOR,
  description = "Line Length should not be too long.",
  tags=Tags.CONVENTION)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("1min")
public class LineLengthCheck extends SquidCheck<Grammar> implements AstAndTokenVisitor {

  private static final int DEFAULT_MAXIMUM_LINE_LENHGTH = 120;

  @RuleProperty(
    key = "maximumLineLength",
    description="The maximum athorized line length.",
    defaultValue = "" + DEFAULT_MAXIMUM_LINE_LENHGTH)
  public int maximumLineLength = DEFAULT_MAXIMUM_LINE_LENHGTH;

  public int getMaximumLineLength() {
    return maximumLineLength;
  }

  private Token previousToken;

  @Override
  public void visitFile(AstNode astNode) {
    previousToken = null;
  }

  @Override
  public void leaveFile(AstNode astNode) {
    previousToken = null;
  }

  public void visitToken(Token token) {
    if (!token.isGeneratedCode()) {
      if (previousToken != null && previousToken.getLine() != token.getLine()) {
        int length = previousToken.getColumn() + previousToken.getValue().length();
        if (length > getMaximumLineLength()) {
          // Note that method from AbstractLineLengthCheck generates other message - see SONARPLUGINS-1809
          getContext().createLineViolation(this,
              "The line contains {0,number,integer} characters which is greater than {1,number,integer} authorized.",
              previousToken.getLine(),
              length,
              getMaximumLineLength());
        }
      }
      previousToken = token;
    }
  }

}
