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
package com.exxeta.iss.sonar.msgflow.api.visitors;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.msgflow.api.tree.Tree;

public class IssueLocation {

	private final Tree firstTree;
	private final Tree lastTree;
	private final String message;

	public IssueLocation(final Tree tree) {
		this(tree, null);
	}

	public IssueLocation(final Tree tree, @Nullable final String message) {
		this(tree, tree, message);
	}

	public IssueLocation(final Tree firstTree, final Tree lastTree, @Nullable final String message) {
		this.firstTree = firstTree;
		this.lastTree = lastTree;
		this.message = message;
	}

	public int endLine() {
		return lastTree.endLine();
	}

	public int endLineOffset() {
		return lastTree.endColumn();
	}

	@Nullable
	public String message() {
		return message;
	}

	public int startLine() {
		return firstTree.startLine();
	}

	public int startLineOffset() {
		return firstTree.startColumn();
	}

}