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

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;


/**
 * This java class is the test class.
 *
 * @author Sapna Singh
 */
class UnusedVariableCheckTest {
    @Test
    void test() {
        EsqlCheck check = new UnusedVariableCheck();
        EsqlCheckVerifier.issues(check, new File("src/test/resources/UnusedVariable.esql"))

                .next().atLine(11).withMessage("Remove the unused 'envRef' variable.")
                .next().atLine(33).withMessage("Remove the unused 'inReturn' variable.")
                .next().atLine(35).withMessage("Remove the unused 'qst' variable.")
                .noMore();

    }

}
