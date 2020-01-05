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
package com.exxeta.iss.sonar.esql.checks.verifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.checks.verifier.TestIssue.Location;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;

public class EsqlCheckVerifierTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void parsing_error() throws Exception {
    thrown.expectMessage("Parse error");
    check("foo(");
  }

  @Test
  public void no_issue() throws Exception {
    check("DECLARE A CHAR; -- OK");
  }

  @Test
  public void same_issues() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant \n" +
        "DECLARE B CHAR; -- OK",
      TestIssue.create("msg1", 1));
  }

  @Test
  public void unexpected_issue() throws Exception {
    expect("Unexpected issue at line 2");
    check(
      "DECLARE A CHAR; -- Noncompliant \n" +
        "DECLARE B CHAR; -- OK",
      TestIssue.create("msg1", 1), TestIssue.create("msg1", 2));
  }

  @Test
  public void missing_issue() throws Exception {
    expect("Missing issue at line 2");
    check(
      "DECLARE A CHAR; -- Noncompliant \n" +
        "DECLARE B CHAR; -- Noncompliant",
      TestIssue.create("msg1", 1));
  }

  @Test
  public void too_small_line_number() throws Exception {
    expect("Unexpected issue at line 1");
    check(
      "\nDECLARE A CHAR; -- Noncompliant",
      TestIssue.create("msg1", 1));
  }

  @Test
  public void too_large_line_number() throws Exception {
    expect("Missing issue at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant",
      TestIssue.create("msg1", 2));
  }

  @Test
  public void right_message() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant {{msg1}}",
      TestIssue.create("msg1", 1));
  }

  @Test
  public void wrong_message() throws Exception {
    expect("Bad message at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant {{msg1}}",
      TestIssue.create("msg2", 1));
  }

  @Test
  public void right_effortToFix() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant [[effortToFix=42]] {{msg1}}",
      TestIssue.create("msg1", 1).effortToFix(42));
  }

  @Test
  public void wrong_effortToFix() throws Exception {
    expect("Bad effortToFix at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[effortToFix=42]] {{msg1}}",
      TestIssue.create("msg1", 1).effortToFix(77));
  }

  @Test
  public void invalid_param() throws Exception {
    thrown.expectMessage("Invalid param at line 2: xxx");
    check(
      "\nDECLARE A CHAR; -- Noncompliant [[xxx=1]] {{msg1}}",
      TestIssue.create("msg1", 1));
  }

  @Test
  public void invalid_param_syntax() throws Exception {
    thrown.expectMessage("Invalid param at line 1: zzz");
    check(
      "DECLARE A CHAR; -- Noncompliant [[zzz]] {{msg1}}",
      TestIssue.create("msg1", 1));
  }

  @Test
  public void right_precise_issue_location() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant [[sc=1;ec=4]]",
      TestIssue.create("msg1", 1).columns(1, 4));
  }

  @Test
  public void wrong_start_column() throws Exception {
    expect("Bad start column at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[sc=1;ec=4]]",
      TestIssue.create("msg1", 1).columns(2, 4));
  }

  @Test
  public void wrong_end_column() throws Exception {
    expect("Bad end column at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[sc=1;ec=4]]",
      TestIssue.create("msg1", 1).columns(1, 5));
  }

  @Test
  public void right_end_line() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant [[el=+1]]\n\n",
      TestIssue.create("msg1", 1).endLine(2));
  }

  @Test
  public void wrong_end_line() throws Exception {
    expect("Bad end line at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[el=+2]]\n\n",
      TestIssue.create("msg1", 1).endLine(2));
  }

  @Test
  public void right_secondary_locations() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant [[secondary=2,3]]",
      TestIssue.create("msg1", 1).secondary(2, 3));
  }

  @Test
  public void precise_secondary_locations_message() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE B CHAR;\n" +
        "--S ^^^ A {{Secondary message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
  }

  @Test
  public void precise_secondary_locations() throws Exception {
    String code = "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
      "DECLARE B CHAR;\n" +
      "--S ^^^ A";

    check(code, TestIssue.create("msg1", 1).secondary("Some message", 2, 5, 8));
    check(code, TestIssue.create("msg1", 1).secondary(null, 2, 5, 8));
  }

  @Test
  public void secondary_no_location() throws Exception {
    thrown.expectMessage("Precise location should contain at least one '^' for comment at line 3");
    check(
      "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE B CHAR;\n" +
        "--S A {{Secondary message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 3, 5, 8));
  }

  @Test
  public void secondary_id_not_found() throws Exception {
    thrown.expectMessage("Invalid test file: precise secondary location is provided for ID 'B' but no issue is asserted with such ID (line 3)");
    check(
      "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE B CHAR;\n" +
        "--S ^^^ B {{Secondary message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 3, 5, 8));
  }

  @Test
  public void precise_secondary_locations_wrong_line() throws Exception {
    expect("Missing secondary location at line 2 for issue at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE B CHAR;\n" +
        "--S ^^^ A {{Secondary message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 3, 5, 8));
  }

  @Test
  public void precise_secondary_locations_wrong_message() throws Exception {
    expect("Bad secondary location at line 2 (issue at line 1): bad message");
    check(
      "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE B CHAR;\n" +
        "--S ^^^ A {{wrong message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
  }

  @Test
  public void wrong_precise_secondary_location_start_column() throws Exception {
    expect("Bad secondary location at line 2 (issue at line 1): bad start column");
    check(
       "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE A CHAR;\n" +
        "--S  ^^ A {{Secondary message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
  }

  @Test
  public void wrong_precise_secondary_location_end_column() throws Exception {
    expect("Bad secondary location at line 2 (issue at line 1): bad end column");
    check(
       "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
        "DECLARE B CHAR;\n" +
        "--S ^^ A {{Secondary message}}",
      TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
  }

  @Test
  public void wrong_secondary_locations() throws Exception {
    expect("Missing secondary location at line 3 for issue at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[secondary=2,3]]",
      TestIssue.create("msg1", 1).secondary(2, 4));
  }

  @Test
  public void no_secondary_locations() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant [[secondary=]]",
      TestIssue.create("msg1", 1).secondary());
  }

  @Test
  public void no_secondary_location_fails() throws Exception {
    expect("Unexpected secondary location at line 2 for issue at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant [[secondary=1]]",
      TestIssue.create("msg1", 1).secondary(1).secondary(2));
  }

  @Test
  public void unordered_issues() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant\n" +
        "DECLARE B CHAR; -- Noncompliant",
      TestIssue.create("msg1", 2), TestIssue.create("msg1", 1));
  }

  @Test
  public void right_columns_for_primary_location() throws Exception {
    check(
      "DECLARE A CHAR; -- Noncompliant\n" +
      "-- ^^",
      TestIssue.create("msg1", 1).columns(4, 6));
  }

  @Test
  public void wrong_start_column_for_primary_location() throws Exception {
    expect("Bad start column at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant\n" +
      "--  ^",
      TestIssue.create("msg1", 1).columns(4, 6));
  }

  @Test
  public void wrong_end_column_for_primary_location() throws Exception {
    expect("Bad end column at line 1");
    check(
      "DECLARE A CHAR; -- Noncompliant\n" +
        "-- ^^^",
      TestIssue.create("msg1", 1).columns(4, 6));
  }

  @Test
  public void precise_location_without_any_issue_assertion() throws Exception {
    thrown.expectMessage("Invalid test file: a precise location is provided at line 2 but no issue is asserted at line 1");
    check(
      "DECLARE A CHAR;\n" +
      "-- ^^");
  }

  @Test
  public void precise_location_with_no_assertion_on_previous_line() throws Exception {
    thrown.expectMessage("Invalid test file: a precise location is provided at line 3 but no issue is asserted at line 2");
    check(
      "DECLARE A CHAR; -- Noncompliant\n" +
      "DECLARE B CHAR;\n" +
      "-- ^^");
  }

  @Test
  public void precise_location_with_comment_starting_after_column_1() throws Exception {
    thrown.expectMessage("Line 2: comments asserting a precise location should start at column 1");
    check(
      "DECLARE A CHAR; -- Noncompliant\n" +
      "   -- ^^");
  }

  @Test
  public void start_line() throws Exception {
    check(
      "-- Noncompliant@+1{{msg1}}\n" +
      "DECLARE A CHAR;", TestIssue.create("msg1", 2));

    check(
      "-- Noncompliant@+1[[]]\n" +
      "DECLARE A CHAR;", TestIssue.create("msg1", 2));

    check(
      "-- Noncompliant@+1 {{msg1}}\n" +
      "DECLARE A CHAR;", TestIssue.create("msg1", 2));

    check(
      "-- Noncompliant@+2\n\n" +
      "DECLARE A CHAR;", TestIssue.create("msg1", 3));
  }

  private void expect(String exceptionMessage) {
    thrown.expect(AssertionError.class);
    thrown.expectMessage(exceptionMessage);
  }

  private void check(String sourceCode, TestIssue... actualIssues) throws Exception {
    EsqlCheck check = new CheckStub(Arrays.asList(actualIssues));
    DefaultInputFile fakeFile = TestUtils.createTestInputFile(folder.getRoot(), "fakeFile.txt");
    fakeFile.setCharset(StandardCharsets.UTF_8);
    Files.write(fakeFile.path(), sourceCode.getBytes(fakeFile.charset()));
    EsqlCheckVerifier.verify(check, fakeFile);
  }

  private static class CheckStub implements EsqlCheck {

    private final List<TestIssue> testIssues;
    private List<Issue> issues;

    public CheckStub(List<TestIssue> testIssues) {
      this.testIssues = testIssues;
    }

    @Override
    public PreciseIssue addIssue(Tree tree, String message) {
      throw new NotImplementedException();
    }

    @Override
    public <T extends Issue> T addIssue(T issue) {
      throw new NotImplementedException();
    }

    @Override
    public List<Issue> scanFile(TreeVisitorContext context) {
      issues = new ArrayList<>();
      for (TestIssue issue : testIssues) {
        log(issue);
      }
      return issues;
    }

    public void log(TestIssue issue) {
      if (issue.startColumn() != null || issue.endLine() != null || !issue.secondaryLocations().isEmpty()) {
        Tree tree = createTree(issue.line(), issue.startColumn(), issue.endLine(), issue.endColumn());
        PreciseIssue preciseIssue = new PreciseIssue(this, new IssueLocation(tree, issue.message()));

        for (Location secondaryLocation : issue.secondaryLocations()) {
          int startColumn = secondaryLocation.startColumn() == null ? 1 : secondaryLocation.startColumn();
          int endColumn = secondaryLocation.endColumn() == null ? 1 : secondaryLocation.endColumn();
          preciseIssue.secondary(new IssueLocation(createTree(secondaryLocation.line(), startColumn, secondaryLocation.line(), endColumn), secondaryLocation.message()));
        }
        issues.add(preciseIssue);

      } else {
        LineIssue lineIssue = new LineIssue(this, issue.line(), issue.message());
        if (issue.effortToFix() != null) {
          lineIssue.cost(issue.effortToFix());
        }
        issues.add(lineIssue);
      }
    }

    private Tree createTree(int line, Integer startColumn, Integer endLine, Integer endColumn) {
      EsqlTree tree = mock(EsqlTree.class);
      when(tree.firstToken()).thenReturn(createToken(line, startColumn, 1));
      when(tree.lastToken()).thenReturn(createToken(endLine == null ? line : endLine, endColumn, 0));
      return tree;
    }

    private static InternalSyntaxToken createToken(int line, @Nullable Integer column, int length) {
      String tokenValue = StringUtils.repeat("x", length);
      int tokenColumn = column == null ? 0 : column - 1;
      return new InternalSyntaxToken(line, tokenColumn, tokenValue, ImmutableList.<SyntaxTrivia>of(), 0, false);
    }
  }

}
