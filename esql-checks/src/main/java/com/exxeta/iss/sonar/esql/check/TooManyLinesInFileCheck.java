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

import java.util.List;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.NoSqale;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;

@Rule(key = "TooManyLinesInFile")
@NoSqale
@ActivatedByDefault
public class TooManyLinesInFileCheck extends SubscriptionVisitorCheck {
	private static final int DEFAULT = 2000;
	private static final String MESSAGE = "File \"%s\" has %d lines, which is greater than %d authorized. Split it into smaller files.";
	@RuleProperty(key = "maximum",
			description = "the maximum authorized lines",
			defaultValue = "" + DEFAULT)
	public int maximum = DEFAULT;



	

	  @Override
	  public void visitNode(Tree tree) {
	    if (!((InternalSyntaxToken) tree).isEOF()) {
	      return;
	    }

	    SyntaxToken token = (SyntaxToken) tree;
	    int lines = token.line();

	    if (lines > maximum) {
	      String fileName = getContext().getEsqlFile().fileName();
	      addIssue(new FileIssue(this, String.format(MESSAGE, fileName, lines, maximum)));
	    }
	  }

	  @Override
	  public List<Tree.Kind> nodesToVisit() {
	    return ImmutableList.of(Tree.Kind.TOKEN);
	  }
}
