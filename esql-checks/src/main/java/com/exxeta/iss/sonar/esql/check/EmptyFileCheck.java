/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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

import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableSet;

@Rule(key = "EmptyFile")
public class EmptyFileCheck extends SubscriptionVisitorCheck{

	public static final String MESSAGE = "File \"%s\" has 0 lines of code.";
	
	  @Override
	  public void visitNode(Tree tree) {
	    if (!((InternalSyntaxToken) tree).isEOF()) {
	      return;
	    }

	    SyntaxToken token = (SyntaxToken) tree;
	    int lines = token.line();

	    if (lines < 2) {
	      String fileName = getContext().getEsqlFile().fileName();
	      addIssue(new FileIssue(this, String.format(MESSAGE, fileName)));
	    }
	  }

	  @Override
	  public Set<Tree.Kind> nodesToVisit() {
	    return ImmutableSet.of(Tree.Kind.TOKEN);
	  }
}
