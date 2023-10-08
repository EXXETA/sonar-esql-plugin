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
package com.exxeta.iss.sonar.esql.checks.verifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.AssertionFailedError;
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

class EsqlCheckVerifierTest {

    @TempDir
    File folder;

    @Test
    void parsing_error() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check("foo(");
        });
        assertThat(thrown.getMessage(), containsString("Parse error"));
    }

    @Test
    void no_issue() throws Exception {
        check("DECLARE A CHAR; -- OK");
    }

    @Test
    void same_issues() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant \n" +
                        "DECLARE B CHAR; -- OK",
                TestIssue.create("msg1", 1));
    }

    @Test
    void unexpected_issue() {
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant \n" +
                            "DECLARE B CHAR; -- OK",
                    TestIssue.create("msg1", 1), TestIssue.create("msg1", 2));
        });
        assertThat(thrown.getMessage(), containsString("Unexpected issue at line 2"));
    }

    @Test
    void missing_issue() {
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant \n" +
                            "DECLARE B CHAR; -- Noncompliant",
                    TestIssue.create("msg1", 1));
        });
        assertThat(thrown.getMessage(), is("Missing issue at line 2"));
    }

    @Test
    void too_small_line_number() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "\nDECLARE A CHAR; -- Noncompliant",
                    TestIssue.create("msg1", 1));
        });
        assertThat(thrown.getMessage(), containsString("Unexpected issue at line 1"));
    }

    @Test
    void too_large_line_number() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant",
                    TestIssue.create("msg1", 2));
        });
        assertThat(thrown.getMessage(), is("Missing issue at line 1"));
    }

    @Test
    void right_message() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant {{msg1}}",
                TestIssue.create("msg1", 1));
    }

    @Test
    void wrong_message() {

        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant {{msg1}}",
                    TestIssue.create("msg2", 1));
        });
        assertThat(thrown.getMessage(), containsString("Bad message at line 1"));
    }

    @Test
    void right_effortToFix() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant [[effortToFix=42]] {{msg1}}",
                TestIssue.create("msg1", 1).effortToFix(42));
    }

    @Test
    void wrong_effortToFix() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[effortToFix=42]] {{msg1}}",
                    TestIssue.create("msg1", 1).effortToFix(77));
        });
        assertThat(thrown.getMessage(), containsString("Bad effortToFix at line 1"));
    }

    @Test
    void invalid_param() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "\nDECLARE A CHAR; -- Noncompliant [[xxx=1]] {{msg1}}",
                    TestIssue.create("msg1", 1));
        });
        assertThat(thrown.getMessage(), is("Invalid param at line 2: xxx"));

    }

    @Test
    void invalid_param_syntax() throws Exception {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[zzz]] {{msg1}}",
                    TestIssue.create("msg1", 1));
        });
        assertThat(thrown.getMessage(), is("Invalid param at line 1: zzz"));
    }

    @Test
    void right_precise_issue_location() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant [[sc=1;ec=4]]",
                TestIssue.create("msg1", 1).columns(1, 4));
    }

    @Test
    void wrong_start_column() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[sc=1;ec=4]]",
                    TestIssue.create("msg1", 1).columns(2, 4));
        });
        assertThat(thrown.getMessage(), containsString("Bad start column at line 1"));
    }

    @Test
    void wrong_end_column() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[sc=1;ec=4]]",
                    TestIssue.create("msg1", 1).columns(1, 5));
        });
        assertThat(thrown.getMessage(), containsString("Bad end column at line 1"));
    }

    @Test
    void right_end_line() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant [[el=+1]]\n\n",
                TestIssue.create("msg1", 1).endLine(2));
    }

    @Test
    void wrong_end_line() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[el=+2]]\n\n",
                    TestIssue.create("msg1", 1).endLine(2));
        });
        assertThat(thrown.getMessage(), containsString("Bad end line at line 1"));

    }

    @Test
    void right_secondary_locations() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant [[secondary=2,3]]",
                TestIssue.create("msg1", 1).secondary(2, 3));
    }

    @Test
    void precise_secondary_locations_message() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                        "DECLARE B CHAR;\n" +
                        "--S ^^^ A {{Secondary message}}",
                TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
    }

    @Test
    void precise_secondary_locations() throws Exception {
        String code = "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                "DECLARE B CHAR;\n" +
                "--S ^^^ A";

        check(code, TestIssue.create("msg1", 1).secondary("Some message", 2, 5, 8));
        check(code, TestIssue.create("msg1", 1).secondary(null, 2, 5, 8));
    }

    @Test
    void secondary_no_location() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                            "DECLARE B CHAR;\n" +
                            "--S A {{Secondary message}}",
                    TestIssue.create("msg1", 1).secondary("Secondary message", 3, 5, 8));
        });
        assertThat(thrown.getMessage(), is("Precise location should contain at least one '^' for comment at line 3"));
    }

    @Test
    void secondary_id_not_found() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                            "DECLARE B CHAR;\n" +
                            "--S ^^^ B {{Secondary message}}",
                    TestIssue.create("msg1", 1).secondary("Secondary message", 3, 5, 8));
        });
        assertThat(thrown.getMessage(), is("Invalid test file: precise secondary location is provided for ID 'B' but no issue is asserted with such ID (line 3)"));
    }

    @Test
    void precise_secondary_locations_wrong_line() {
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                            "DECLARE B CHAR;\n" +
                            "--S ^^^ A {{Secondary message}}",
                    TestIssue.create("msg1", 1).secondary("Secondary message", 3, 5, 8));
        });
        assertThat(thrown.getMessage(), is("Missing secondary location at line 2 for issue at line 1"));
    }

    @Test
    void precise_secondary_locations_wrong_message() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                            "DECLARE B CHAR;\n" +
                            "--S ^^^ A {{wrong message}}",
                    TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
        });
        assertThat(thrown.getMessage(), containsString("Bad secondary location at line 2 (issue at line 1): bad message"));
    }

    @Test
    void wrong_precise_secondary_location_start_column() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                            "DECLARE A CHAR;\n" +
                            "--S  ^^ A {{Secondary message}}",
                    TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
        });
        assertThat(thrown.getMessage(), containsString("Bad secondary location at line 2 (issue at line 1): bad start column"));
    }

    @Test
    void wrong_precise_secondary_location_end_column() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[id=A]]\n" +
                            "DECLARE B CHAR;\n" +
                            "--S ^^ A {{Secondary message}}",
                    TestIssue.create("msg1", 1).secondary("Secondary message", 2, 5, 8));
        });
        assertThat(thrown.getMessage(), containsString("Bad secondary location at line 2 (issue at line 1): bad end column"));

    }

    @Test
    void wrong_secondary_locations() {
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[secondary=2,3]]",
                    TestIssue.create("msg1", 1).secondary(2, 4));
        });
        assertThat(thrown.getMessage(), is("Missing secondary location at line 3 for issue at line 1"));
    }

    @Test
    void no_secondary_locations() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant [[secondary=]]",
                TestIssue.create("msg1", 1).secondary());
    }

    @Test
    void no_secondary_location_fails() throws Exception {
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant [[secondary=1]]",
                    TestIssue.create("msg1", 1).secondary(1).secondary(2));
        });
        assertThat(thrown.getMessage(), is("Unexpected secondary location at line 2 for issue at line 1"));

    }

    @Test
    void unordered_issues() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant\n" +
                        "DECLARE B CHAR; -- Noncompliant",
                TestIssue.create("msg1", 2), TestIssue.create("msg1", 1));
    }

    @Test
    void right_columns_for_primary_location() throws Exception {
        check(
                "DECLARE A CHAR; -- Noncompliant\n" +
                        "-- ^^",
                TestIssue.create("msg1", 1).columns(4, 6));
    }

    @Test
    void wrong_start_column_for_primary_location() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant\n" +
                            "--  ^",
                    TestIssue.create("msg1", 1).columns(4, 6));
        });
        assertThat(thrown.getMessage(), containsString("Bad start column at line 1"));
    }

    @Test
    void wrong_end_column_for_primary_location() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant\n" +
                            "-- ^^^",
                    TestIssue.create("msg1", 1).columns(4, 6));
        });
        assertThat(thrown.getMessage(), containsString("Bad end column at line 1"));

    }

    @Test
    void precise_location_without_any_issue_assertion() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "DECLARE A CHAR;\n" +
                            "-- ^^");
        });
        assertThat(thrown.getMessage(), is("Invalid test file: a precise location is provided at line 2 but no issue is asserted at line 1"));
    }

    @Test
    void precise_location_with_no_assertion_on_previous_line() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant\n" +
                            "DECLARE B CHAR;\n" +
                            "-- ^^");
        });
        assertThat(thrown.getMessage(), is("Invalid test file: a precise location is provided at line 3 but no issue is asserted at line 2"));
    }

    @Test
    void precise_location_with_comment_starting_after_column_1() {
        Exception thrown = assertThrows(Exception.class, () -> {
            check(
                    "DECLARE A CHAR; -- Noncompliant\n" +
                            "   -- ^^");
        });
        assertThat(thrown.getMessage(), is("Line 2: comments asserting a precise location should start at column 1"));
    }

    @Test
    void start_line() throws Exception {
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


    private void check(String sourceCode, TestIssue... actualIssues) throws Exception {
        EsqlCheck check = new CheckStub(Arrays.asList(actualIssues));
        DefaultInputFile fakeFile = TestUtils.createTestInputFile(folder, "fakeFile.txt");
        fakeFile.setCharset(StandardCharsets.UTF_8);
        Files.write(fakeFile.path(), sourceCode.getBytes(fakeFile.charset()));
        EsqlCheckVerifier.verify(check, fakeFile);
    }

    private static class CheckStub implements EsqlCheck {

        private final List<TestIssue> testIssues;
        private List<Issue> issues;

        CheckStub(List<TestIssue> testIssues) {
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
