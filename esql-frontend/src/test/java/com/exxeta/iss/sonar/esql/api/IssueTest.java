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
/*
 * SonarQube Esql Plugin
 * Copyright (C) 2011-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.exxeta.iss.sonar.esql.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IssueTest {

  private static final EsqlCheck check = new DoubleDispatchVisitorCheck() { };
  private static final String MESSAGE = "message";
  private static final InternalSyntaxToken token = new InternalSyntaxToken(5, 1, "value", Collections.<SyntaxTrivia>emptyList(), 42, false);


  @Test
  public void test_file_issue() throws Exception {
    FileIssue fileIssue = new FileIssue(check, MESSAGE);

    assertThat(fileIssue.check()).isEqualTo(check);
    assertThat(fileIssue.cost()).isNull();
    assertThat(fileIssue.message()).isEqualTo(MESSAGE);

    fileIssue.cost(42);
    assertThat(fileIssue.cost()).isEqualTo(42);
  }

  @Test
  public void test_line_issue() throws Exception {
    LineIssue lineIssue = new LineIssue(check, 42, MESSAGE);

    assertThat(lineIssue.check()).isEqualTo(check);
    assertThat(lineIssue.cost()).isNull();
    assertThat(lineIssue.message()).isEqualTo(MESSAGE);
    assertThat(lineIssue.line()).isEqualTo(42);

    lineIssue.cost(42);
    assertThat(lineIssue.cost()).isEqualTo(42);

    lineIssue = new LineIssue(check, token, MESSAGE);
    assertThat(lineIssue.line()).isEqualTo(5);
  }

  @Test
  public void test_precise_issue() throws Exception {
    IssueLocation primaryLocation = new IssueLocation(token, MESSAGE);
    PreciseIssue preciseIssue = new PreciseIssue(check, primaryLocation);

    assertThat(preciseIssue.check()).isEqualTo(check);
    assertThat(preciseIssue.cost()).isNull();
    assertThat(preciseIssue.primaryLocation()).isEqualTo(primaryLocation);
    assertThat(preciseIssue.secondaryLocations()).isEmpty();

    preciseIssue.cost(42);
    assertThat(preciseIssue.cost()).isEqualTo(42);

    assertThat(primaryLocation.startLine()).isEqualTo(5);
    assertThat(primaryLocation.endLine()).isEqualTo(5);
    assertThat(primaryLocation.startLineOffset()).isEqualTo(1);
    assertThat(primaryLocation.endLineOffset()).isEqualTo(6);
    assertThat(primaryLocation.message()).isEqualTo(MESSAGE);

    preciseIssue
      .secondary(token)
      .secondary(token, "secondary message");

    assertThat(preciseIssue.secondaryLocations()).hasSize(2);
    assertThat(preciseIssue.secondaryLocations().get(0).message()).isNull();
    assertThat(preciseIssue.secondaryLocations().get(1).message()).isEqualTo("secondary message");
  }

  @Test
  public void test_long_issue_location() throws Exception {
    InternalSyntaxToken lastToken = new InternalSyntaxToken(10, 5, "last", Collections.<SyntaxTrivia>emptyList(), 42, false);

    IssueLocation issueLocation = new IssueLocation(token, lastToken, MESSAGE);

    assertThat(issueLocation.startLine()).isEqualTo(5);
    assertThat(issueLocation.endLine()).isEqualTo(10);
    assertThat(issueLocation.startLineOffset()).isEqualTo(1);
    assertThat(issueLocation.endLineOffset()).isEqualTo(9);
    assertThat(issueLocation.message()).isEqualTo(MESSAGE);

  }
}
