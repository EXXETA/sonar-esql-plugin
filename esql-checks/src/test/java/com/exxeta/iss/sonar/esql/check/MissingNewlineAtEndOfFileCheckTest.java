package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class MissingNewlineAtEndOfFileCheckTest {

  private static final String DIRECTORY = "src/test/resources/MissingNewlineAtEndOfFileCheck";
  private static final MissingNewlineAtEndOfFileCheck check = new MissingNewlineAtEndOfFileCheck();

  @Test
  public void nok() {
    EsqlCheckVerifier.issues(check, new File(DIRECTORY, "newlineAtEndOfFile_nok.esql"))
      .next()
      .noMore();
  }

  @Test
  public void ok() {
    EsqlCheckVerifier.issues(check, new File(DIRECTORY, "newlineAtEndOfFile_ok.esql"))
      .noMore();
  }

  @Test
  public void empty_lines() {
    EsqlCheckVerifier.issues(check, new File(DIRECTORY, "empty_lines.esql"))
      .noMore();
  }

  @Test
  public void empty() {
    EsqlCheckVerifier.issues(check, new File(DIRECTORY, "empty.esql"))
      .noMore();
  }

}
