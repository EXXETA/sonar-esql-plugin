package com.exxeta.iss.sonar.esql.metrics;


import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.tree.EsqlCommentAnalyser;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class CommentLineVisitor extends SubscriptionVisitor {

  private Set<Integer> comments = Sets.newHashSet();
  private Set<Integer> noSonarLines = Sets.newHashSet();

  // seenFirstToken is required to track header comments (header comments are saved as trivias of first non-trivia token)
  private boolean seenFirstToken;

  private EsqlCommentAnalyser commentAnalyser = new EsqlCommentAnalyser();

  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.TOKEN);
  }

  public CommentLineVisitor(Tree tree) {
    this.comments.clear();
    this.noSonarLines.clear();
    this.seenFirstToken = false;
    scanTree(tree);
  }

  @Override
  public void visitNode(Tree tree) {
    for (SyntaxTrivia trivia : ((SyntaxToken) tree).trivias()) {
      if (seenFirstToken) {
        String[] commentLines = commentAnalyser.getContents(trivia.text())
          .split("(\r)?\n|\r", -1);
        int lineNumber = trivia.line();
        for (String commentLine : commentLines) {
          if (commentLine.contains("NOSONAR")) {
            noSonarLines.add(lineNumber);
          } else if (!commentAnalyser.isBlank(commentLine)) {
            comments.add(lineNumber);
          }
          lineNumber++;
        }
      } else {
        seenFirstToken = true;
      }
    }
    seenFirstToken = true;
  }

  public Set<Integer> noSonarLines() {
    return noSonarLines;
  }

  public Set<Integer> getCommentLines() {
    return comments;
  }

  public int getCommentLineNumber() {
    return comments.size();
  }
}
