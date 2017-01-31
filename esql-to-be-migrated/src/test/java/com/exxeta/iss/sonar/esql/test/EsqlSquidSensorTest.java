/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.checks.NoSonarFilter;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.source.Highlightable;
import org.sonar.test.TestUtils;

import com.exxeta.iss.sonar.esql.EsqlSquidSensor;
import com.exxeta.iss.sonar.esql.core.Esql;

public class EsqlSquidSensorTest {

  private DefaultFileSystem fs = new DefaultFileSystem();
  private FileLinesContextFactory fileLinesContextFactory;
  private final Project project = new Project("project");
  private CheckFactory checkFactory = new CheckFactory(mock(ActiveRules.class));
  
  @Before
  public void setUp() {
	  fileLinesContextFactory = mock(FileLinesContextFactory.class);
	  FileLinesContext fileLinesContext = mock(FileLinesContext.class);
	  when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
	 

  }

@Test
  public void should_execute_on_esql_project() {
	
	DefaultFileSystem localFS = new DefaultFileSystem();
	EsqlSquidSensor sensor = new EsqlSquidSensor(checkFactory, fileLinesContextFactory, mock(ResourcePerspectives.class), localFS, new NoSonarFilter(
	mock(SensorContext.class)), new PathResolver(), new Settings());
	// no JS files -> do not execute
	assertThat(sensor.shouldExecuteOnProject(project)).isFalse();
	// at least one JS file -> do execute
	localFS.add(new DefaultInputFile("test.esql").setType(InputFile.Type.MAIN).setLanguage(Esql.KEY));
	assertThat(sensor.shouldExecuteOnProject(project)).isTrue();
	
  }

  @Test
  public void should_analyse() {
	  
	  fs.setBaseDir(TestUtils.getResource("/squid/"));
	  DefaultInputFile inputFile = new DefaultInputFile("ifTest.esql").
			  setAbsolutePath(TestUtils.getResource("/squid/ifTest.esql").getAbsolutePath())
			  .setType(InputFile.Type.MAIN)
			  .setLanguage(Esql.KEY);
	  fs.add(inputFile);
	  
	  SensorContext context = mock(SensorContext.class);
	  ResourcePerspectives perspectives = mock(ResourcePerspectives.class);
	  Highlightable highlightable = mock(Highlightable.class);
	  Highlightable.HighlightingBuilder builder = mock(Highlightable.HighlightingBuilder.class);
	  when(perspectives.as(Highlightable.class, inputFile)).thenReturn(highlightable);
	  when(highlightable.newHighlighting()).thenReturn(builder);
	  when(context.getResource(any(Resource.class))).thenReturn(File.create((new PathResolver()).relativePath(fs.baseDir(), TestUtils.getResource("/squid/ifTest.esql"))));
	  
/*	  Issuable issuable = mock(Issuable.class);
	  Issuable.IssueBuilder issueBuilder = mock(Issuable.IssueBuilder.class);
	  when(perspectives.as(Mockito.eq(Issuable.class), Mockito.any(InputFile.class))).thenReturn(issuable);
	  when(issuable.newIssueBuilder()).thenReturn(issueBuilder);
	  when(issueBuilder.ruleKey(Mockito.any(RuleKey.class))).thenReturn(issueBuilder);
	  when(issueBuilder.line(Mockito.any(Integer.class))).thenReturn(issueBuilder);
	  when(issueBuilder.message(Mockito.any(String.class))).thenReturn(issueBuilder);*/
	  EsqlSquidSensor sensor = new EsqlSquidSensor(checkFactory, fileLinesContextFactory, perspectives, fs, new NoSonarFilter(
			  mock(SensorContext.class)), new PathResolver(), new Settings());
	  Project project = new Project("key");
	  sensor.analyse(project, context);
	  
	  
    verify(context).saveMeasure(any(Resource.class),eq(CoreMetrics.FILES), Mockito.eq(1.0));
    verify(context).saveMeasure(any(Resource.class), eq(CoreMetrics.LINES), Mockito.eq(20.0));
    verify(context).saveMeasure(any(Resource.class), Mockito.eq(CoreMetrics.NCLOC), Mockito.eq(18.0));
    verify(context).saveMeasure(any(Resource.class), Mockito.eq(CoreMetrics.FUNCTIONS), Mockito.eq(1.0));
    verify(context).saveMeasure(any(Resource.class), Mockito.eq(CoreMetrics.STATEMENTS), Mockito.eq(5.0));
    verify(context).saveMeasure(any(Resource.class), Mockito.eq(CoreMetrics.COMPLEXITY), Mockito.eq(6.0));
    verify(context).saveMeasure(any(Resource.class), Mockito.eq(CoreMetrics.COMMENT_LINES), Mockito.eq(3.0));
    

  }

}
