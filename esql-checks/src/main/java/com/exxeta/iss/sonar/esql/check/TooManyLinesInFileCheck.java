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

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Grammar;

@Rule(key = TooManyLinesInFileCheck.CHECK_KEY, priority = Priority.MAJOR, name = "Files should not have too many lines", tags = Tags.BRAIN_OVERLOAD)
@NoSqale
@ActivatedByDefault
public class TooManyLinesInFileCheck extends SquidCheck<Grammar> {
	public static final String CHECK_KEY = "TooManyLinesInFile";
	private static final int DEFAULT = 2000;
	@RuleProperty(key = "maximum",
			description = "the maximum allowed lines",
			defaultValue = "" + DEFAULT)
	public int maximum = DEFAULT;

	@Override
	public void init() {
		subscribeTo(GenericTokenType.EOF);
	}

	@Override
	public void visitNode(AstNode astNode) {
		int lines = astNode.getTokenLine();
		if (lines > maximum) {
			String message = "File \"{0}\" has {1} lines, which is greater than {2} authorized. Split it into smaller files.";
			getContext().createFileViolation(this, message,
					getContext().getFile().getName(), lines, maximum);
		}
	}
}
