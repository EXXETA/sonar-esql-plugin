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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Test class is created to check the cyclomatic complexity of the code.
 *
 * @author sapna singh
 */
class CyclomaticComplexityCheckTest {


    @Test
    void test() {
        EsqlCheck check = new CyclomaticComplexityCheck();
        EsqlCheckVerifier.issues(check, new File("src/test/resources/CyclomaticComplexity.esql"))
                .next().atLine(2).withMessage("Refactor this function to reduce its Cognitive Complexity from 12 to the 10 allowed.")
                .next().atLine(33).withMessage("Refactor this procedure to reduce its Cognitive Complexity from 33 to the 10 allowed.")
                .noMore();
    }

}


