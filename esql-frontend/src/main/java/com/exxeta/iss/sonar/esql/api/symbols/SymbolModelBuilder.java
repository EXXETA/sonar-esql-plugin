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

import com.exxeta.iss.sonar.esql.tree.symbols.Scope;


public interface SymbolModelBuilder {
	  Scope globalScope();

	  void addScope(Scope scope);

	  Set<Scope> getScopes();

	  Symbol declareSymbol(String name, Symbol.Kind kind, Scope scope);

}
