/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

class MetricsTest extends EsqlTreeModelTest<ProgramTree> {

  @Test
  void complexity() {
    String path = "src/test/resources/metrics/complexity.esql";
    Tree tree = parse(new File(path));
    assertThat(new ComplexityVisitor().getComplexity(tree)).isEqualTo(19);
  }

  @Test
  void lines_of_code() {
    String path = "src/test/resources/metrics/lines_of_code.esql";
    Tree tree = parse(new File(path));
    assertThat(new LineVisitor(tree).getLinesOfCodeNumber()).isEqualTo(4);
  }

  @Test
  void lines() {
    String path = "src/test/resources/metrics/lines.esql";
    ProgramTree tree = parse(new File(path));
    LineVisitor lineVisitor = new LineVisitor(tree);
    assertThat(lineVisitor.getLinesOfCode()).containsOnly(2, 3, 4, 5, 6, 7);

//    lineVisitor = new LineVisitor(tree.items().items());
//    assertThat(lineVisitor.getLinesOfCode()).containsOnly(2, 3, 4);
  }

  @Test
  void functions() {
    String path = "src/test/resources/metrics/functions.esql";
    Tree tree = parse(new File(path));
    assertThat(new CounterVisitor(tree).getFunctionsNumber()).isEqualTo(10);
  }

  @Test
  void statements() {
    String path = "src/test/resources/metrics/functions.esql";
    Tree tree = parse(new File(path));
    assertThat(new CounterVisitor(tree).getStatementsNumber()).isEqualTo(10);

    path = "src/test/resources/metrics/statements.esql";
    tree = parse(new File(path));
    assertThat(new CounterVisitor(tree).getStatementsNumber()).isEqualTo(14);
  }

  @Test
  void modules() {
    String path = "src/test/resources/metrics/modules.esql";
    Tree tree = parse(new File(path));
    assertThat(new CounterVisitor(tree).getModulesNumber()).isEqualTo(3);
  }

  @Test
  void comments() {
    String path = "src/test/resources/metrics/comments.esql";
    Tree tree = parse(new File(path));
    CommentLineVisitor commentLineVisitor = new CommentLineVisitor(tree, true);
    assertThat(commentLineVisitor.getCommentLineNumber()).isEqualTo(2);
    assertThat(commentLineVisitor.noSonarLines()).containsOnly(11);
    assertThat(commentLineVisitor.getCommentLines()).containsOnly(5, 9);

    commentLineVisitor = new CommentLineVisitor(tree, false);
    assertThat(commentLineVisitor.getCommentLineNumber()).isEqualTo(3);
    assertThat(commentLineVisitor.noSonarLines()).containsOnly(11);
  }

  @Test
  void executable_lines() throws Exception {
    Tree tree = parse(new File("src/test/resources/metrics/executable_lines.esql"));
    Set<Integer> commentLines = new CommentLineVisitor(tree, false).getCommentLines();
    Set<Integer> expectedExecutableLines = Sets.difference(commentLines, ImmutableSet.of(1, 3, 6));
    assertThat(new ExecutableLineVisitor(tree).getExecutableLines()).isEqualTo(expectedExecutableLines);
  }

}
