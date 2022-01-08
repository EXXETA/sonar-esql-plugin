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

package com.exxeta.iss.sonar.esql.checks.verifier;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CheckMessagesVerifierTest {

    @Test
    void next() {
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            CheckMessagesVerifier.verify(Collections.EMPTY_LIST)
                    .next();
        });
        assertThat(thrown.getMessage(), is("\nExpected violation"));


    }

    @Test
    void noMore() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .noMore();
        });
        assertThat(thrown.getMessage(), is("\nNo more violations expected\ngot: at line 1"));
    }

    @Test
    void line() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .next().atLine(2);
        });
        assertThat(thrown.getMessage(), is("\nExpected: 2\ngot: 1"));
    }

    @Test
    void line_withoutHasNext() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .atLine(2);
        });
        assertThat(thrown.getMessage(), is("Prior to this method you should call next()"));
    }

    @Test
    void withMessage() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .next().atLine(1).withMessage("bar");
        });
        assertThat(thrown.getMessage(), containsString("Expected: \"bar\""));
        assertThat(thrown.getMessage(), containsString("got: \"foo\""));
    }

    @Test
    void withMessage_withoutHasNext() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .withMessage("foo");
        });
        assertThat(thrown.getMessage(), is("Prior to this method you should call next()"));
    }

    @Test
    void withMessageThat() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .next().atLine(1).withMessageThat(containsString("bar"));
        });
        assertThat(thrown.getMessage(), is("\nExpected: a string containing \"bar\"\n     but: was \"foo\""));
    }

    @Test
    void withCost() {
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo", 0d));
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            CheckMessagesVerifier.verify(messages)
                    .next().withCost(1d);
        });
        assertThat(thrown.getMessage(), containsString("Expected: 1.0"));
        assertThat(thrown.getMessage(), containsString("got: 0.0"));
    }

    @Test
    void ok() {
        Collection<CheckMessage> messages = Arrays.asList(
                mockCheckMessage(null, "b"),
                mockCheckMessage(2, "a", 1d),
                mockCheckMessage(null, "a"),
                mockCheckMessage(1, "b"),
                mockCheckMessage(1, "a"));
        CheckMessagesVerifier.verify(messages)
                .next().atLine(null).withMessage("a")
                .next().atLine(null).withMessageThat(containsString("b"))
                .next().atLine(1).withMessage("a")
                .next().atLine(1).withMessage("b")
                .next().atLine(2).withMessage("a").withCost(1d)
                .noMore();
    }

    private static final CheckMessage mockCheckMessage(Integer line, String message, Double cost) {
        CheckMessage checkMessage = mock(CheckMessage.class);
        when(checkMessage.getLine()).thenReturn(line);
        when(checkMessage.getDefaultMessage()).thenReturn(message);
        when(checkMessage.getText()).thenReturn(message);
        when(checkMessage.getCost()).thenReturn(cost);
        return checkMessage;
    }

    private static final CheckMessage mockCheckMessage(Integer line, String message) {
        return mockCheckMessage(line, message, null);
    }
}
