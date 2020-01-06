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
package com.exxeta.iss.sonar.msgflow.check.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.visitors.Issue;
import com.exxeta.iss.sonar.msgflow.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.exxeta.iss.sonar.msgflow.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.msgflow.check.utils.MsgflowCheckTest;
import com.exxeta.iss.sonar.msgflow.check.utils.TestIssue;
import com.exxeta.iss.sonar.msgflow.check.utils.TestIssue.Location;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;

public class MsgflowCheckVerifier {

	private static class IssueToLine implements Function<Issue, Integer> {
		@Override
		public Integer apply(final Issue issue) {
			return line(issue);
		}
	}

	private static void assertSecondary(final Issue actualIssue, final TestIssue expectedIssue) {
		final List<Location> expectedLocations = expectedIssue.secondaryLocations();
		final List<IssueLocation> actualLocations = actualIssue instanceof PreciseIssue
				? ((PreciseIssue) actualIssue).secondaryLocations()
				: new ArrayList<>();

		final String format = "Bad secondary location at line %s (issue at line %s): %s";

		for (final Location expected : expectedLocations) {
			final IssueLocation actual = secondary(expected.line(), actualLocations);

			if (actual != null) {
				if (expected.message() != null) {
					assertThat(actual.message())
							.as(String.format(format, expected.line(), line(actualIssue), "bad message"))
							.isEqualTo(expected.message());
				}
				if (expected.startColumn() != null) {
					assertThat(actual.startLineOffset() + 1)
							.as(String.format(format, expected.line(), line(actualIssue), "bad start column"))
							.isEqualTo(expected.startColumn());
					assertThat(actual.endLineOffset() + 1)
							.as(String.format(format, expected.line(), line(actualIssue), "bad end column"))
							.isEqualTo(expected.endColumn());
				}
				actualLocations.remove(actual);
			} else {
				throw new AssertionError("Missing secondary location at line " + expected.line() + " for issue at line "
						+ expectedIssue.line());
			}
		}

		if (!actualLocations.isEmpty()) {
			final IssueLocation location = actualLocations.get(0);
			throw new AssertionError("Unexpected secondary location at line " + location.startLine()
					+ " for issue at line " + line(actualIssue));
		}
	}

	/**
	 * To use this message you should provide a comment on each line of the source
	 * file where you expect an issue. For example:
	 *
	 * <pre>
	 * SET x = 1; -- Noncompliant {{A message for this line.}}
	 *
	 * function foo() BEGIN -- Noncompliant [[effortToFix=2]] [[secondary=+0,+1]]
	 * 					    -- [[sc=5;ec=6;el=+0]]
	 * END
	 * </pre>
	 *
	 * How to write these comments:
	 * <ul>
	 * <li>Put a comment starting with "Noncompliant" if you expect an issue on this
	 * line.</li>
	 * <li>If for some reason you can't put comment on a line with issue, put "@+N"
	 * just after "Noncompliant" for issue located N lines after the one with
	 * comment</li>
	 * <li>In double curly braces <code>{{MESSAGE}}</code> provide expected
	 * message.</li>
	 * <li>In double brackets provide expected effort to fix (cost) with
	 * <code>effortToFix</code> keyword.</li>
	 * <li>In double brackets provide precise location description with
	 * <code>sc, ec, el</code> keywords respectively for start column, end column
	 * and end line.</li>
	 * <li>Since version 2.13 to specify precise location you can provide separate
	 * comment on next line which contains symbol <code>^</code> under each
	 * character of issue location.</li>
	 *
	 * <pre>
	 * x = a && a; -- Noncompliant
	 * -- ^^
	 * </pre>
	 *
	 * <li>Since version 2.15 to specify precise secondary location you can provide
	 * separate comment on next line which contains symbol <code>^</code> under each
	 * character of secondary issue location. Start comment with "S" symbol and put
	 * after "^" ID of issue (specify it in double square brackets). You can
	 * optionally provide message of secondary location.</li>
	 *
	 * <pre>
	 * x = a && a; -- Noncompliant [[id=SomeID]]
	 * -- S ^^ SomeID {{secondary message}}
	 * </pre>
	 *
	 * <li>In double brackets provide secondary locations with
	 * <code>secondary</code> keyword.</li>
	 * <li>To specify the line you can use relative location by putting
	 * <code>+</code> or <code>-</code>.</li>
	 * <li>All listed elements are optional (except "Noncompliant").</li>
	 * </ul>
	 *
	 * Example of call:
	 *
	 * <pre>
	 * EsqlCheckVerifier.verify(new MyCheck(), myFile));
	 * </pre>
	 */
	/*
	 * public static void verify(MsgflowCheck check, File file) { verify(check,
	 * TestUtils.createTestInputFile(file.getAbsolutePath())); }
	 *
	 * static void verify(MsgflowCheck check, InputFile file) { EsqlVisitorContext
	 * context = TestUtils.createContext(file);
	 *
	 * List<TestIssue> expectedIssues =
	 * ExpectedIssuesParser.parseExpectedIssues(context); Iterator<Issue>
	 * actualIssues = getActualIssues(check, context);
	 *
	 * for (TestIssue expected : expectedIssues) { if (actualIssues.hasNext()) {
	 * verifyIssue(expected, actualIssues.next()); } else { throw new
	 * AssertionError("Missing issue at line " + expected.line()); } }
	 *
	 * if (actualIssues.hasNext()) { Issue issue = actualIssues.next(); throw new
	 * AssertionError("Unexpected issue at line " + line(issue) + ": \"" +
	 * message(issue) + "\""); } }
	 */

