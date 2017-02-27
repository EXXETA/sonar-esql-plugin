package com.exxeta.iss.sonar.esql.checks.verifier;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.sonar.api.batch.fs.internal.DefaultInputFile;

public class TestInputFile extends DefaultInputFile {
  public TestInputFile(String relativePath) {
    this("", relativePath);
  }

  public TestInputFile(String baseDir, String relativePath) {
    super("module1", relativePath);
    this.setModuleBaseDir(Paths.get(baseDir))
      .setLanguage("js")
      .setCharset(StandardCharsets.UTF_8)
      .setType(Type.MAIN);
  }

  public TestInputFile(File baseDir, String relativePath) {
    this(baseDir.getAbsolutePath(), relativePath);
  }
}
