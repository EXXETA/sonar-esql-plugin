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
package com.exxeta.iss.sonar.esql.highlighter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import com.google.common.io.Files;

public class HighlightSymbolTableBuilderTest extends EsqlTreeModelTest<Tree> {

  private SensorContextTester sensorContext;
  private DefaultInputFile inputFile;

  private NewSymbolTable newSymbolTable(String filename) throws FileNotFoundException  {
    File moduleBaseDir = new File("src/test/resources/highlighter/");
    sensorContext = SensorContextTester.create(moduleBaseDir);
    inputFile = new TestInputFileBuilder("moduleKey", filename)
      .setModuleBaseDir(moduleBaseDir.toPath())
      .setCharset(StandardCharsets.UTF_8)
      .build();
    inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(inputFile.file()), inputFile.charset(), inputFile.absolutePath()));

    return sensorContext.newSymbolTable().onFile(inputFile);
  }

  private static DefaultTextRange textRange(int line, int startColumn, int endColumn) {
    return new DefaultTextRange(new DefaultTextPointer(line, startColumn), new DefaultTextPointer(line, endColumn));
  }

  private Collection<TextRange> references(String key, int line, int column) {
    return sensorContext.referencesForSymbolAt(key, line, column);
  }

  @Test
  public void sonar_symbol_table() throws Exception {
    String filename = "symbolHighlighting.esql";
    String key = "moduleKey:" + filename;
    HighlightSymbolTableBuilder.build(newSymbolTable(filename), context(inputFile));

    // variable
    assertThat(references(key, 2, 11)).containsOnly(textRange(3, 7, 8), textRange(4, 7, 8));

    // function declaration
    assertThat(references(key, 1, 16)).containsOnly(textRange(3, 11, 12));

    // function parameter
    assertThat(references(key, 14, 26)).containsOnly(textRange(15, 19, 20));

  }

  @Test
  public void byte_order_mark_should_not_increment_offset() throws Exception {
    String filename = "symbolHighlightingBom.esql";

    HighlightSymbolTableBuilder.build(newSymbolTable(filename), context(inputFile));
    assertThat(Files.toString(inputFile.file(), inputFile.charset()).startsWith("\uFEFF")).isTrue();
    assertThat(references("moduleKey:" + filename, 1, 16)).containsOnly(textRange(2, 11, 12));
  }

 
}
