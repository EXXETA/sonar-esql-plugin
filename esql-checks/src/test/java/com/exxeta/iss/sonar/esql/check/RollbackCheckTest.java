package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;
import java.io.File;
import org.junit.jupiter.api.Test;

class RollbackCheckTest {

    @Test
    void test() {

        EsqlCheckVerifier.issues(new CommitCheck(), new File("src/test/resources/commitRollback.esql"))
            .next().atLine(6).withMessage("COMMIT should not be called explicitly. Otherwise the messageflow can't handle the transaction.")
            .noMore();
    }
}
