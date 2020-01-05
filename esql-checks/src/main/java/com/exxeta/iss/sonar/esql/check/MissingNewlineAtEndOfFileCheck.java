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

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

@Rule(key = "MissingNewlineAtEndOfFile")
public class MissingNewlineAtEndOfFileCheck extends DoubleDispatchVisitorCheck {

  private static final String MESSAGE = "Add a new line at the end of this file.";

  @Override
  public void visitProgram(ProgramTree tree) {
	SyntaxToken lastToken = null;
	  
    if (!tree.esqlContents().items().isEmpty()) {
    	lastToken = ((EsqlTree) tree.esqlContents()).lastToken();
    } else if (tree.semiToken()!=null){
    	lastToken=tree.semiToken();
    } else if (tree.pathClause()!=null){
    	lastToken=((EsqlTree)tree.pathClause()).lastToken();
    } else if (tree.brokerSchemaStatement()!=null){
    	lastToken=((EsqlTree)tree.brokerSchemaStatement()).lastToken();
    }
    if (lastToken!=null){
      int lastLine = tree.EOFToken().line();
      int lastTokenLine = lastToken.endLine();

      if (lastLine == lastTokenLine) {
        addIssue(new FileIssue(this, MESSAGE));
      }
    }
  }

}
