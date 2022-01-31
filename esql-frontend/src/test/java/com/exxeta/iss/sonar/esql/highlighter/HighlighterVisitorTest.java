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
package com.exxeta.iss.sonar.esql.highlighter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.sonar.api.batch.sensor.highlighting.TypeOfText.COMMENT;
import static org.sonar.api.batch.sensor.highlighting.TypeOfText.KEYWORD;
import static org.sonar.api.batch.sensor.highlighting.TypeOfText.STRING;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFileImpl;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import com.google.common.base.Charsets;

class HighlighterVisitorTest extends EsqlTreeModelTest<ProgramTree> {

    private static final Charset CHARSET = Charsets.UTF_8;

    private HighlighterVisitor highlighterVisitor;

    private TreeVisitorContext visitorContext;
    private SensorContextTester sensorContext;
    private DefaultInputFile inputFile;

    @TempDir
    File tempFolder;

    @BeforeEach
    void setUp() throws IOException {
        sensorContext = SensorContextTester.create(tempFolder);
        visitorContext = mock(TreeVisitorContext.class);
        highlighterVisitor = new HighlighterVisitor(sensorContext);
    }

    private void initFile(String text) throws IOException {
        File file = new File(tempFolder, "temp-" + UUID.randomUUID());
        inputFile = new TestInputFileBuilder("moduleKey", file.getName())
                .setLanguage("esql")
                .setType(Type.MAIN)
                .setCharset(CHARSET)
                .initMetadata(text)
                .build();

        when(visitorContext.getEsqlFile()).thenReturn(new EsqlFileImpl(inputFile));
    }

    private void highlight(String string) throws Exception {
        initFile(string);
        Tree tree = parse(string);
        when(visitorContext.getTopTree()).thenReturn((ProgramTree) tree);
        highlighterVisitor.scanTree(visitorContext);
    }

    private void assertHighlighting(int column, int length, TypeOfText type) {
        assertHighlighting(1, column, length, type);
    }

    private void assertHighlighting(int line, int column, int length, TypeOfText type) {
        for (int i = column; i < column + length; i++) {
            List<TypeOfText> typeOfTexts = sensorContext.highlightingTypeAt("moduleKey:" + inputFile.relativePath(),
                    line, i);
            assertThat(typeOfTexts).hasSize(1);
            assertThat(typeOfTexts.get(0)).isEqualTo(type);
        }
    }

    @Test
    void empty_input() throws Exception {
        highlight("");
        assertThat(sensorContext.highlightingTypeAt("moduleKey:" + inputFile.relativePath(), 1, 0)).isEmpty();
    }

    @Test
    void multiline_comment() throws Exception {
        highlight("/*\nComment\n*/ ");
        assertHighlighting(1, 0, 2, COMMENT);
        assertHighlighting(2, 0, 7, COMMENT);
        assertHighlighting(3, 0, 2, COMMENT);
    }

    @Test
    void single_line_comment() throws Exception {
        highlight("  --Comment ");
        assertHighlighting(2, 10, COMMENT);
    }

    @Test
    void javadoc_comment() throws Exception {
        highlight("  /**Comment*/ ");
        assertHighlighting(2, 12, TypeOfText.STRUCTURED_COMMENT);
    }

    @Test
    void numbers() throws Exception {
        highlight("CREATE FUNCTION a() SET x = 1;");
        assertHighlighting(28, 1, TypeOfText.CONSTANT);
    }

    @Test
    void string() throws Exception {
        highlight("CREATE FUNCTION a() SET x = 'a';");
        assertHighlighting(28, 3, STRING);
    }

    @Test
    void keyword() throws Exception {
        highlight("CREATE FUNCTION a() SET x = 0;");
        assertHighlighting(20, 3, KEYWORD);
    }

    @Test
    void byte_order_mark() throws Exception {
        highlight("\uFEFFDECLARE a CHAR;  --");
        assertHighlighting(0, 7, KEYWORD);
    }

}
