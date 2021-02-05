/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.config.internal.MapSettings;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Throwables;

public class TestUtils {

  public static EsqlVisitorContext createContext(InputFile file) {
    try {
      EsqlTree programTree = (EsqlTree) EsqlParserBuilder.createParser().parse(file.contents());
      return new EsqlVisitorContext((ProgramTree)programTree, file, new MapSettings().asConfig());
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
  public static DefaultInputFile createTestInputFile(File file, String contents, Charset encoding) {
	    final DefaultInputFile inputFile = new TestInputFileBuilder("module1", file.getName()).setCharset(encoding).build();
	    try {
	      Files.write(file.toPath(), contents.getBytes(encoding));
	      inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(file), encoding, file.getAbsolutePath()));
	    } catch (IOException e) {
	      throw Throwables.propagate(e);
	    }
	    return inputFile;
	  }
  
  public static DefaultInputFile createTestInputFile(String baseDir, String relativePath) {
	    final DefaultInputFile inputFile = new TestInputFileBuilder("module1", relativePath)
	      .setModuleBaseDir(Paths.get(baseDir))
	      .setLanguage("esql")
	      .setCharset(StandardCharsets.UTF_8)
	      .setType(InputFile.Type.MAIN)
	      .build();
	    return inputFile;
	  }

	  public static DefaultInputFile createTestInputFile(String relativePath) {
	    return createTestInputFile("", relativePath);
	  }
}
