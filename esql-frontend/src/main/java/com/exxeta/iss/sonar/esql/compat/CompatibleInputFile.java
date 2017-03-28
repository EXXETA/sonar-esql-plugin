package com.exxeta.iss.sonar.esql.compat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;

import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;

/**
 * A compatibility wrapper for InputFile. See class hierarchy.
 *
 * All methods of this class simply delegate to the wrapped instance, except `wrapped`.
 */
public class CompatibleInputFile implements EsqlFile {
  private final InputFile wrapped;

  CompatibleInputFile(InputFile wrapped) {
    this.wrapped = wrapped;
  }

  /**
   * Get the original InputFile instance wrapped inside.
   *
   * @return original InputFile instance
   */
  public InputFile wrapped() {
    return wrapped;
  }

  public Path path() {
    return wrapped.path();
  }

  @Override
  public String fileName() {
    return path().getFileName().toString();
  }

  public String absolutePath() {
    return wrapped.absolutePath();
  }

  @Override
  public String relativePath() {
    return wrapped.relativePath();
  }

  @Override
  public String contents() {
    try {
      return wrapped.contents();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public File file() {
    return wrapped.file();
  }

  public TextPointer newPointer(int line, int lineOffset) {
    return wrapped.newPointer(line, lineOffset);
  }

  public TextRange newRange(int startLine, int startLineOffset, int endLine, int endLineOffset) {
    return wrapped.newRange(startLine, startLineOffset, endLine, endLineOffset);
  }

  public TextRange selectLine(int line) {
    return wrapped.selectLine(line);
  }

  public Charset charset() {
    return wrapped.charset();
  }

}
