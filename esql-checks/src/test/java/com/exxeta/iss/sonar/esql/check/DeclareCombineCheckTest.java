/**
 * This java class is created to implement the logic for checking if variable is initialised or not,
 * if more than one variable of same datatype is found uninitialised then declare statement could be combined.
 *
 * @author Prerana Agarkar
 */

package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.jupiter.api.Test;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

class DeclareCombineCheckTest {

    @Test
    void test() {
        EsqlCheckVerifier.verify(new DeclareCombineCheck(), new File("src/test/resources/declareCombine.esql"));

    }
}