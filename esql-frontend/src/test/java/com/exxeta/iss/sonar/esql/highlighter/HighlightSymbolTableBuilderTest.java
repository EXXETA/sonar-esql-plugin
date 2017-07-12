/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.exxeta.iss.sonar.esql.highlighter;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.compat.CompatibleInputFile;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import org.junit.Test;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;

import static org.assertj.core.api.Assertions.assertThat;
import static com.exxeta.iss.sonar.esql.compat.CompatibilityHelper.wrap;

public class HighlightSymbolTableBuilderTest extends EsqlTreeModelTest<Tree> {

  private SensorContextTester sensorContext;
  private CompatibleInputFile inputFile;

  private NewSymbolTable newSymbolTable(String filename) {
    File moduleBaseDir = new File("src/test/resources/highlighter/");
    sensorContext = SensorContextTester.create(moduleBaseDir);
    DefaultInputFile defaultInputFile = new DefaultInputFile("moduleKey", filename)
      .setModuleBaseDir(moduleBaseDir.toPath())
      .setCharset(StandardCharsets.UTF_8);
    inputFile = wrap(defaultInputFile);
    defaultInputFile.initMetadata(new FileMetadata().readMetadata(inputFile.file(), defaultInputFile.charset()));

    return sensorContext.newSymbolTable().onFile(defaultInputFile);
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
