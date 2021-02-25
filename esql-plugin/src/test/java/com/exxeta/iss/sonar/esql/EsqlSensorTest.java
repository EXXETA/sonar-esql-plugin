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
package com.exxeta.iss.sonar.esql;

import com.exxeta.iss.sonar.esql.api.CustomEsqlRulesDefinition;
import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.check.CheckList;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.RecognitionException;
import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.LogTester;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.ProgressReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class
EsqlSensorTest {

    @org.junit.Rule
    public final ExpectedException thrown = ExpectedException.none();

    @org.junit.Rule
    public LogTester logTester = new LogTester();

    private FileLinesContextFactory fileLinesContextFactory;

    private CheckFactory checkFactory = new CheckFactory(mock(ActiveRules.class));


    private final CustomEsqlRulesDefinition[] CUSTOM_RULES = {new CustomEsqlRulesDefinition() {
        @Override
        public String repositoryName() {
            return "custom name";
        }

        @Override
        public String repositoryKey() {
            return "customKey";
        }

        @Override
        public Class[] checkClasses() {
            return new Class[]{MyCustomRule.class};
        }
    }};

    private File baseDir = new File("src/test/resources");
    private final ProgressReport progressReport = mock(ProgressReport.class);
    private SensorContextTester context = SensorContextTester.create(baseDir);

    private EsqlSensor createSensor() {
        return new EsqlSensor(checkFactory, fileLinesContextFactory, context.fileSystem(), new NoSonarFilter());
    }

    private EsqlSensor createSensorWithCustomRules() {
        return new EsqlSensor(checkFactory, fileLinesContextFactory, context.fileSystem(), new NoSonarFilter(), CUSTOM_RULES);
    }

    @Before
    public void setUp() {
        fileLinesContextFactory = mock(FileLinesContextFactory.class);
        FileLinesContext fileLinesContext = mock(FileLinesContext.class);
        when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
    }

    @Test
    public void should_analyse() {
        String relativePath = "cpd/Person.esql";
        inputFile(relativePath);

        createSensor().execute(context);

        String key = "moduleKey:" + relativePath;


        //assertThat(context.measure(key, CoreMetrics.FILES).value()).isEqualTo(1);
        //assertThat(context.measure(key, CoreMetrics.LINES).value()).isEqualTo(20);
        assertThat(context.measure(key, CoreMetrics.NCLOC).value()).isEqualTo(18);
        assertThat(context.measure(key, CoreMetrics.FUNCTIONS).value()).isEqualTo(1);
        assertThat(context.measure(key, CoreMetrics.STATEMENTS).value()).isEqualTo(8);
        assertThat(context.measure(key, CoreMetrics.COMPLEXITY).value()).isEqualTo(7);
        assertThat(context.measure(key, CoreMetrics.COMMENT_LINES).value()).isEqualTo(3);
    }

    @Test
    public void parsing_error() {
        String relativePath = "cpd/parsingError.esql";
        inputFile(relativePath);

        String parsingErrorCheckKey = "ParsingError";

        ActiveRules activeRules = (new ActiveRulesBuilder())
                .addRule(new NewActiveRule.Builder().setName("ParsingError").setRuleKey(RuleKey.of(CheckList.REPOSITORY_KEY, parsingErrorCheckKey)).build())
                .build();

        checkFactory = new CheckFactory(activeRules);

        context.setActiveRules(activeRules);
        createSensor().execute(context);
        Collection<Issue> issues = context.allIssues();
        assertThat(issues).hasSize(1);
        Issue issue = issues.iterator().next();
        assertThat(issue.primaryLocation().textRange().start().line()).isEqualTo(3);
        assertThat(issue.primaryLocation().message()).isEqualTo("Parse error");

        assertThat(context.allAnalysisErrors()).hasSize(1);
    }

    @Test
    public void should_add_error_to_context_but_not_fail_analysis_with_technical_error() {
        EsqlCheck check = new ExceptionRaisingCheck(new NullPointerException("NPE forcibly raised by check class"));

        InputFile file = inputFile("file.esql");
        createSensor().analyseFiles(context, ImmutableList.of((TreeVisitor) check),
                ImmutableList.of(file), progressReport);
        assertThat(context.allAnalysisErrors()).hasSize(1);

        assertThat(logTester.logs()).contains("Unable to analyse file: " + file.uri());
    }

    @Test
    public void analysis_with_no_issue_should_not_add_error_to_context() {
        inputFile("file.esql");

        createSensor().execute(context);

        Collection<Issue> issues = context.allIssues();
        assertThat(issues).hasSize(0);

        assertThat(context.allAnalysisErrors()).isEmpty();
    }

    @Test
    public void analysis_with_issues_should_not_add_error_to_context() {
        inputFile("file.esql");

        ActiveRules activeRules = (new ActiveRulesBuilder())
                .addRule(new NewActiveRule.Builder().setRuleKey(RuleKey.of(CheckList.REPOSITORY_KEY, "MissingNewlineAtEndOfFile")).build())
                .build();
        checkFactory = new CheckFactory(activeRules);

        createSensor().execute(context);

        Collection<Issue> issues = context.allIssues();
        assertThat(issues).hasSize(1);

        assertThat(context.allAnalysisErrors()).isEmpty();
    }

    @Test
    public void save_issue() throws Exception {
        inputFile("file.esql");

        ActiveRules activeRules = (new ActiveRulesBuilder())
                .addRule(new NewActiveRule.Builder().setRuleKey(RuleKey.of(CheckList.REPOSITORY_KEY, "MissingNewlineAtEndOfFile")).build())
                .addRule(new NewActiveRule.Builder().setRuleKey(RuleKey.of(CheckList.REPOSITORY_KEY, "InitializeVariables")).build())
                .build();

        checkFactory = new CheckFactory(activeRules);
        createSensor().execute(context);
        Collection<Issue> issues = context.allIssues();
        assertThat(issues).hasSize(2);
    }

    @Test
    public void custom_rule() throws Exception {
        inputFile("file.esql");
        ActiveRules activeRules = (new ActiveRulesBuilder())
                .addRule(new NewActiveRule.Builder().setRuleKey(RuleKey.of("customKey", "key")).build()).build();
        checkFactory = new CheckFactory(activeRules);
        createSensorWithCustomRules().execute(context);

        Collection<Issue> issues = context.allIssues();
        assertThat(issues).hasSize(1);
        Issue issue = issues.iterator().next();
        assertThat(issue.gap()).isEqualTo(42);
        assertThat(issue.primaryLocation().message()).isEqualTo("Message of custom rule");
        assertThat(issue.primaryLocation().textRange())
                .isEqualTo(new DefaultTextRange(new DefaultTextPointer(1, 0), new DefaultTextPointer(1, 24)));
    }

    @Test
    public void progress_report_should_be_stopped() throws Exception {
        InputFile inputFile = inputFile("cpd/Person.esql");
        createSensor().analyseFiles(context, ImmutableList.of(), ImmutableList.of(inputFile), progressReport);
        verify(progressReport).stop();
    }

    @Test
    public void progress_report_should_be_stopped_without_files() throws Exception {
        createSensor().analyseFiles(context, ImmutableList.of(), ImmutableList.of(), progressReport);
        verify(progressReport).stop();
    }

    @Test
    public void cancelled_analysis() throws Exception {
        EsqlCheck check = new ExceptionRaisingCheck(new IllegalStateException(new InterruptedException()));
        analyseFileWithException(check, inputFile("cpd/Person.esql"), "Analysis cancelled");
        assertThat(context.allAnalysisErrors()).hasSize(1);
    }

    @Test
    public void cancelled_analysis_causing_recognition_exception() throws Exception {
        EsqlCheck check = new ExceptionRaisingCheck(
                new RecognitionException(42, "message", new InterruptedIOException()));
        analyseFileWithException(check, inputFile("cpd/Person.esql"), "Analysis cancelled");
        assertThat(context.allAnalysisErrors()).hasSize(1);
    }

    @Test
    public void exception_should_report_file_name() throws Exception {
        EsqlCheck check = new ExceptionRaisingCheck(new IllegalStateException(new InterruptedException()));
        analyseFileWithException(check, inputFile("cpd/Person.esql"), "Analysis cancelled");
        assertThat(context.allAnalysisErrors()).hasSize(1);
    }

    @Test
    public void cancelled_context_should_cancel_progress_report_and_return_with_no_exception() {
        EsqlCheck check = new DoubleDispatchVisitorCheck() {
        };
        EsqlSensor sensor = createSensor();
        SensorContextTester cancelledContext = SensorContextTester.create(baseDir);
        cancelledContext.setCancelled(true);
        sensor.analyseFiles(cancelledContext, ImmutableList.of((TreeVisitor) check),
                ImmutableList.of(inputFile("cpd/Person.esql")), progressReport);
        verify(progressReport).cancel();
    }

    private void analyseFileWithException(EsqlCheck check, InputFile inputFile,
                                          String expectedMessageSubstring) {
        EsqlSensor sensor = createSensor();
        thrown.expectMessage(expectedMessageSubstring);
        try {
            sensor.analyseFiles(context, ImmutableList.of((TreeVisitor) check), ImmutableList.of(inputFile),
                    progressReport);
        } finally {
            verify(progressReport).cancel();
        }
    }


    private InputFile inputFile(String relativePath) {
        DefaultInputFile inputFile = new TestInputFileBuilder("moduleKey", relativePath)
                .setModuleBaseDir(baseDir.toPath())
                .setType(Type.MAIN)
                .setLanguage(EsqlLanguage.KEY)
                .setCharset(StandardCharsets.UTF_8)
                .build();

        context.fileSystem().add(inputFile);

        try {
            inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(inputFile.file()), Charsets.UTF_8, inputFile.absolutePath()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputFile;
    }

    private final class ExceptionRaisingCheck extends DoubleDispatchVisitorCheck {

        private final RuntimeException exception;

        ExceptionRaisingCheck(RuntimeException exception) {
            this.exception = exception;
        }

        @Override
        public TreeVisitorContext getContext() {
            return null;
        }

        @Override
        public void visitProgram(ProgramTree tree) {
            throw exception;
        }
    }

    @Rule(key = "key", name = "name", description = "desc", tags = {"bug"})
    public static class MyCustomRule extends DoubleDispatchVisitorCheck {
        @RuleProperty(key = "customParam", description = "Custome parameter", defaultValue = "value")
        public String customParam = "value";

        @Override
        public void visitProgram(ProgramTree tree) {
            addIssue(new LineIssue(this, 1, "Message of custom rule")).cost(42);
        }
    }

}
