/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

class TestIssue {

  public static class Location {

    private String message;
    private int line;
    private Integer startColumn = null;
    private Integer endColumn = null;
    private Integer endLine = null;

    private Location(@Nullable String message, int line) {
      this.message = message;
      this.line = line;
    }

    public String message() {
      return message;
    }

    public int line() {
      return line;
    }

    public Integer startColumn() {
      return startColumn;
    }

    public Integer endColumn() {
      return endColumn;
    }
  }

  private String id = null;

  private Integer effortToFix = null;

  private Location primaryLocation;
  private List<Location> secondaryLocations = new ArrayList<>();


  private TestIssue(@Nullable String message, int line) {
    primaryLocation = new Location(message, line);
  }

  public static TestIssue create(@Nullable String message, int lineNumber) {
    return new TestIssue(message, lineNumber);
  }

  public void id(String value) {
    this.id = value;
  }

  public TestIssue message(String message) {
    this.primaryLocation.message = message;
    return this;
  }

  public TestIssue columns(int startColumn, int endColumn) {
    startColumn(startColumn);
    endColumn(endColumn);
    return this;
  }

  public TestIssue startColumn(int startColumn) {
    this.primaryLocation.startColumn = startColumn;
    return this;
  }

  public TestIssue endColumn(int endColumn) {
    this.primaryLocation.endColumn = endColumn;
    return this;
  }

  public TestIssue effortToFix(int effortToFix) {
    this.effortToFix = effortToFix;
    return this;
  }

  public TestIssue endLine(int endLine) {
    this.primaryLocation.endLine = endLine;
    return this;
  }

  public TestIssue secondary(Integer... lines) {
    return this.secondary(Arrays.asList(lines));
  }

  public TestIssue secondary(List<Integer> secondaryLines) {
    for (int line : secondaryLines) {
      secondaryLocations.add(new Location(null, line));
    }
    return this;
  }

  public TestIssue secondary(@Nullable String message, int line, int startColumn, int endColumn) {
    Location location = new Location(message, line);
    location.startColumn = startColumn;
    location.endColumn = endColumn;
    secondaryLocations.add(location);
    return this;
  }

  public int line() {
    return primaryLocation.line;
  }

  public Integer startColumn() {
    return primaryLocation.startColumn;
  }

  public Integer endLine() {
    return primaryLocation.endLine;
  }

  public Integer endColumn() {
    return primaryLocation.endColumn;
  }

  public String message() {
    return primaryLocation.message;
  }

  public String id() {
    return id;
  }

  public Integer effortToFix() {
    return effortToFix;
  }

  public List<Location> secondaryLocations() {
    return secondaryLocations;
  }

}
