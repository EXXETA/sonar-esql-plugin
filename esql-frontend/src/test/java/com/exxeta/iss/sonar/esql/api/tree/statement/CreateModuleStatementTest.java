package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateModuleStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import org.junit.jupiter.api.Test;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateModuleStatementTest extends EsqlTreeModelTest<CreateModuleStatementTreeImpl> {

    @Test
    void createModule() {
        assertThat(Kind.CREATE_MODULE_STATEMENT)
                .matches("CREATE COMPUTE MODULE AB\n END MODULE;");
    }

    @Test
    void modelTest() throws Exception {
        CreateModuleStatementTree tree = parse("CREATE COMPUTE MODULE abc END MODULE ;", Kind.CREATE_MODULE_STATEMENT);
        assertNotNull(tree);
        assertNotNull(tree.createKeyword());
        assertEquals("CREATE", tree.createKeyword().text());
        assertNotNull(tree.moduleType());
        assertEquals("COMPUTE", tree.moduleType().text());
        assertNotNull(tree.moduleKeyword());
        assertEquals("MODULE", tree.moduleKeyword().text());
        assertNotNull(tree.moduleName());
        assertEquals("abc", tree.moduleName().firstToken().text());
        assertNotNull(tree.moduleStatementsList());
        assertEquals(0, tree.moduleStatementsList().statements().size());
        assertNotNull(tree.endKeyword());
        assertEquals("END", tree.endKeyword().text());
        assertNotNull(tree.moduleKeyword2());
        assertEquals("MODULE", tree.moduleKeyword2().text());
        assertNotNull(tree.semi());
        assertEquals(";", tree.semi().text());


    }

    @Test
    void createDatabaseEventModule() {
        assertThat(Kind.CREATE_MODULE_STATEMENT)
            .matches("CREATE DATABASEEVENT MODULE AB\n END MODULE;");
    }
}
