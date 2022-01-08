/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CallStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import org.junit.jupiter.api.Test;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CallStatementTest extends EsqlTreeModelTest<CallStatementTreeImpl> {

    @Test
    void callStatement() {
        assertThat(Kind.CALL_STATEMENT)
                .matches("CALL myProc1();")
                .matches("CALL a.b.c.myProc1();")
                .matches("CALL myProc1() INTO cursor;")
                .matches("CALL myProc1() INTO OutputRoot.XMLNS.TestValue1;")
                .matches("CALL myProc1() EXTERNAL SCHEMA 'test';")
                .matches("CALL myProc1() IN Database.test;")
                .matches("CALL myFunc(inBody, outBody.ns01:ele01.{}[<], InputLocalEnvironment);")
        ;

    }

    @Test
    void modelTest() throws Exception {
        CallStatementTreeImpl tree = parse("CALL myProc1() INTO cursor;", Kind.CALL_STATEMENT);
        assertNotNull(tree);
        assertNotNull(tree.callKeyword());
        assertEquals("CALL", tree.callKeyword().text());
        assertNotNull(tree.routineName());
        assertNotNull(tree.openParen());
        assertEquals("(", tree.openParen().text());
        assertNotNull(tree.parameterList());
        assertFalse(tree.parameterList().iterator().hasNext());
        assertNotNull(tree.closeParen());
        assertEquals(")", tree.closeParen().text());
        assertNotNull(tree.intoKeyword());
        assertEquals("INTO", tree.intoKeyword().text());
        assertNotNull(tree.intoTarget());
        assertNotNull(tree.semi());
        assertNull(tree.inKeyword());
        assertNull(tree.schemaReference());
        assertNull(tree.externalKeyword());
        assertNull(tree.schemaKeyword());
        assertNull(tree.externalSchemaName());

    }

}
