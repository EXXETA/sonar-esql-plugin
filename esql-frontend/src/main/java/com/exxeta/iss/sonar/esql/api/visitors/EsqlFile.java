package com.exxeta.iss.sonar.esql.api.visitors;

public interface EsqlFile {

  String relativePath();

  /**
   * File name with extension
   */
  String fileName();

  String contents();
}
