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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class DuplicateConditionIfElseAndCaseWhensCheckTest {
    @Test
    void caseTest() {
        EsqlCheckVerifier.issues(new DuplicateConditionIfElseAndCaseWhensCheck(), new File("src/test/resources/caseTest.esql"))
                .next().atLine(8).withMessage("This WHEN duplicates the one on line 6.")
                .next().atLine(20).withMessage("This WHEN duplicates the one on line 18.")
                .next().atLine(31).withMessage("This WHEN duplicates the one on line 29.")
                .noMore();
    }

    @Test
    void ifTest() {
        EsqlCheckVerifier.issues(new DuplicateConditionIfElseAndCaseWhensCheck(), new File("src/test/resources/ifTest.esql"))
                .next().atLine(16).withMessage("This branch duplicates the one on line 12.")
                .next().atLine(17).withMessage("This branch duplicates the one on line 8.")
                .noMore();
    }
}
