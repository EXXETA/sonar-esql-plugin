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
package com.exxeta.iss.sonar.esql.check.naming;

import java.io.File;

import com.exxeta.iss.sonar.esql.check.naming.FileNameCheck;
import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class FileNameCheckTest {
    @Test
    void test_negative() {
        FileNameCheck check = new FileNameCheck();
        check.format = "^[a-z][a-zA-Z]{1,30}\\.esql$";
        EsqlCheckVerifier.issues(check, new File("src/test/resources/test.esql"))
                .noMore();
    }

    @Test
    void test_positive() {
        FileNameCheck check = new FileNameCheck();
        check.format = "^[A-Z][a-zA-Z]{1,30}\\.esql$";
        EsqlCheckVerifier.issues(check, new File("src/test/resources/test.esql"))
                .next()
                .withMessage(
                        "Rename file \"test.esql\"  to match the regular expression ^[A-Z][a-zA-Z]{1,30}\\.esql$.")
                .noMore();
    }

}