	private static Iterator<Issue> getActualIssues(final MsgflowCheck check, final MsgflowVisitorContext context) {
		final MsgflowCheck checkToRun = check;

		final List<Issue> issues = checkToRun.scanFile(context);
		final List<Issue> sortedIssues = Ordering.natural().onResultOf(new IssueToLine()).sortedCopy(issues);
		return sortedIssues.iterator();
	}

	/**
	 * Example:
	 *
	 * <pre>
	 * EsqlCheckVerifier.issues(new MyCheck(), myFile))
	 *    .next().atLine(2).withMessage("This is message for line 2.")
	 *    .next().atLine(3).withMessage("This is message for line 3.").withCost(2.)
	 *    .next().atLine(8)
	 *    .noMore();
	 * </pre>
	 */
	public static CheckMessagesVerifier issues(final MsgflowCheck check, final File file) {
		return CheckMessagesVerifier.verify(MsgflowCheckTest.getIssues(file.getAbsolutePath(), check));
	}

	private static int line(final Issue issue) {
		if (issue instanceof PreciseIssue) {
			return ((PreciseIssue) issue).primaryLocation().startLine();
		} else {
			return -1;
		}
	}

	private static String message(final Issue issue) {
		if (issue instanceof PreciseIssue) {
			return ((PreciseIssue) issue).primaryLocation().message();
		} else {
			return null;
		}
	}

	private static IssueLocation secondary(final int line, final List<IssueLocation> allSecondaryLocations) {
		for (final IssueLocation location : allSecondaryLocations) {
			if (location.startLine() == line) {
				return location;
			}
		}
		return null;
	}

	private static void verifyIssue(final TestIssue expected, final Issue actual) {
		if (line(actual) > expected.line()) {
			fail("Missing issue at line " + expected.line());
		}
		if (line(actual) < expected.line()) {
			fail("Unexpected issue at line " + line(actual) + ": \"" + message(actual) + "\"");
		}
		if (expected.message() != null) {
			assertThat(message(actual)).as("Bad message at line " + expected.line()).isEqualTo(expected.message());
		}
		if (expected.effortToFix() != null) {
			assertThat(actual.cost()).as("Bad effortToFix at line " + expected.line())
					.isEqualTo((double) expected.effortToFix());
		}
		if (expected.startColumn() != null) {
			assertThat(((PreciseIssue) actual).primaryLocation().startLineOffset() + 1)
					.as("Bad start column at line " + expected.line()).isEqualTo(expected.startColumn());
		}
		if (expected.endColumn() != null) {
			assertThat(((PreciseIssue) actual).primaryLocation().endLineOffset() + 1)
					.as("Bad end column at line " + expected.line()).isEqualTo(expected.endColumn());
		}
		if (expected.endLine() != null) {
			assertThat(((PreciseIssue) actual).primaryLocation().endLine())
					.as("Bad end line at line " + expected.line()).isEqualTo(expected.endLine());
		}
		if (!expected.secondaryLocations().isEmpty()) {
			assertSecondary(actual, expected);
		}
	}

	private MsgflowCheckVerifier() {
	}

}
