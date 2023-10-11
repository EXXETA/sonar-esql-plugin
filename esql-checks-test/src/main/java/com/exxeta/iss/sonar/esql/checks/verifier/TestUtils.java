/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.checks.verifier;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.config.internal.MapSettings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class TestUtils {
    protected static final ActionParser<Tree> p = newParser();

    private TestUtils() {
    }

    private static ActionParser<Tree> newParser() {
        return EsqlParserBuilder.createParser();
    }

    public static EsqlVisitorContext createContext(InputFile file) {
        return createContext(file, p);
    }

    public static EsqlVisitorContext createParallelContext(InputFile file) {
        return createContext(file, newParser());
    }

    private static EsqlVisitorContext createContext(InputFile file, ActionParser<Tree> parser) {
        try {
            ProgramTree programTree = (ProgramTree) parser.parse(file.contents());
            return new EsqlVisitorContext(programTree, file, new MapSettings().asConfig());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DefaultInputFile createTestInputFile(String baseDir, String relativePath) {
        return new TestInputFileBuilder("module1", relativePath).setModuleBaseDir(Paths.get(baseDir))
                .setLanguage("esql").setCharset(StandardCharsets.UTF_8).setType(InputFile.Type.MAIN)
                .build();
    }

    public static DefaultInputFile createTestInputFile(File baseDir, String relativePath) {
        return createTestInputFile(baseDir.getAbsolutePath(), relativePath);
    }

    public static DefaultInputFile createTestInputFile(String relativePath) {
        return createTestInputFile("", relativePath);
    }

    public static DefaultInputFile createTestInputFile(File baseDir, String relativePath, Charset charset) {
        return new TestInputFileBuilder(baseDir.getAbsolutePath(), relativePath)
                .setModuleBaseDir(Paths.get(baseDir.getAbsolutePath())).setLanguage("esql").setCharset(charset)
                .build();
    }

}
