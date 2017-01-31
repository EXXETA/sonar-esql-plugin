package com.exxeta.iss.sonar.esql.api;

import com.google.common.annotations.Beta;
import java.util.List;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;

/**
 * Marker interface for all ESQL checks.
 */
@Beta
public interface EsqlCheck {

  @Deprecated
  LineIssue addLineIssue(Tree tree, String message);

 
  PreciseIssue addIssue(Tree tree, String message);


  <T extends Issue> T addIssue(T issue);

  List<Issue> scanFile(TreeVisitorContext context);
}