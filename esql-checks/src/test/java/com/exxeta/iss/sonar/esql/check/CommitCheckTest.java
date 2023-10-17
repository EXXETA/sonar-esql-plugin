package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;
import java.io.File;
import org.junit.jupiter.api.Test;

class CommitCheckTest {

    @Test
    void test() {

        EsqlCheckVerifier.issues(new RollbackCheck(), new File("src/test/resources/commitRollback.esql"))
            .next().atLine(5).withMessage("ROLLBACK should not be called explicitly. Otherwise the messageflow can't handle the transaction.")
            .noMore();
    }
}
