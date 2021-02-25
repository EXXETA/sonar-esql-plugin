/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;

class ExpectedIssuesParser extends SubscriptionVisitorCheck {

  private final List<TestIssue> expectedIssues = new ArrayList<>();
  private final List<SyntaxTrivia> preciseSecondaryLocationComments = new ArrayList<>();

  static List<TestIssue> parseExpectedIssues(EsqlVisitorContext context) {
    ExpectedIssuesParser expectedIssuesParser = new ExpectedIssuesParser();
    expectedIssuesParser.scanFile(context);
    expectedIssuesParser.addPreciseSecondaryLocations();

    return expectedIssuesParser.expectedIssues;
  }

  @Override
  public Set<Kind> nodesToVisit() {
    return ImmutableSet.of(Kind.TOKEN);
  }

  @Override
  public void visitNode(Tree tree) {
    SyntaxToken token = (SyntaxToken) tree;
    for (SyntaxTrivia trivia : token.trivias()) {

      String text = trivia.text().substring(2).trim();
      String marker = "Noncompliant";

      if (text.startsWith(marker)) {
        int issueLine = trivia.line();
        String paramsAndMessage = text.substring(marker.length()).trim();

        if (paramsAndMessage.startsWith("@+")) {
          String[] spaceSplit = paramsAndMessage.split("[\\s\\[{]", 2);
          issueLine += Integer.parseInt(spaceSplit[0].substring(2));
          paramsAndMessage = spaceSplit.length > 1 ? spaceSplit[1] : "";
        }

        TestIssue issue = issue(null, issueLine);

        if (paramsAndMessage.startsWith("[[")) {
          int endIndex = paramsAndMessage.indexOf("]]");
          addParams(issue, paramsAndMessage.substring(2, endIndex));
          paramsAndMessage = paramsAndMessage.substring(endIndex + 2).trim();
        }

        if (paramsAndMessage.startsWith("{{")) {
          int endIndex = paramsAndMessage.indexOf("}}");
          String message = paramsAndMessage.substring(2, endIndex);
          issue.message(message);
        }

        expectedIssues.add(issue);

      } else if (text.startsWith("^")) {
        addPreciseLocation(trivia);

      } else if (text.startsWith("S ")) {
        preciseSecondaryLocationComments.add(trivia);

      }
    }
  }

  private void addPreciseSecondaryLocations() {
    for (SyntaxTrivia trivia : preciseSecondaryLocationComments) {
      checkPreciseLocationComment(trivia);

      String text = trivia.text();

      int startColumn = text.indexOf('^') + 1;
      int endColumn = text.lastIndexOf('^') + 2;
      String message = null;

      text = text.substring(endColumn).trim();

      if (text.contains("{{")) {
        int endIndex = text.indexOf("}}");
        int startIndex = text.indexOf("{{") + 2;
        message = text.substring(startIndex, endIndex);
        text = text.substring(0, startIndex - 2).trim();
      }

      issueByID(text, trivia.line()).secondary(message, trivia.line() - 1, startColumn, endColumn);
    }
  }

  private static void checkPreciseLocationComment(SyntaxTrivia trivia) {
    if (trivia.column() > 1) {
      throw new IllegalStateException("Line " + trivia.line() + ": comments asserting a precise location should start at column 1");
    }

    if (!trivia.text().contains("^")) {
      throw new IllegalStateException("Precise location should contain at least one '^' for comment at line " + trivia.line());
    }
  }

  private static void addParams(TestIssue issue, String params) {
    for (String param : Splitter.on(';').split(params)) {
      int equalIndex = getEqualIndex(param, issue);
      String name = param.substring(0, equalIndex);
      String value = param.substring(equalIndex + 1);

      if ("effortToFix".equalsIgnoreCase(name)) {
        issue.effortToFix(Integer.parseInt(value));

      } else if ("sc".equalsIgnoreCase(name)) {
        issue.startColumn(Integer.parseInt(value));

      } else if ("ec".equalsIgnoreCase(name)) {
        issue.endColumn(Integer.parseInt(value));

      } else if ("el".equalsIgnoreCase(name)) {
        issue.endLine(lineValue(issue.line(), value));

      } else if ("secondary".equalsIgnoreCase(name)) {
        addSecondaryLines(issue, value);

      } else if ("id".equalsIgnoreCase(name)) {
        issue.id(value);

      } else {
        throw new IllegalStateException("Invalid param at line " + issue.line() + ": " + name);
      }
    }
  }

  private static int getEqualIndex(String param, TestIssue issue) {
    int equalIndex = param.indexOf('=');
    if (equalIndex == -1) {
      throw new IllegalStateException("Invalid param at line " + issue.line() + ": " + param);
    }
    return equalIndex;
  }

  private static void addSecondaryLines(TestIssue issue, String value) {
    List<Integer> secondaryLines = new ArrayList<>();
    if (!"".equals(value)) {
      for (String secondary : Splitter.on(',').split(value)) {
        secondaryLines.add(lineValue(issue.line(), secondary));
      }
    }
    issue.secondary(secondaryLines);
  }

  private static int lineValue(int baseLine, String shift) {
    if (shift.startsWith("+")) {
      return baseLine + Integer.parseInt(shift.substring(1));
    }
    if (shift.startsWith("-")) {
      return baseLine - Integer.parseInt(shift.substring(1));
    }
    return Integer.parseInt(shift);
  }


  private TestIssue issueByID(String id, int line) {
    for (TestIssue expectedIssue : expectedIssues) {
      if (id.equals(expectedIssue.id())) {
        return expectedIssue;
      }
    }

    String format = "Invalid test file: precise secondary location is provided for ID '%s' but no issue is asserted with such ID (line %s)";
    String missingAssertionMessage = String.format(format, id, line);
    throw new IllegalStateException(missingAssertionMessage);
  }

  private void addPreciseLocation(SyntaxTrivia trivia) {
    checkPreciseLocationComment(trivia);

    int line = trivia.line();
    String text = trivia.text();

    String missingAssertionMessage = String.format("Invalid test file: a precise location is provided at line %s but no issue is asserted at line %s", line, line - 1);
    if (expectedIssues.isEmpty()) {
      throw new IllegalStateException(missingAssertionMessage);
    }
    TestIssue issue = expectedIssues.get(expectedIssues.size() - 1);
    if (issue.line() != line - 1) {
      throw new IllegalStateException(missingAssertionMessage);
    }
    issue.endLine(issue.line());
    issue.startColumn(text.indexOf('^') + 1);
    issue.endColumn(text.lastIndexOf('^') + 2);
  }

  private static TestIssue issue(@Nullable String message, int lineNumber) {
    return TestIssue.create(message, lineNumber);
  }

}
