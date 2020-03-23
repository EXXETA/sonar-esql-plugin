/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckMessageTest {
    @Test
    public void testFormatDefaultMessage() {
        CheckMessage message = new CheckMessage(null, "Value is {0,number,integer}, expected value is {1,number,integer}.", 3, 7);
        assertThat(message.formatDefaultMessage()).isEqualTo("Value is 3, expected value is 7.");
    }
    @Test
    public void testNotFormatMessageWithoutParameters() {
        CheckMessage message = new CheckMessage(null, "public void main(){."); // This message can't be used as a pattern by the MessageFormat
        // class
        assertThat(message.formatDefaultMessage()).isEqualTo("public void main(){.");
    }
}
