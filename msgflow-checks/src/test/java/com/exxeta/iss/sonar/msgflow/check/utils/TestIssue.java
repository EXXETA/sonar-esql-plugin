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
package com.exxeta.iss.sonar.msgflow.check.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class TestIssue {

	public static class Location {

		private String message;
		private final int line;
		private Integer startColumn = null;
		private Integer endColumn = null;
		private Integer endLine = null;

		private Location(@Nullable final String message, final int line) {
			this.message = message;
			this.line = line;
		}

		public Integer endColumn() {
			return endColumn;
		}

		public int line() {
			return line;
		}

		public String message() {
			return message;
		}

		public Integer startColumn() {
			return startColumn;
		}
	}

	public static TestIssue create(@Nullable final String message, final int lineNumber) {
		return new TestIssue(message, lineNumber);
	}

	private String id = null;

	private Integer effortToFix = null;
	private final Location primaryLocation;

	private final List<Location> secondaryLocations = new ArrayList<>();

	private TestIssue(@Nullable final String message, final int line) {
		primaryLocation = new Location(message, line);
	}

	public TestIssue columns(final int startColumn, final int endColumn) {
		startColumn(startColumn);
		endColumn(endColumn);
		return this;
	}

	public Integer effortToFix() {
		return effortToFix;
	}

	public TestIssue effortToFix(final int effortToFix) {
		this.effortToFix = effortToFix;
		return this;
	}

	public Integer endColumn() {
		return primaryLocation.endColumn;
	}

	public TestIssue endColumn(final int endColumn) {
		primaryLocation.endColumn = endColumn;
		return this;
	}

	public Integer endLine() {
		return primaryLocation.endLine;
	}

	public TestIssue endLine(final int endLine) {
		primaryLocation.endLine = endLine;
		return this;
	}

	public String id() {
		return id;
	}

	public void id(final String value) {
		id = value;
	}

	public int line() {
		return primaryLocation.line;
	}

	public String message() {
		return primaryLocation.message;
	}

	public TestIssue message(final String message) {
		primaryLocation.message = message;
		return this;
	}

	public TestIssue secondary(final Integer... lines) {
		return this.secondary(Arrays.asList(lines));
	}

	public TestIssue secondary(final List<Integer> secondaryLines) {
		for (final int line : secondaryLines) {
			secondaryLocations.add(new Location(null, line));
		}
		return this;
	}

	public TestIssue secondary(@Nullable final String message, final int line, final int startColumn,
			final int endColumn) {
		final Location location = new Location(message, line);
		location.startColumn = startColumn;
		location.endColumn = endColumn;
		secondaryLocations.add(location);
		return this;
	}

	public List<Location> secondaryLocations() {
		return secondaryLocations;
	}

	public Integer startColumn() {
		return primaryLocation.startColumn;
	}

	public TestIssue startColumn(final int startColumn) {
		primaryLocation.startColumn = startColumn;
		return this;
	}

}
