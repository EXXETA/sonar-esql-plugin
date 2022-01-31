package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ImmediatelyReturnedVariableCheckTest {
    @Test
    void test() {
        EsqlCheckVerifier.verify(new ImmediatelyReturnedVariableCheck(), new File("src/test/resources/ImmediatelyReturnedVariable.esql"));
    }
}
