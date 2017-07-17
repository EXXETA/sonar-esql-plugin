/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl;

import static com.exxeta.iss.sonar.esql.utils.TestUtils.createContext;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateRoutineTreeImpl;
import com.exxeta.iss.sonar.esql.utils.TestUtils;

public class CreateRoutineTreeImplTest {

  @Test
  public void should_return_outer_scope_symbol_usages() throws Exception {
	  InputFile inputFile = TestUtils.createTestInputFile("src/test/resources/tree/", "outer_scope_variables.esql");
    final EsqlVisitorContext context = createContext(inputFile);
    CreateRoutineTreeImpl functionTree = (CreateRoutineTreeImpl) context.getTopTree().esqlContents().descendants().filter(tree -> tree.is(Tree.Kind.CREATE_PROCEDURE_STATEMENT)).findFirst().get();
    Set<String> usages = functionTree.outerScopeSymbolUsages().map(usage -> usage.identifierTree().name()).collect(Collectors.toSet());
    assertThat(usages).containsExactlyInAnyOrder("a", "b", "writeOnly");
  }

}
