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

class TrailingCommentsCheckTest {
    TrailingCommentsCheck check = new TrailingCommentsCheck();

    @Test
    void test() {
        EsqlCheckVerifier.verify(check, new File("src/test/resources/trailingComments.esql"));
    }

    @Test
    void withoutPattern() {
        TrailingCommentsCheck check = new TrailingCommentsCheck();
        check.setLegalCommentPattern("");
        EsqlCheckVerifier.issues(check, new File("src/test/resources/trailingComments.esql"))
                .next().atLine(8).withMessage("Move this trailing comment on the previous empty line.")
                .next().atLine(15).withMessage("Move this trailing comment on the previous empty line.")
                .next().atLine(18).withMessage("Move this trailing comment on the previous empty line.")
                .next().atLine(20).withMessage("Move this trailing comment on the previous empty line.")
                .next().atLine(23).withMessage("Move this trailing comment on the previous empty line.")
                .noMore();


    }


}

