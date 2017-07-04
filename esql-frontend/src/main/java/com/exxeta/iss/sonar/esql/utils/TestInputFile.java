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
package com.exxeta.iss.sonar.esql.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;

import com.google.common.base.Throwables;

public class TestInputFile extends DefaultInputFile {
  public TestInputFile(File file, String contents, Charset encoding) {
    this("", file.getAbsolutePath());
    this.setCharset(encoding);
    try {
      Files.write(file.toPath(), contents.getBytes(encoding));
      this.initMetadata(new FileMetadata().readMetadata(file, encoding));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public TestInputFile(String relativePath) {
    this("", relativePath);
  }

  public TestInputFile(String baseDir, String relativePath) {
    super("module1", relativePath);
    this.setModuleBaseDir(Paths.get(baseDir))
      .setLanguage("esql")
      .setCharset(StandardCharsets.UTF_8)
      .setType(Type.MAIN);
  }

  public TestInputFile(File baseDir, String relativePath) {
    this(baseDir.getAbsolutePath(), relativePath);
  }
}
