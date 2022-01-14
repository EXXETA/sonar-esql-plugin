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
package com.exxeta.iss.sonar.esql.tree.symbols.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.Type.Kind;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class PrimitiveOperations {

  private static Map<OperationKey, Type> binaryOperationsResults = new HashMap<>();

  static {
    // +
    put(Kind.NUMBER, Kind.NUMBER, Tree.Kind.PLUS, PrimitiveType.INTEGER);

    
    //||
    put(Kind.CHARACTER, Kind.CHARACTER, Tree.Kind.CONCAT, PrimitiveType.CHARACTER);

  
  }

  private static final EnumSet<Tree.Kind> COMPARATIVE_OPERATORS = EnumSet.of(
    Tree.Kind.LESS_THAN,
    Tree.Kind.GREATER_THAN,
    Tree.Kind.LESS_THAN_OR_EQUAL_TO,
    Tree.Kind.GREATER_THAN_OR_EQUAL_TO,
    Tree.Kind.EQUAL_TO,
    Tree.Kind.NOT_EQUAL_TO
  );

  private static final EnumSet<Tree.Kind> ARITHMETIC_OPERATORS = EnumSet.of(
    Tree.Kind.MINUS,
    Tree.Kind.MULTIPLY,
    Tree.Kind.DIVIDE
  );
  private static final EnumSet<Tree.Kind> NUMBER_UNARY_OPERATORS = EnumSet.of(
  	Tree.Kind.UNARY_MINUS,
	Tree.Kind.UNARY_PLUS
  );
  private PrimitiveOperations() {
  }

  private static void put(Type.Kind leftOperandType, Type.Kind rightOperandType, Tree.Kind operation, Type result) {
    binaryOperationsResults.put(new OperationKey(leftOperandType, rightOperandType, operation), result);
  }

  @Nullable
  static Type getType(ExpressionTree leftOperand, ExpressionTree rightOperand, Tree.Kind operationKind) {
    if (COMPARATIVE_OPERATORS.contains(operationKind)) {
      return PrimitiveType.BOOLEAN;

    } else if (ARITHMETIC_OPERATORS.contains(operationKind)) {
      return PrimitiveType.INTEGER;

    } else {
      return getType(leftOperand.types().getUniqueKnownType(), rightOperand.types().getUniqueKnownType(), operationKind);
    }
  }

  @Nullable
  static Type getType(@Nullable Type leftOperandType, @Nullable Type rightOperandType, Tree.Kind operationKind) {
    if (leftOperandType != null && rightOperandType != null) {
      return binaryOperationsResults.get(new OperationKey(leftOperandType.kind(), rightOperandType.kind(), operationKind));
    }
    return null;
  }
  
  @Nullable
  static Type getType(UnaryExpressionTree expressionTree) {
    Tree.Kind kind = ((EsqlTree) expressionTree).getKind();
    if (NUMBER_UNARY_OPERATORS.contains(kind)) {
      return PrimitiveType.INTEGER;

    }  else if (expressionTree.is(Tree.Kind.LOGICAL_COMPLEMENT)) {
      return PrimitiveType.BOOLEAN;
    }

    return null;
  }

  private static class OperationKey {
    Type.Kind leftOperandType;
    Type.Kind rightOperandType;
    Tree.Kind operation;

    public OperationKey(Type.Kind leftOperandType, Type.Kind rightOperandType, Tree.Kind operation) {
      this.leftOperandType = leftOperandType;
      this.rightOperandType = rightOperandType;
      this.operation = operation;
    }

    @Override
    public boolean equals(@Nullable Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      OperationKey that = (OperationKey) o;
      return leftOperandType == that.leftOperandType && rightOperandType == that.rightOperandType && operation == that.operation;
    }

    @Override
    public int hashCode() {
      int result = leftOperandType.hashCode();
      result = 31 * result + rightOperandType.hashCode();
      result = 31 * result + operation.hashCode();
      return result;
    }
  }

}
