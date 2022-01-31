package com.exxeta.iss.sonar.esql.checks.verifier;

import com.google.common.collect.Ordering;
import org.hamcrest.Matcher;
import com.google.common.base.Objects;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This class was copy&pasted from sslr-squid-bridge to avoid dependency on it
 * <p>
 * Helper class for testing checks without having to deploy them on a Sonar instance.
 * It can be used as following:
 * <pre>{@code
 * CheckMessagesVerifier.verify(messages)
 *   .next().atLine(1).withMessage("foo")
 *   .next().atLine(2).withMessage("bar")
 *   .noMore();
 * }</pre>
 * Strictly speaking this is just a wrapper over collection of {@link CheckMessage},
 * which guarantees order of traversal.
 *
 * @since sslr-squid-bridge 2.1
 */
public final class CheckMessagesVerifier {

    private final Iterator<CheckMessage> iterator;
    private CheckMessage current;

    private static final Comparator<CheckMessage> ORDERING = (left, right) -> {
        if (Objects.equal(left.getLine(), right.getLine())) {
            return left.getDefaultMessage().compareTo(right.getDefaultMessage());
        } else if (left.getLine() == null) {
            return -1;
        } else if (right.getLine() == null) {
            return 1;
        } else {
            return left.getLine().compareTo(right.getLine());
        }
    };

    private CheckMessagesVerifier(Collection<CheckMessage> messages) {
        iterator = Ordering.from(ORDERING).sortedCopy(messages).iterator();
    }

    public static CheckMessagesVerifier verify(Collection<CheckMessage> messages) {
        return new CheckMessagesVerifier(messages);
    }

    public CheckMessagesVerifier next() {
        if (!iterator.hasNext()) {
            throw new AssertionError("\nExpected violation");
        }
        current = iterator.next();
        return this;
    }

    public void noMore() {
        if (iterator.hasNext()) {
            CheckMessage next = iterator.next();
            throw new AssertionError("\nNo more violations expected\ngot: at line " + next.getLine());
        }
    }

    private void checkStateOfCurrent() {
        if (current == null) {
            throw new IllegalStateException("Prior to this method you should call next()");
        }
    }

    public CheckMessagesVerifier atLine(@Nullable Integer expectedLine) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedLine, current.getLine())) {
            throw assertionError(expectedLine, current.getLine());
        }
        return this;
    }

    public CheckMessagesVerifier withMessage(String expectedMessage) {
        checkStateOfCurrent();
        String actual = current.getText();
        if (!actual.equals(expectedMessage)) {
            throw assertionError("\"" + expectedMessage + "\"", "\"" + actual + "\"");
        }
        return this;
    }

    /**
     * Note that this method requires JUnit and Hamcrest.
     */
    CheckMessagesVerifier withMessageThat(Matcher<String> matcher) {
        checkStateOfCurrent();
        String actual = current.getText();
        assertThat(actual, matcher);
        return this;
    }

    /**
     * @since sslr-squid-bridge 2.3
     */
    CheckMessagesVerifier withCost(Double expectedCost) {
        checkStateOfCurrent();
        if (!Objects.equal(expectedCost, current.getCost())) {
            throw assertionError(expectedCost, current.getCost());
        }
        return this;
    }

    private static AssertionError assertionError(@Nullable Object expected, @Nullable Object actual) {
        return new AssertionError("\nExpected: " + expected + "\ngot: " + actual);
    }
}
