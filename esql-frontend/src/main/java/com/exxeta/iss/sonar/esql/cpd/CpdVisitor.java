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
package com.exxeta.iss.sonar.esql.cpd;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFileImpl;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;

public class CpdVisitor extends SubscriptionVisitor {

  private final SensorContext sensorContext;
  private InputFile inputFile;
  private NewCpdTokens cpdTokens;

  public CpdVisitor(SensorContext sensorContext) {
    this.sensorContext = sensorContext;
  }

  @Override
  public Set<Kind> nodesToVisit() {
    return ImmutableSet.of(Kind.TOKEN);
  }

  @Override
  public void visitFile(Tree scriptTree) {
    inputFile = ((EsqlFileImpl) getContext().getEsqlFile()).inputFile();
    cpdTokens = sensorContext.newCpdTokens().onFile(inputFile);
    super.visitFile(scriptTree);
  }

  @Override
  public void leaveFile(Tree scriptTree) {
    super.leaveFile(scriptTree);
    cpdTokens.save();
  }

  @Override
  public void visitNode(Tree tree) {
    if (((InternalSyntaxToken) tree).isEOF()) {
      return;
    }

    SyntaxToken token = (SyntaxToken) tree;
    String text = token.text();

    if ( text.startsWith("'") ) {
      text = "LITERAL";
    }

    TextRange range = inputFile.newRange(token.line(), token.column(), token.endLine(), token.endColumn());
    cpdTokens.addToken(range, text);
  }

}
