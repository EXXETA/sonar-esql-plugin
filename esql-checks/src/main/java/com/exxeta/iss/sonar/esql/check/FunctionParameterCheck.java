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

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
/**
 * This Java Class is created to ensure that all the functions do not have parameter count above the allowed threshold
 * @author Arjav Shah
 *
 */
@Rule(key = FunctionParameterCheck.CHECK_KEY, priority = Priority.MAJOR, name = "Function names should comply with a naming convention", tags = Tags.CONVENTION)
public class FunctionParameterCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "FunctionParameter";

	private static final int DEFAULT_PARAMETER_THRESHOLD = 10;

	@RuleProperty(key = "maximumParameterThreshold", description = "Maximum allowed threshold", defaultValue = "" + DEFAULT_PARAMETER_THRESHOLD)
	public int maximumParameterThreshold = DEFAULT_PARAMETER_THRESHOLD;
	
	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		super.visitCreateFunctionStatement(tree);
		if(tree.parameterList().size()>maximumParameterThreshold){
			addIssue(
					new PreciseIssue(this, new IssueLocation(tree.identifier(), tree.identifier(), "Reduce parameters for function \""
							+ tree.identifier().name() + "\". Parameter count : "+tree.parameterList().size()+". (Allowed Threshold : " + maximumParameterThreshold + ")")));
		}
	}

	

	

}
