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
import java.util.regex.Pattern;

import org.sonar.check.Rule;


import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;

import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;

import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;


/**
 * This java class is created to implement the logic to check sub-elements should be in UpperCamel-case and elements containing simple value should be in lowercase.
 * @author sapna singh
 *
 */
@Rule(key="SubElementName")
public class SubElementNameCheck extends DoubleDispatchVisitorCheck{

	private static final String MESSAGE = "sub-elements should be in UpperCamel-case and elements containing simple value should be in lowercase.";

	private static final String UPPERCASE_FORMAT = "^[A-Z][a-zA-Z0-9]*$";
	private static final String LOWERCASE_FORMAT = "^[a-z][a-zA-Z0-9]*$";



	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		int i = 0;
		for (String line : lines) {
			i =i+1;

			if(line.trim().startsWith("SET Environment")){

				String[] strArr1 = line.split(Pattern.quote("="));

				String envSubElement = strArr1[0];
				if(! strArr1[1].isEmpty()){

					String envSubElement1 = envSubElement.substring(line.indexOf("Environment")+12,line.indexOf('='));
					String[] strArray = envSubElement1.split(Pattern.quote("."));

					int strCount =0;
					for(String str:strArray){
						strCount++;

						if(!str.matches(UPPERCASE_FORMAT) && (strCount != strArray.length)){


							addIssue(new LineIssue(this, i,   MESSAGE ));
						}

					}

					String lastElement =strArray[strArray.length - 1].trim();
					if(!lastElement.matches(LOWERCASE_FORMAT)){
						addIssue(new LineIssue(this,  i,   MESSAGE ));
					}

				}



			}
		}

	}
}







