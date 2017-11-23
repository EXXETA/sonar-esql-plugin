/**
 * 
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
			String  temp = line.toString();

			if(temp.trim().startsWith("SET Environment")){

				String[] strArr1 = temp.split(Pattern.quote("="));

				String envSubElement = strArr1[0];
				if(! strArr1[1].isEmpty()){

					String envSubElement1 = envSubElement.substring(temp.indexOf("Environment")+12,temp.indexOf("="));
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







