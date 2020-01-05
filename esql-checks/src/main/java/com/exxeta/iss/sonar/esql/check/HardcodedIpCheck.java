/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.google.common.base.Splitter;

@Rule(key="HardcodedIp")
public class HardcodedIpCheck extends DoubleDispatchVisitorCheck {

	private static final Matcher IP = Pattern.compile("[^\\d.]*?((?:\\d{1,3}\\.){3}\\d{1,3}(?!\\d|\\.)).*?")
			.matcher("");

	@Override
	public void visitLiteral(LiteralTree tree) {
		if (tree.is(Kind.STRING_LITERAL)) {
			IP.reset(tree.value());
		      if (IP.matches()) {
		        String ip = IP.group(1);
		        if (areAllBelow256(Splitter.on('.').split(ip))) {
		          addIssue(tree, "Make this IP '" + ip + "' address configurable.");
		        }
		      }
		}
	}
	
	private static boolean areAllBelow256(Iterable<String> numbersAsStrings) {
	    for (String numberAsString : numbersAsStrings) {
	      if (Integer.parseInt(numberAsString) > 255) {
	        return false;
	      }
	    }
	    return true;
	  }

}
