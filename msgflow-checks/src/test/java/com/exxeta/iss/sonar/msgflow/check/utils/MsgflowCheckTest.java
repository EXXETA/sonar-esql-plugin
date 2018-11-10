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
package com.exxeta.iss.sonar.msgflow.check.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.squidbridge.api.CheckMessage;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.visitors.FileIssue;
import com.exxeta.iss.sonar.msgflow.api.visitors.Issue;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.exxeta.iss.sonar.msgflow.api.visitors.PreciseIssue;

public class MsgflowCheckTest {

	private static Collection<CheckMessage> getCheckMessages(final List<Issue> issues) {
		final List<CheckMessage> checkMessages = new ArrayList<>();
		for (final Issue issue : issues) {
			CheckMessage checkMessage;
			if (issue instanceof FileIssue) {
				final FileIssue fileIssue = (FileIssue) issue;
				checkMessage = new CheckMessage(fileIssue.check(), fileIssue.message());

			} else {
				final PreciseIssue preciseIssue = (PreciseIssue) issue;
				checkMessage = new CheckMessage(preciseIssue.check(), preciseIssue.primaryLocation().message());
				checkMessage.setLine(preciseIssue.primaryLocation().startLine());
			}

			if (issue.cost() != null) {
				checkMessage.setCost(issue.cost());
			}

			checkMessages.add(checkMessage);
		}

		return checkMessages;
	}

	public static Collection<CheckMessage> getIssues(final String relativePath, final MsgflowCheck check) {
		final InputFile file = TestUtils.createTestInputFile(relativePath);

		final MsgflowVisitorContext context = TestUtils.createContext(file);
		final List<Issue> issues = check.scanFile(context);

		return getCheckMessages(issues);
	}

	private MsgflowCheckTest() {
	}

}
