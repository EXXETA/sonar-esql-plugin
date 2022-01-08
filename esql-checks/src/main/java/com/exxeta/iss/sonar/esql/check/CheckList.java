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
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import com.exxeta.iss.sonar.esql.check.naming.ConstantNameCheck;
import com.exxeta.iss.sonar.esql.check.naming.FileNameCheck;
import com.exxeta.iss.sonar.esql.check.naming.FunctionNameCheck;
import com.exxeta.iss.sonar.esql.check.naming.ModuleNameCheck;
import com.exxeta.iss.sonar.esql.check.naming.ProcedureNameCheck;
import com.exxeta.iss.sonar.esql.check.naming.SubElementNameCheck;
import com.exxeta.iss.sonar.esql.check.naming.VariableNameCheck;
import com.google.common.collect.ImmutableList;

public final class CheckList {

  public static final String REPOSITORY_KEY = "esql";

  public static final String REPOSITORY_NAME = "SonarAnalyzer";

  public static final String SONAR_WAY_PROFILE = "Sonar way";

  private CheckList() {
  }

  public static List<Class<?>> getChecks() {
    return ImmutableList.<Class<?>> of(
            BinaryOperatorSeparatedBySpaceCheck.class,
            BlankLineBeforeCommentsCheck.class,
            BooleanEqualityComparisonCheck.class,
            BooleanInversionCheck.class,
            BooleanLiteralCheck.class,
            CardinalityInLoopCheck.class,
            CaseAtLeastThreeWhenCheck.class,
            CaseWithTooManyWhensCheck.class,
            CaseWithoutElseCheck.class,
            CommentRegularExpressionCheck.class,
            CommentedCodeCheck.class,
            CommentsCheck.class,
            ConditionParenthesisCheck.class,
            CyclomaticComplexityCheck.class,
            DeclareCombineCheck.class,
            DeleteFromWithoutWhereCheck.class,
            DeprecatedMethodCheck.class,
            DuplicateConditionIfElseAndCaseWhensCheck.class,
            ElseIfWithoutElseCheck.class,
            EmptyBlockCheck.class,
            EmptyFileCheck.class,
            EmptyMainFunctionCheck.class,
            EvalCheck.class,
            FileHeaderCheck.class,
            FilterNodeHaveOnlyOneReturnCheck.class,
            FilterNodeModifyMessageCheck.class,
            FunctionProcedureLengthCheck.class,
            HardCodedCredentialsCheck.class,
            HardcodedIpCheck.class,
            HardcodedURICheck.class,
            IdenticalExpressionOnBinaryOperatorCheck.class,
            IfConditionalAlwaysTrueOrFalseCheck.class,
            ImmediatelyReturnedVariableCheck.class,
            InitializeVariablesCheck.class,
            InsertBlankLineBetweenFuncProcCheck.class,
            IterateStatementCheck.class,
            KeyWordCaseCheck.class,
            LineLengthCheck.class,
            LoopWithoutLeaveCheck.class,
            MissingNewlineAtEndOfFileCheck.class,
            NavigatingTreeCouldBeReferenceCheck.class,
            NestedIfDepthCheck.class,
            NonReservedKeywordCheck.class,
            OneStatementPerLineCheck.class,
            ParameterWithDirectionCheck.class,
            ParsingErrorCheck.class,
            PassThruStatementCheck.class,
            PropagateConsistencyCheck.class,
            PropagateToLabelCheck.class,
            RecursionCheck.class,
            RoutineCommentsCheck.class,
            RoutineWithExcessiveReturnsCheck.class,
            SelectAllCheck.class,
            SelfAssignmentCheck.class,
            SleepCheck.class,
            StringLiteralDuplicatedCheck.class,
            TooManyIterateOrLeaveInLoopCheck.class,
            TooManyLinesInFileCheck.class,
            TooManyParametersCheck.class,
            TrailingCommentsCheck.class,
            TrailingWhitespaceCheck.class,
            UndocumentedModuleCheck.class,
            UndocumentedRoutineCheck.class,
            UnknownMessageDomainCheck.class,
            UnreachableCodeCheck.class,
            UnusedParameterCheck.class,
            UnusedRoutineCheck.class,
            UnusedVariableCheck.class,
            UseBrokerSchemaCheck.class,
            UselessParenthesesCheck.class,
            VariablesSubtreeCheck.class,
            XmlnscDomainCheck.class,
            //NAMING
            ConstantNameCheck.class,
            FileNameCheck.class,
            FunctionNameCheck.class,
            ModuleNameCheck.class,
            ProcedureNameCheck.class,
            SubElementNameCheck.class,
            VariableNameCheck.class
    );
  }

}
