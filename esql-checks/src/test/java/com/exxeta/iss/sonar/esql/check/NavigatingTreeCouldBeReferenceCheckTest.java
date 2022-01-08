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

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * This java  class is  the test class for NavigatingTreeCouldBeReferenceCheck.java.
 *
 * @author Sapna. singh
 */
class NavigatingTreeCouldBeReferenceCheckTest {


    @Test
    void test() {
        NavigatingTreeCouldBeReferenceCheck check = new NavigatingTreeCouldBeReferenceCheck();
        EsqlCheckVerifier.issues(check, new File("src/test/resources/NavigatingTreeCouldBeReference.esql"))
                .next().atLine(4).withMessage("Navigating message tree could be replaced by a reference.")
                .next().atLine(16).withMessage("Navigating message tree could be replaced by a reference.")
                .next().atLine(16).withMessage("Navigating message tree could be replaced by a reference.")
                .noMore();
        check.maximumAllowedPathElements = 3;
        EsqlCheckVerifier.issues(check, new File("src/test/resources/NavigatingTreeCouldBeReference.esql"))
                .next().atLine(4).withMessage("Navigating message tree could be replaced by a reference.")
                .noMore();
        check.maximumAllowedPathElements = 2;
        check.allowedNumberOfFieldReferenceUses = 4;
        EsqlCheckVerifier.issues(check, new File("src/test/resources/NavigatingTreeCouldBeReference.esql"))
                .next().atLine(16).withMessage("Navigating message tree could be replaced by a reference.")
                .noMore();
    }
}

