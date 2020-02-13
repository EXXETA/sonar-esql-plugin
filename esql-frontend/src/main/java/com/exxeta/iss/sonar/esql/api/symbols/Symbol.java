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
package com.exxeta.iss.sonar.esql.api.symbols;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IdentifierTreeImpl;
import com.exxeta.iss.sonar.esql.tree.symbols.Scope;
import com.google.common.annotations.Beta;

@Beta
public class Symbol {

  public enum Kind {
    VARIABLE("variable"),
    CONST_VARIABLE("read-only variable"),
    EXTERNAL_VARIABLE("user defined property"),
    FUNCTION("function"),
    PROCEDURE("procedure"),
    MODULE("module"), 
    PARAMETER("parameter");

    private final String value;

    Kind(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

  }
  private final String name;

  private Kind kind;
  private boolean builtIn;
  private Scope scope;
  private List<Usage> usages = new LinkedList<>();
  private TypeSet types;

  public Symbol(String name, Kind kind, Scope scope) {
    this.name = name;
    this.kind = kind;
    this.builtIn = false;
    this.scope = scope;
    this.types = TypeSet.emptyTypeSet();
  }

  private void addUsage(Usage usage) {
	    usages.add(usage);
	    ((IdentifierTreeImpl) usage.identifierTree()).setSymbolUsage(usage);
	  }

	  public void addUsage(IdentifierTree identifierTree, Usage.Kind usageKind) {
	    final Usage usage = new Usage(identifierTree, usageKind, this);
	    addUsage(usage);
	  }
  public Collection<Usage> usages() {
    return Collections.unmodifiableList(usages);
  }

  public Symbol setBuiltIn(boolean isBuiltIn) {
    this.builtIn = isBuiltIn;
    return this;
  }

  public Scope scope() {
    return scope;
  }

  public String name() {
    return name;
  }

  public boolean builtIn() {
    return builtIn;
  }

  public boolean is(Symbol.Kind kind) {
    return kind.equals(this.kind);
  }

  public Kind kind() {
    return kind;
  }

  public void setKind(Kind kind) {
    this.kind = kind;
  }

  public void addTypes(Set<Type> type) {
    types.addAll(type);
  }

  public void addType(Type type) {
    types.add(type);
  }

  public TypeSet types() {
    return types.immutableCopy();
  }

  /**
   * @return true if symbol created with CREATE
   */
  public boolean isVariable() {
    return kind == Kind.CONST_VARIABLE || kind == Kind.VARIABLE;
  }

  @Override
  public String toString() {
    return name();
  }
}
