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

import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "ProcedureName")
public class ProcedureNameCheck extends DoubleDispatchVisitorCheck {
	
	private static final String DEFAULT_FORMAT = "^[a-z][a-zA-Z0-9]{1,30}$";
	
	@RuleProperty(key = "format", 
			description="regular expression",
			defaultValue = "" + DEFAULT_FORMAT)
	public String format = DEFAULT_FORMAT;

	private Pattern pattern;

	
	
	public ProcedureNameCheck() {
		pattern = Pattern.compile(getFormat());
	}


	public String getFormat() {
		return format;
	}


	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		super.visitCreateProcedureStatement(tree);
		if (!pattern.matcher(tree.identifier().name()).matches()){
			 addIssue(new PreciseIssue(this, new IssueLocation(tree.identifier(), tree.identifier(), "Rename procedure \""+tree.identifier().name()+"\" to match the regular expression "+format+".")));
			 
		}
		
	}
	

}
