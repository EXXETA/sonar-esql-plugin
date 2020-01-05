package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ImmediatelyReturnedVariableCheckTest {
    @Test
    public void test() {
        EsqlCheckVerifier.verify(new ImmediatelyReturnedVariableCheck(), new File("src/test/resources/ImmediatelyReturnedVariable.esql"));
    }
}
