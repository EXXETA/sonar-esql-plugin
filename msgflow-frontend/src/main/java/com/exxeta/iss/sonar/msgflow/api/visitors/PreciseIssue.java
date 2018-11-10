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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.tree.MsgflowTree;

public class PreciseIssue implements Issue {
	private final MsgflowCheck check;
	private Double cost;
	private final IssueLocation primaryLocation;
	private final List<IssueLocation> secondaryLocations;

	public PreciseIssue(final MsgflowCheck check, final IssueLocation primaryLocation) {
		this.check = check;
		this.primaryLocation = primaryLocation;
		secondaryLocations = new ArrayList<>();
		cost = null;
	}

	@Override
	public MsgflowCheck check() {
		return check;
	}

	@Nullable
	@Override
	public Double cost() {
		return cost;
	}

	@Override
	public PreciseIssue cost(final double cost) {
		this.cost = cost;
		return this;
	}

	public IssueLocation primaryLocation() {
		return primaryLocation;
	}

	public PreciseIssue secondary(final IssueLocation secondaryLocation) {
		secondaryLocations.add(secondaryLocation);
		return this;
	}

	public PreciseIssue secondary(final MsgflowTree tree) {
		secondaryLocations.add(new IssueLocation(tree, null));
		return this;
	}

	public PreciseIssue secondary(final MsgflowTree tree, final String message) {
		secondaryLocations.add(new IssueLocation(tree, message));
		return this;
	}

	public List<IssueLocation> secondaryLocations() {
		return secondaryLocations;
	}

}
