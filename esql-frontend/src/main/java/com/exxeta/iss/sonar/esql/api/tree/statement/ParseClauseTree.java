/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface ParseClauseTree extends Tree {

	
	SyntaxToken parseKeyword();
	SyntaxToken openingParenthesis();
	ExpressionTree expression();
	
	InternalSyntaxToken optionsSeparator();
	ExpressionTree optionsExpression();
	InternalSyntaxToken encodingSeparator();
	ExpressionTree encodingExpression();
	InternalSyntaxToken ccsidSeparator();
	ExpressionTree ccsidExpression();
	InternalSyntaxToken setSeparator();
	ExpressionTree setExpression();
	InternalSyntaxToken typeSeparator();
	ExpressionTree typeExpression();
	InternalSyntaxToken formatSeparator();
	ExpressionTree formatExpression();

	SyntaxToken closingParenthesis();
}
