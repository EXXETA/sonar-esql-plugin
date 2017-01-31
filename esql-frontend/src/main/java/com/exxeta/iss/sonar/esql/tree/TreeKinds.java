package com.exxeta.iss.sonar.esql.tree;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class TreeKinds {

  private static final Kind[] ASSIGNMENT_KINDS = {
    Kind.ASSIGNMENT,
    Kind.EXPONENT_ASSIGNMENT,
    Kind.MULTIPLY_ASSIGNMENT,
    Kind.DIVIDE_ASSIGNMENT,
    Kind.REMAINDER_ASSIGNMENT,
    Kind.PLUS_ASSIGNMENT,
    Kind.MINUS_ASSIGNMENT,
    Kind.LEFT_SHIFT_ASSIGNMENT,
    Kind.RIGHT_SHIFT_ASSIGNMENT,
    Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT,
    Kind.AND_ASSIGNMENT,
    Kind.XOR_ASSIGNMENT,
    Kind.OR_ASSIGNMENT
  };

  private static final Kind[] INC_DEC_KINDS = {
    Tree.Kind.POSTFIX_INCREMENT,
    Tree.Kind.PREFIX_INCREMENT,
    Tree.Kind.POSTFIX_DECREMENT,
    Tree.Kind.PREFIX_DECREMENT
  };

  private static final Kind[] FUNCTION_KINDS = {
    Kind.FUNCTION_DECLARATION,
    Kind.FUNCTION_EXPRESSION,
    Kind.GET_METHOD,
    Kind.SET_METHOD
  };

  private TreeKinds() {
    // This class has only static methods
  }

  public static boolean isAssignment(Tree tree) {
    return tree.is(ASSIGNMENT_KINDS);
  }

  public static boolean isIncrementOrDecrement(Tree tree) {
    return tree.is(INC_DEC_KINDS);
  }

  public static boolean isFunction(Tree tree) {
    return tree.is(FUNCTION_KINDS);
  }

  public static List<Kind> functionKinds() {
    return ImmutableList.copyOf(FUNCTION_KINDS);
  }

}
