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
package com.exxeta.iss.sonar.msgflow.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.msgflow.api.visitors.DoubleDispatchMsgflowVisitor;
import com.exxeta.iss.sonar.msgflow.api.visitors.FileIssue;
import com.exxeta.iss.sonar.msgflow.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorCheck;
import com.exxeta.iss.sonar.msgflow.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;

public class IssueTest {

	private static final MsgflowCheck check = new MsgflowVisitorCheck() {
	};
	private static final String MESSAGE = "message";
	private static final MsgflowTree token = new MsgflowTree(null) {

		@Override
		public void accept(final DoubleDispatchMsgflowVisitor visitor) {
		}

		@Override
		public int endColumn() {
			return 6;
		}

		@Override
		public int endLine() {
			return 5;
		};

		@Override
		public Kind getKind() {
			return null;
		};

		@Override
		public int startColumn() {
			return 1;
		}

		@Override
		public int startLine() {
			return 5;
		};
	};

	@Test
	public void test_file_issue() throws Exception {
		final FileIssue fileIssue = new FileIssue(check, MESSAGE);

		assertThat(fileIssue.check()).isEqualTo(check);
		assertThat(fileIssue.cost()).isNull();
		assertThat(fileIssue.message()).isEqualTo(MESSAGE);

		fileIssue.cost(42);
		assertThat(fileIssue.cost()).isEqualTo(42);
	}

	@Test
	public void test_long_issue_location() throws Exception {
		final MsgflowTree lastToken = new MsgflowTree(null) {

			@Override
			public void accept(final DoubleDispatchMsgflowVisitor visitor) {
			}

			@Override
			public int endColumn() {
				return 9;
			}

			@Override
			public int endLine() {
				return 10;
			}

			@Override
			public Kind getKind() {
				return null;
			}

			@Override
			public int startLine() {
				return 5;
			}
		};

		final IssueLocation issueLocation = new IssueLocation(token, lastToken, MESSAGE);

		assertThat(issueLocation.startLine()).isEqualTo(5);
		assertThat(issueLocation.endLine()).isEqualTo(10);
		assertThat(issueLocation.startLineOffset()).isEqualTo(1);
		assertThat(issueLocation.endLineOffset()).isEqualTo(9);
		assertThat(issueLocation.message()).isEqualTo(MESSAGE);

	}

	@Test
	public void test_precise_issue() throws Exception {
		final IssueLocation primaryLocation = new IssueLocation(token, MESSAGE);
		final PreciseIssue preciseIssue = new PreciseIssue(check, primaryLocation);

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

		preciseIssue.secondary(token).secondary(token, "secondary message");

		assertThat(preciseIssue.secondaryLocations()).hasSize(2);
		assertThat(preciseIssue.secondaryLocations().get(0).message()).isNull();
		assertThat(preciseIssue.secondaryLocations().get(1).message()).isEqualTo("secondary message");
	}
}
