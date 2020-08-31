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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckMessagesVerifierTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void next() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("\nExpected violation");
        CheckMessagesVerifier.verify(Collections.EMPTY_LIST)
                .next();
    }

    @Test
    public void noMore() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("\nNo more violations expected\ngot: at line 1");
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        CheckMessagesVerifier.verify(messages)
                .noMore();
    }

    @Test
    public void line() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("\nExpected: 2\ngot: 1");
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        CheckMessagesVerifier.verify(messages)
                .next().atLine(2);
    }

    @Test
    public void line_withoutHasNext() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Prior to this method you should call next()");
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        CheckMessagesVerifier.verify(messages)
                .atLine(2);
    }

    @Test
    public void withMessage() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(containsString("Expected: \"bar\""));
        thrown.expectMessage(containsString("got: \"foo\""));
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        CheckMessagesVerifier.verify(messages)
                .next().atLine(1).withMessage("bar");
    }

    @Test
    public void withMessage_withoutHasNext() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Prior to this method you should call next()");
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        CheckMessagesVerifier.verify(messages)
                .withMessage("foo");
    }

    @Test
    public void withMessageThat() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("\nExpected: a string containing \"bar\"\n     but: was \"foo\"");
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo"));
        CheckMessagesVerifier.verify(messages)
                .next().atLine(1).withMessageThat(containsString("bar"));
    }

    @Test
    public void withCost() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(containsString("Expected: 1.0"));
        thrown.expectMessage(containsString("got: 0.0"));
        Collection<CheckMessage> messages = Arrays.asList(mockCheckMessage(1, "foo", 0d));
        CheckMessagesVerifier.verify(messages)
                .next().withCost(1d);
    }

    @Test
    public void ok() {
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
