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
