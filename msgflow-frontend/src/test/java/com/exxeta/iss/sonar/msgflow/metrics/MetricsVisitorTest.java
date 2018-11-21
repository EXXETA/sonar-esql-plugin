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
package com.exxeta.iss.sonar.msgflow.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowFileImpl;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitorContext;
import com.exxeta.iss.sonar.msgflow.tree.impl.MsgflowTree;

public class MetricsVisitorTest extends MsgflowModelTest {

  private static final File MODULE_BASE_DIR = new File("src/test/resources/metrics/");

  private static final DefaultInputFile INPUT_FILE = new TestInputFileBuilder("moduleKey", "nodes.msgflow")
    .setModuleBaseDir(MODULE_BASE_DIR.toPath())
    .setLanguage("msgflow")
    .setType(InputFile.Type.MAIN)
    .build();

  private static final String COMPONENT_KEY = "moduleKey:nodes.msgflow";
  private SensorContextTester context;
  private MsgflowVisitorContext treeVisitorContext;

  @Before
  public void setUp() throws Exception {
    context = SensorContextTester.create(MODULE_BASE_DIR);
    context.fileSystem().add(INPUT_FILE);
    treeVisitorContext = mock(MsgflowVisitorContext.class);
    when(treeVisitorContext.getMsgflowFile()).thenReturn(new MsgflowFileImpl(INPUT_FILE));
    MsgflowTree input_file = parse(INPUT_FILE.file());
	when(treeVisitorContext.getMsgflow()).thenReturn(input_file );
  }


@Test
  public void test() {
    MetricsVisitor metricsVisitor = createMetricsVisitor();
    metricsVisitor.scanTree(treeVisitorContext);
    //Change when connections are beeing parsed
    assertThat(context.measure(COMPONENT_KEY, MsgflowMetrics.CONNECTIONS).value()).isEqualTo(0);
    assertThat(context.measure(COMPONENT_KEY, MsgflowMetrics.NODES).value()).isEqualTo(1);

  }

  private MetricsVisitor createMetricsVisitor() {
    return new MetricsVisitor(context);
  }

}
