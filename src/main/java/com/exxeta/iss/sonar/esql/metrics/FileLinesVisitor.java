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
package com.exxeta.iss.sonar.esql.metrics;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlMetric;
import com.google.common.collect.Sets;
import com.sonar.sslr.api.*;
import com.sonar.sslr.squid.SquidAstVisitor;




import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;

import java.util.List;
import java.util.Set;

/**
 * Visitor that computes {@link CoreMetrics#NCLOC_DATA_KEY} and {@link CoreMetrics#COMMENT_LINES_DATA_KEY} metrics used by the DevCockpit.
 */
public class FileLinesVisitor extends SquidAstVisitor<EsqlGrammar> implements AstAndTokenVisitor {

  private final Project project;
  private final FileLinesContextFactory fileLinesContextFactory;

  private final Set<Integer> linesOfCode = Sets.newHashSet();
  private final Set<Integer> linesOfComments = Sets.newHashSet();

  public FileLinesVisitor(Project project, FileLinesContextFactory fileLinesContextFactory) {
    this.project = project;
    this.fileLinesContextFactory = fileLinesContextFactory;
  }

  public void visitToken(Token token) {
    if (token.getType().equals(GenericTokenType.EOF)) {
      return;
    }

    linesOfCode.add(token.getLine());
    List<Trivia> trivias = token.getTrivia();
    for (Trivia trivia : trivias) {
      if (trivia.isComment()) {
        linesOfComments.add(trivia.getToken().getLine());
      }
    }
  }

  @Override
  public void leaveFile(AstNode astNode) {
    File sonarFile = File.fromIOFile(getContext().getFile(), project);
    FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(sonarFile);

    int fileLength = getContext().peekSourceCode().getInt(EsqlMetric.LINES);
    for (int line = 1; line <= fileLength; line++) {
      fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, linesOfCode.contains(line) ? 1 : 0);
      fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, linesOfComments.contains(line) ? 1 : 0);
    }
    fileLinesContext.save();

    linesOfCode.clear();
    linesOfComments.clear();
  }

}
