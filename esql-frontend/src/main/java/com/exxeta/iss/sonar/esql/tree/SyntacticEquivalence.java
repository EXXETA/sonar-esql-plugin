/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;

public final class SyntacticEquivalence {

  private SyntacticEquivalence() {
  }

  /**
   * @return true, if nodes are syntactically equivalent
   */
  public static boolean areEquivalent(List<? extends Tree> leftList, List<? extends Tree> rightList) {
    if (leftList.size() != rightList.size() || leftList.isEmpty()) {
      return false;
    }

    for (int i = 0; i < leftList.size(); i++) {
      Tree left = leftList.get(i);
      Tree right = rightList.get(i);
      if (!areEquivalent(left, right)) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return true, if nodes are syntactically equivalent
   */
  public static boolean areEquivalent(@Nullable Tree leftNode, @Nullable Tree rightNode) {
    return areEquivalent((EsqlTree) leftNode, (EsqlTree) rightNode);
  }

  private static boolean areEquivalent(@Nullable EsqlTree leftNode, @Nullable EsqlTree rightNode) {
    if (leftNode == rightNode) {
      return true;
    }
    if (leftNode == null || rightNode == null) {
      return false;
    }
    if (leftNode.getKind() != rightNode.getKind()) {
      return false;
    } else if (leftNode.isLeaf()) {
      return areLeafsEquivalent(leftNode, rightNode);
    }

    Iterator<Tree> iteratorA = leftNode.childrenIterator();
    Iterator<Tree> iteratorB = rightNode.childrenIterator();

    while (iteratorA.hasNext() && iteratorB.hasNext()) {
      if (!areEquivalent(iteratorA.next(), iteratorB.next())) {
        return false;
      }
    }

    return !iteratorA.hasNext() && !iteratorB.hasNext();
  }

  /**
   * Caller must guarantee that nodes of the same kind.
   */
  @VisibleForTesting
  protected static boolean areLeafsEquivalent(EsqlTree leftNode, EsqlTree rightNode) {
    if (leftNode instanceof IdentifierTree) {
      return Objects.equal(((IdentifierTree) leftNode).name(), ((IdentifierTree) rightNode).name());
    } else if (leftNode instanceof SyntaxToken) {
      return Objects.equal(((SyntaxToken) leftNode).text(), ((SyntaxToken) rightNode).text());
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  
	public static ExpressionTree skipParentheses(ExpressionTree tree) {
		if (tree == null) {
			return null;
		} else if (tree.is(Tree.Kind.PARENTHESISED_EXPRESSION)) {
			return skipParentheses(((ParenthesisedExpressionTree) tree).expression());
		} else {
			return tree;
		}
	}

}
