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
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;


/**
 * This Java class is created to implement the logic to avoid the depricated methods.
 * @author Sapna Singh
 *
 */
@Rule(key = "DepricatedMethod")
public class DepricatedMethodCheck extends DoubleDispatchVisitorCheck {
	private static final String MESSAGE = "Depricated methods should not be used.";

	private List<String> Methods = new ArrayList<>();


	private static final String DEFAULT_DEPRICATED_METHODS = "BITSTREAM";
	@RuleProperty(
			key = "DepricatedMethod",
			description = "Depricated methods should not be used.",
			defaultValue = "" + DEFAULT_DEPRICATED_METHODS)
	public String depricatedMethods = DEFAULT_DEPRICATED_METHODS;

	@Override
	public void visitProgram(ProgramTree tree) {
		super.visitProgram(tree);

		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		int i = 0;
		for (String line : lines) {
			i = i + 1;	
			String upperCaseTheLine = line.toUpperCase();

			for (String depricatedMethod : Methods) {


				if(upperCaseTheLine.contains(depricatedMethod)  && splitByComma(depricatedMethods).contains(depricatedMethod)){

					addIssue(new LineIssue(this, i,   MESSAGE ));
				}

			}

		}
	}
	@Override
	public void visitCallExpression(CallExpressionTree tree) {
		if (tree.functionName() instanceof IdentifierTree) {
			Methods.add(((IdentifierTree)tree.functionName()).name());
		}
		super.visitCallExpression(tree);
	}

	public static List<String> splitByComma(String v){
		return Arrays.asList(v.split("\\,"));
	}


}

