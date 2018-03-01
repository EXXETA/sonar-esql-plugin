/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;

import com.exxeta.iss.sonar.esql.api.visitors.EsqlFileImpl;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class MetricsVisitorTest extends EsqlTreeModelTest {

  private static final File MODULE_BASE_DIR = new File("src/test/resources/metrics/");

  private static final DefaultInputFile INPUT_FILE = new TestInputFileBuilder("moduleKey", "lines.esql")
    .setModuleBaseDir(MODULE_BASE_DIR.toPath())
    .setLanguage("esql")
    .setType(InputFile.Type.MAIN)
    .build();

  private static final String COMPONENT_KEY = "moduleKey:lines.esql";
  private FileLinesContext linesContext;
  private SensorContextTester context;
  private TreeVisitorContext treeVisitorContext;

  @Before
  public void setUp() throws Exception {
    context = SensorContextTester.create(MODULE_BASE_DIR);
    context.fileSystem().add(INPUT_FILE);
    linesContext = mock(FileLinesContext.class);
    treeVisitorContext = mock(TreeVisitorContext.class);
    when(treeVisitorContext.getEsqlFile()).thenReturn(new EsqlFileImpl(INPUT_FILE));
    when(treeVisitorContext.getTopTree()).thenReturn(parse(INPUT_FILE.file()));
  }

  @Test
  public void test() {
    MetricsVisitor metricsVisitor = createMetricsVisitor();
    metricsVisitor.scanTree(treeVisitorContext);
    assertThat(context.measure(COMPONENT_KEY, CoreMetrics.FUNCTIONS).value()).isEqualTo(0);
    assertThat(context.measure(COMPONENT_KEY, CoreMetrics.STATEMENTS).value()).isEqualTo(1);
    assertThat(context.measure(COMPONENT_KEY, EsqlMetrics.MODULES).value()).isEqualTo(1);

    assertThat(metricsVisitor.executableLines().get(INPUT_FILE)).containsOnly(5);
  }

  @Test
  public void save_executable_lines() {
    final MetricsVisitor metricsVisitorWithSave = createMetricsVisitor();
    metricsVisitorWithSave.scanTree(treeVisitorContext);
    Mockito.verify(linesContext, atLeastOnce()).setIntValue(eq(CoreMetrics.EXECUTABLE_LINES_DATA_KEY), any(Integer.class), any(Integer.class));
  }

  private MetricsVisitor createMetricsVisitor() {
    FileLinesContextFactory linesContextFactory = mock(FileLinesContextFactory.class);
    when(linesContextFactory.createFor(INPUT_FILE)).thenReturn(linesContext);
    return new MetricsVisitor(context, false, linesContextFactory);
  }

}
