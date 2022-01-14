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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.check.TooManyLinesInFileCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class TooManyLinesInFileCheckTest {

    private TooManyLinesInFileCheck check = new TooManyLinesInFileCheck();

    @Test
    void test_negative() {
        check.maximum = 3;
        EsqlCheckVerifier.issues(check, new File("src/test/resources/test.esql"))
                .noMore();
    }

    @Test
    void test_positive() {
        check.maximum = 2;
        EsqlCheckVerifier.issues(check, new File("src/test/resources/test.esql"))
                .next()
                .withMessage(
                        "File \"test.esql\" has 3 lines, which is greater than 2 authorized. Split it into smaller files.")
                .noMore();
    }

}
