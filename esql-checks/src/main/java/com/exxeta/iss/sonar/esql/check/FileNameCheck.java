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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;

@Rule(key = FileNameCheck.CHECK_KEY)
public class FileNameCheck extends SubscriptionVisitorCheck {
	
	public static final String CHECK_KEY = "FileName";
	private static final String DEFAULT_FORMAT = "^[A-Z][a-zA-Z]{1,30}\\.esql$";
	private static final String MESSAGE = "Rename file \"%s\"  to match the regular expression %s.";
	
	@RuleProperty(key = "format",
			description="regular expression",
			defaultValue = "" + DEFAULT_FORMAT)
	public String format = DEFAULT_FORMAT;
	
	private Pattern pattern;
	public FileNameCheck() {
	}
	
	
	@Override
	public List<Kind> nodesToVisit() {
		return Collections.emptyList();
	}

	@Override
	public void visitFile(Tree scriptTree) {
		if (!Pattern.compile(format).matcher(getContext().getEsqlFile().fileName()).matches()){
			addIssue(new FileIssue(this, String.format(MESSAGE, getContext().getEsqlFile().fileName(), format)));
		}
		super.visitFile(scriptTree);
	}
	
	
}
