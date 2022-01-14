/**
 * This java class is created to implement the logic for checking if the line contains both code and comments,
 * if it contains both then trailing comments should be removed.
 *
 * @author Prerana Agarkar
 */

package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class TrailingWhitespaceCheckTest {
    TrailingCommentsCheck check = new TrailingCommentsCheck();

    @Test
    void test() {
        TrailingWhitespaceCheck check = new TrailingWhitespaceCheck();
        EsqlCheckVerifier.issues(check, new File("src/test/resources/trailingWhitespace.esql"))
                .next().atLine(3).withMessage("Remove the useless trailing whitespaces at the end of this line.")
                .noMore();
    }


}

