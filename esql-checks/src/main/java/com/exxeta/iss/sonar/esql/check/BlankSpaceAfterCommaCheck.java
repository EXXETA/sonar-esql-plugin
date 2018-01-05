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

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;

/**
 * This java class is created to check that blank space should follow each comma in any ESQL statement
 * @author   Sapna singh
 *
 */
@Rule(key = "BlankSpaceAfterComma")
public class BlankSpaceAfterCommaCheck extends DoubleDispatchVisitorCheck{

	private static final String MESSAGE = "A blank space should follow each comma in any ESQL statement that makes use of commas outside of a string literal.";



	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		
		int linecounter = 0;
		for (String line : lines) {
			linecounter = linecounter + 1;	
	       
			String upperCaseTheLine = line.toUpperCase().trim();
			if(upperCaseTheLine.contains(",") ){
				int pos= upperCaseTheLine.indexOf(',');
				
						if (!upperCaseTheLine.substring(pos+1,pos+2).matches(EsqlLexer.WHITESPACE) ) {
							addIssue(new LineIssue(this, linecounter, MESSAGE));
				}
			}
		} 
	}
}
