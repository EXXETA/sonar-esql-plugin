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
package com.exxeta.iss.sonar.esql.highlighter;

import java.util.List;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.compat.CompatibleInputFile;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;

public class HighlighterVisitor extends SubscriptionVisitor{

	  private final SensorContext sensorContext;
	  private NewHighlighting highlighting;


	  public HighlighterVisitor(SensorContext sensorContext) {
		    this.sensorContext = sensorContext;
		  }

	  @Override
	  public List<Kind> nodesToVisit() {
	    return ImmutableList.<Kind>builder()
	      .add(
	        Kind.NUMERIC_LITERAL,
	        Kind.STRING_LITERAL,
	        Kind.TOKEN)
	      .build();
	  }

	  @Override
	  public void visitFile(Tree scriptTree) {
	    highlighting = sensorContext.newHighlighting().onFile(((CompatibleInputFile) getContext().getEsqlFile()).wrapped());
	  }

	  @Override
	  public void leaveFile(Tree scriptTree) {
	    highlighting.save();
	  }

	  @Override
	  public void visitNode(Tree tree) {
	    SyntaxToken token = null;
	    TypeOfText code = null;

	    if (tree.is(Kind.TOKEN)) {
	      highlightToken((InternalSyntaxToken) tree);

	    } else if (tree.is(Kind.STRING_LITERAL)) {
	      token = ((LiteralTreeImpl) tree).token();
	      code = TypeOfText.STRING;

	    } else if (tree.is(Kind.NUMERIC_LITERAL)) {
	      token = ((LiteralTreeImpl) tree).token();
	      code = TypeOfText.CONSTANT;

	    }

	    if (token != null) {
	      highlight(token, code);
	    }
	  }

	  private void highlightToken(InternalSyntaxToken token) {
	    if (isKeyword(token.text())) {
	      highlight(token, TypeOfText.KEYWORD);
	    }
	    highlightComments(token);
	  }

	  private void highlightComments(InternalSyntaxToken token) {
	    TypeOfText type;
	    for (SyntaxTrivia trivia : token.trivias()) {
	      if (trivia.text().startsWith("/**")) {
	        type = TypeOfText.STRUCTURED_COMMENT;
	      } else {
	        type = TypeOfText.COMMENT;
	      }
	      highlight(trivia, type);
	    }
	  }

	  private void highlight(SyntaxToken token, TypeOfText type) {
	    highlighting.highlight(token.line(), token.column(), token.endLine(), token.endColumn(), type);
	  }

	  private static boolean isKeyword(String text) {
		    for (String keyword : EsqlReservedKeyword.keywordValues()) {
			      if (keyword.equals(text)) {
			        return true;
			      }
			    }
		    for (String keyword : EsqlNonReservedKeyword.keywordValues()) {
			      if (keyword.equals(text)) {
			        return true;
			      }
			    }
	    return false;
	  }
}
