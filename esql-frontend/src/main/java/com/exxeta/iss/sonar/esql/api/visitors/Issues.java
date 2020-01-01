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
package com.exxeta.iss.sonar.esql.api.visitors;

import java.util.ArrayList;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.Tree;

public class Issues {

  private List<Issue> issueList;
  private EsqlCheck check;

  public Issues(EsqlCheck check) {
    this.check = check;
    this.issueList = new ArrayList<>();
  }

  public LineIssue addLineIssue(Tree tree, String message) {
    return addIssue(new LineIssue(check, tree, message));
  }

  public PreciseIssue addIssue(Tree tree, String message) {
    PreciseIssue preciseIssue = new PreciseIssue(check, new IssueLocation(tree, message));
    addIssue(preciseIssue);
    return preciseIssue;
  }

  public <T extends Issue> T addIssue(T issue) {
    issueList.add(issue);
    return issue;
  }

  public List<Issue> getList() {
    return issueList;
  }

  public void reset() {
    issueList = new ArrayList<>();
  }
}
