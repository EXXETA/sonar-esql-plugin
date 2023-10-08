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

class NestedIfDepthCheckTest {
    @Test
    void test() {
        NestedIfDepthCheck check = new NestedIfDepthCheck();
        check.maximumNestingLevel = 3;

        EsqlCheckVerifier.issues(check, new File("src/test/resources/ifTest.esql"))
                .next().atLine(9).withMessage("This if has a nesting level of 4, which is higher than the maximum allowed 3.")
                .next().atLine(13).withMessage("This if has a nesting level of 4, which is higher than the maximum allowed 3.")
                .noMore();
    }
}
