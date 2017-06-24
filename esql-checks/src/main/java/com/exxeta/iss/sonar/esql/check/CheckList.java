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
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class CheckList {

  public static final String REPOSITORY_KEY = "esql";

  public static final String REPOSITORY_NAME = "SonarAnalyzer";

  public static final String SONAR_WAY_PROFILE = "Sonar way";

  private CheckList() {
  }

  public static List<Class> getChecks() {
    return ImmutableList.<Class> of(
        CommentRegularExpressionCheck.class,
        ElseIfWithoutElseCheck.class,
        FileNameCheck.class,
        FunctionNameCheck.class,
        IterateStatementCheck.class,
        LineLengthCheck.class,
        ModuleNameCheck.class,
        NestedIfDepthCheck.class,
        NonReservedKeywordCheck.class,
        OneStatementPerLineCheck.class,
        ParsingErrorCheck.class,
        ProcedureNameCheck.class,
        TooManyIterateOrLeaveInLoopCheck.class,
        TooManyLinesInFileCheck.class,
        UseBrokerSchemaCheck.class,
        VariableNameCheck.class,
        PropagateToLabelCheck.class,
        ConstantNameCheck.class,
        MissingNewlineAtEndOfFileCheck.class,
        UnusedRoutineCheck.class,
        EmptyBlockCheck.class,
        EmptyFileCheck.class,
        UselessParenthesesCheck.class,
        CaseAtLeastThreeWhenCheck.class,
        FileHeaderCheck.class,
        IdenticalExpressionOnBinaryOperatorCheck.class,
        IfConditionalAlwaysTrueOrFalseCheck.class,
        DuplicateConditionIfElseAndCaseWhensCheck.class,
        BooleanInversionCheck.class,
        HardCodedCredentialsCheck.class,
        HardcodedIpCheck.class,
        HardcodedURICheck.class,
        BooleanLiteralCheck.class,
        CaseWithoutElseCheck.class,
        CardinalityInLoopCheck.class,
        LoopWithoutLeaveCheck.class,
        EmptyMainFunctionCheck.class,
        UnknownMessageDomainCheck.class,
        ParameterWithDirectionCheck.class,
        DeleteFromWithoutWhereCheck.class,
        SelectAllCheck.class,
        SleepCheck.class,
        EvalCheck.class,
        BitstreamCheck.class,
        CaseWithTooManyWhensCheck.class,
        InitializeVariablesCheck.class
        
        /*
        
        IdenticalOperandOnBinaryExpressionCheck

- Environment Variables in the Variables-Subtree

- Use the SOAP domain not soap in the xmlnsc domain

- Use the xmlnsc domain not the xml or xmlns domain

- No comment before modlue, function or procedure

- Uppercase keywords

- IF/ELSEIF should be CASE

- Too many parameter in function/procedure

- Unreachable code after THROW or RETURN

- SOAP version should be 1.2 or 1.1

- Unused variable

- ExcessiveReturnsCheck
        
        
        */
        );
  }

}
