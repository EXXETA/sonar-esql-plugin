/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql.check;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class CheckList {

  public static final String REPOSITORY_KEY = "esql";

  public static final String REPOSITORY_NAME = "Sonar";

  public static final String SONAR_WAY_PROFILE = "Sonar way";

  private CheckList() {
  }

  public static List<Class> getChecks() {
    return ImmutableList.<Class> of(
            LineLengthCheck.class,
            ParsingErrorCheck.class,
//        XPathCheck.class,
//        CommentedCodeCheck.class,
//        FunctionComplexityCheck.class,
//        DebuggerStatementCheck.class,
//        WithStatementCheck.class,
//        EqEqEqCheck.class,
//        CommentRegularExpressionCheck.class,
//        EvalCheck.class,
        OneStatementPerLineCheck.class,
//        SemicolonCheck.class,
//        AlwaysUseCurlyBracesCheck.class,
//        MultilineStringLiteralsCheck.class,
//        SingleQuoteStringLiteralsCheck.class,
//        ArrayAndObjectConstructorsCheck.class,
//        BitwiseOperatorsCheck.class,
//        PrimitiveWrappersCheck.class,
//        ForInCheck.class,
//        FunctionDeclarationsWithinBlocksCheck.class,
//        TrailingCommaCheck.class,
//        AssignmentWithinConditionCheck.class,
//        LabelPlacementCheck.class,
//        UnreachableCodeCheck.class,
//        ConditionalOperatorCheck.class,
//        ParenthesesCheck.class,
//        SwitchWithoutDefaultCheck.class,
//        NonEmptyCaseWithoutBreakCheck.class,
//        ContinueStatementCheck.class,
//        HtmlCommentsCheck.class,
//        EmptyBlockCheck.class,
//        ElseIfWithoutElseCheck.class,
//        ExcessiveParameterListCheck.class,
//        CollapsibleIfStatementsCheck.class,
//        ConstructorFunctionsForSideEffectsCheck.class,
//        FutureReservedWordsCheck.class,
//        DuplicateFunctionArgumentCheck.class,
//        DuplicatePropertyNameCheck.class,
//        OctalNumberCheck.class,
//        StrictModeCheck.class,
//        ConditionalCommentCheck.class,
//        TabCharacterCheck.class,
//        RedeclaredVariableCheck.class,
//        RedeclaredFunctionCheck.class,
//        TrailingWhitespaceCheck.class,
//        TrailingCommentCheck.class,
//        MissingNewlineAtEndOfFileCheck.class,
//        BoundOrAssignedEvalOrArgumentsCheck.class,
//        SameNameForFunctionAndVariableCheck.class,
        CommentRegularExpressionCheck.class,
        NestedIfDepthCheck.class);
  }

}
