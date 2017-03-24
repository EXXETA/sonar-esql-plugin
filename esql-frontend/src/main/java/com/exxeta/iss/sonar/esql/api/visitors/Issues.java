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
