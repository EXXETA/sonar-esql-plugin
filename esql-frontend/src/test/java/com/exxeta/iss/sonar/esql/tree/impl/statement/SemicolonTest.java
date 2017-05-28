package com.exxeta.iss.sonar.esql.tree.impl.statement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class SemicolonTest extends EsqlTreeModelTest<SetStatementTreeImpl> {

  @Test
  public void with_semicolon() throws Exception {
    SetStatementTree tree = parse("SET a = 1;", Kind.SET_STATEMENT);

    assertThat(tree.semiToken()).isNotNull();
    assertThat(tree.semiToken().text()).isEqualTo(";");
  }

}
