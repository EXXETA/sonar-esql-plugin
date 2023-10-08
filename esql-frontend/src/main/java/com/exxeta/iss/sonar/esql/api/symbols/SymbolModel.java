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
package com.exxeta.iss.sonar.esql.api.symbols;

import java.util.Set;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.symbols.Scope;


public interface SymbolModel {
	  /**
	   * Returns all symbols in script
	   */
	  Set<Symbol> getSymbols();

	  /**
	   * @param kind kind of symbols to look for
	   * @return list of symbols with the given kind
	   */
	  Set<Symbol> getSymbols(Symbol.Kind kind);

	  /**
	   * @param name name of symbols to look for
	   * @return list of symbols with the given name
	   */
	  Set<Symbol> getSymbols(String name);

	  /**
	   * @param tree
	   * @return scope corresponding to this tree. Returns Null if no scope found
	   */
	  @Nullable
	  Scope getScope(Tree tree);
}
