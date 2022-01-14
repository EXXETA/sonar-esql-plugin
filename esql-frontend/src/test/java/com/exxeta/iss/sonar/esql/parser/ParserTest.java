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
package com.exxeta.iss.sonar.esql.parser;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void realLife() {
        assertThat(EsqlLegacyGrammar.PROGRAM).matches("");
    }

    @Test
    void bugfixes() throws IOException {
        File bugfixDir = new File("src/test/resources/bugfixes");
        for (File testFile : bugfixDir.listFiles()) {
            String content = FileUtils.readFileToString(testFile, Charset.defaultCharset());
            System.out.println("Parsing " + testFile.getAbsolutePath());
            assertThat(EsqlLegacyGrammar.PROGRAM).matches(content.trim());
        }

    }


}
