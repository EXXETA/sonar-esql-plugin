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

import java.util.List;
import java.util.regex.Pattern;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.GenericTokenType;

@Rule(key = ConstantNameCheck.CHECK_KEY)
public class ConstantNameCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "ConstantName";

	private static final String DEFAULT_FORMAT = "^[A-Z_]{0,30}$";

	@RuleProperty(key = "format", description = "regular expression", defaultValue = "" + DEFAULT_FORMAT)
	public String format = DEFAULT_FORMAT;

	private Pattern pattern;

	public String getFormat() {
		return format;
	}

	public ConstantNameCheck() {
		pattern = Pattern.compile(getFormat());
	}

	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		super.visitDeclareStatement(tree);

		boolean isConstant = (tree.constantKeyword() != null) || (tree.sharedExt()!=null && tree.sharedExt().text().equalsIgnoreCase("EXTERNAL"));

		if (isConstant) {
			for (int i = 0; i < tree.nameList().size(); i++) {
				if (!pattern.matcher(tree.nameList().get(i).text()).matches()) {
					addIssue(new PreciseIssue(this,
							new IssueLocation(tree.nameList().get(i), tree.nameList().get(i),
									"Rename constant \"" + tree.nameList().get(i).text()
											+ "\" to match the regular expression " + format + ".")));

				}
			}
		}

	}

}
