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
import com.exxeta.iss.sonar.esql.utils.TestInputFile;

public class CreateRoutineTreeImplTest {

  @Test
  public void should_return_outer_scope_symbol_usages() throws Exception {
    InputFile inputFile = new TestInputFile("src/test/resources/tree/", "outer_scope_variables.esql");
    final EsqlVisitorContext context = createContext(inputFile);
    CreateRoutineTreeImpl functionTree = (CreateRoutineTreeImpl) context.getTopTree().esqlContents().descendants().filter(tree -> tree.is(Tree.Kind.CREATE_PROCEDURE_STATEMENT)).findFirst().get();
    Set<String> usages = functionTree.outerScopeSymbolUsages().map(usage -> usage.identifierTree().name()).collect(Collectors.toSet());
    assertThat(usages).containsExactlyInAnyOrder("a", "b", "writeOnly");
  }

}
