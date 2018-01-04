/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class SyntacticEquivalenceTest extends EsqlTreeModelTest<Tree> {

  @Test
  public void test() throws Exception {
    Tree tree1 = parse("if a then end if;", Tree.Kind.IF_STATEMENT);
    Tree tree2 = parse("if a then end if;", Tree.Kind.IF_STATEMENT);

    Tree tree3 = parse("if b then end if;", Tree.Kind.IF_STATEMENT);
    Tree tree4 = parse("if b then leave aaa; end if;", Tree.Kind.IF_STATEMENT);
    Tree tree5 = parse("DECLARE a CHAR;", Tree.Kind.DECLARE_STATEMENT);

    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree1)).isTrue();
    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree2)).isTrue();

    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree3)).isFalse();
    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree4)).isFalse();
    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree4)).isFalse();

    assertThat(SyntacticEquivalence.areEquivalent(tree1, null)).isFalse();
    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree5)).isFalse();
  }

  @Test
  public void test_equivalence_for_tree_list() throws Exception {
	  CallStatementTree tree1 = parse("CALL f(a, b, c) ;", Tree.Kind.CALL_STATEMENT);
    CallStatementTree tree2 = parse("CALL f(a, b, c) ;", Tree.Kind.CALL_STATEMENT);
    CallStatementTree tree3 = parse("CALL f(a, b) ;", Tree.Kind.CALL_STATEMENT);
    CallStatementTree tree4 = parse("CALL f(a, b, b) ;", Tree.Kind.CALL_STATEMENT);

    assertThat(SyntacticEquivalence.areEquivalent(tree1.parameterList(), tree1.parameterList())).isTrue();
    assertThat(SyntacticEquivalence.areEquivalent(tree1.parameterList(), tree2.parameterList())).isTrue();

    assertThat(SyntacticEquivalence.areEquivalent(tree1.parameterList(), tree3.parameterList())).isFalse();
    assertThat(SyntacticEquivalence.areEquivalent(tree1.parameterList(), tree4.parameterList())).isFalse();
  }

  @Test
  public void test_equivalence_for_empty_tree_list() throws Exception {
    CallStatementTree tree1 = parse("CALL f() ;", Tree.Kind.CALL_STATEMENT);
    CallStatementTree tree2 = parse("CALL f() ;", Tree.Kind.CALL_STATEMENT);

    assertThat(SyntacticEquivalence.areEquivalent(tree1.parameterList(), tree1.parameterList())).isFalse();
    assertThat(SyntacticEquivalence.areEquivalent(tree1.parameterList(), tree2.parameterList())).isFalse();
  }

  @Test
  public void test_equivalence_for_tokens() throws Exception {
    Tree tree1 = parse("IF TRUE THEN END IF;", Tree.Kind.IF_STATEMENT);
    Tree tree2 = parse("IF FALSE THEN END IF;", Tree.Kind.IF_STATEMENT);

    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree1)).isTrue();
    assertThat(SyntacticEquivalence.areEquivalent(tree1, tree2)).isFalse();
  }
  
  @Test
  public void test_skip_parenthesis() throws Exception {
	    IfStatementTree tree1 = parse("IF (TRUE) THEN END IF;", Tree.Kind.IF_STATEMENT);
	    IfStatementTree tree2 = parse("IF TRUE THEN END IF;", Tree.Kind.IF_STATEMENT);

    assertThat(SyntacticEquivalence.skipParentheses(tree1.condition()).is(Kind.BOOLEAN_LITERAL)).isTrue();
  }

}
