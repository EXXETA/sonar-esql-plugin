package com.exxeta.iss.sonar.esql.tree;

import org.sonar.squidbridge.CommentAnalyser;

public class EsqlCommentAnalyser extends CommentAnalyser {

  @Override
  public boolean isBlank(String line) {
    for (int i = 0; i < line.length(); i++) {
      if (Character.isLetterOrDigit(line.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getContents(String comment) {
    if (comment.startsWith("//")) {
      return comment.substring(2);
    } else if (comment.startsWith("/*")) {
      return comment.substring(2, comment.length() - 2);
    } else if (comment.startsWith("<!--")) {
      if (comment.endsWith("-->")) {
        return comment.substring(4, comment.length() - 3);
      }
      return comment.substring(4);
    } else {
      throw new IllegalArgumentException();
    }
  }

}
