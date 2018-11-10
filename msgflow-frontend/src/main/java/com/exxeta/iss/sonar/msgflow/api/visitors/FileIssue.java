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

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;

public class FileIssue implements Issue {

	private final MsgflowCheck check;
	private Double cost;
	private final String message;

	public FileIssue(final MsgflowCheck check, final String message) {
		this.check = check;
		this.message = message;
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
	public Issue cost(final double cost) {
		this.cost = cost;
		return this;
	}

	public String message() {
		return message;
	}
}
