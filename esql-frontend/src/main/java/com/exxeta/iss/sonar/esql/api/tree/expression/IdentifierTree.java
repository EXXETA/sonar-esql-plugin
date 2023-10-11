/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree.expression;

import java.util.Optional;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.declaration.BindingElementTree;
import com.exxeta.iss.sonar.esql.tree.symbols.Scope;


public interface IdentifierTree extends VariableReferenceTree, BindingElementTree {

	  SyntaxToken identifierToken();

	  String name();

	  /**
	   * @return {@link Usage} corresponding to this identifier. Empty optional is returned when there is no symbol available for this identifier (see {@link IdentifierTree#symbol()})
	   */
	  Optional<Usage> symbolUsage();

	  /**
	   * @return {@link Symbol} which is referenced by this identifier. No {@link Symbol} is returned in several cases:
	   * <ul>
	   *   <li>for {@link Kind#PROPERTY_IDENTIFIER}</li>
	   *   <li>for unresolved symbol (i.e. symbol being read without being written)</li>
	   * </ul>
	   * Note that {@link Kind#BINDING_IDENTIFIER} (used for symbol declaration) always has corresponding symbol.
	   * Note that this method is a shortcut for {@link IdentifierTree#symbolUsage()#symbol()}.
	   */
	  Optional<Symbol> symbol();

	  /**
	   * @return {@link Scope} instance in which this identifier appear
	   */
	  Scope scope();
	}
