/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.cpd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.cpd.internal.TokensLine;


import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.exxeta.iss.sonar.esql.utils.TestUtils;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.typed.ActionParser;

public class CpdVisitorTest {
	  private static final Charset CHARSET = Charsets.UTF_8;

	  private final ActionParser<Tree> p = EsqlParserBuilder.createParser();

	  private DefaultInputFile inputFile;
	  private SensorContextTester sensorContext;

	  @TempDir
	  public File tempFolder;

	  @Test
	  public void test() throws Exception {
	    scan("CREATE PROCEDURE A () BEGIN\nSET x = 'a' + 1 + 'line1';\nSET y = 2;\nEND;\n");
	    List<TokensLine> cpdTokenLines = sensorContext.cpdTokens("module1:" + inputFile.relativePath());
	    assertThat(cpdTokenLines).hasSize(4);
	    TokensLine firstTokensLine = cpdTokenLines.get(1);
	    assertThat(firstTokensLine.getValue()).isEqualTo("SETx=LITERAL+1+LITERAL;");
	    assertThat(firstTokensLine.getStartLine()).isEqualTo(2);
	    assertThat(firstTokensLine.getStartUnit()).isEqualTo(7);
	    assertThat(firstTokensLine.getEndLine()).isEqualTo(2);
	    assertThat(firstTokensLine.getEndUnit()).isEqualTo(15);

	    TokensLine secondTokensLine = cpdTokenLines.get(2);
	    assertThat(secondTokensLine.getValue()).isEqualTo("SETy=2;");
	    assertThat(secondTokensLine.getStartLine()).isEqualTo(3);
	    assertThat(secondTokensLine.getStartUnit()).isEqualTo(16);
	    assertThat(secondTokensLine.getEndLine()).isEqualTo(3);
	    assertThat(secondTokensLine.getEndUnit()).isEqualTo(20);
	  }

	  private void scan(String source) throws IOException {
	    inputFile = TestUtils.createTestInputFile(new File(tempFolder, "temp-"+ UUID.randomUUID()), source, CHARSET);

	    sensorContext = SensorContextTester.create(tempFolder.toPath());
	    CpdVisitor cpdVisitor = new CpdVisitor(sensorContext);
	    ProgramTree tree = (ProgramTree) p.parse(source);
	    TreeVisitorContext visitorContext = new EsqlVisitorContext(tree, inputFile, null);
	    cpdVisitor.scanTree(visitorContext);
	  }


}
