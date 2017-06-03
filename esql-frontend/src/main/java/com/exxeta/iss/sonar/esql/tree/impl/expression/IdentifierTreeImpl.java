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
package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.symbols.Scope;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class IdentifierTreeImpl extends EsqlTree implements IdentifierTree, TypableTree {

	  private final InternalSyntaxToken nameToken;
	  private final Kind kind;
	  private Usage usage = null;
	  private Symbol symbol = null;
	  private TypeSet types = TypeSet.emptyTypeSet();
	  private Scope scope;

	  public IdentifierTreeImpl(Kind kind, InternalSyntaxToken nameToken) {
	    this.kind = kind;
	    this.nameToken = Preconditions.checkNotNull(nameToken);
	  }

	  @Override
	  public Kind getKind() {
	    return kind;
	  }

	  @Override
	  public SyntaxToken identifierToken() {
	    return nameToken;
	  }

	  @Override
	  public String name() {
	    return identifierToken().text();
	  }

	  @Override
	  public String toString() {
	    return name();
	  }

	  @Override
	  public Optional<Usage> symbolUsage() {
	    return Optional.ofNullable(usage);
	  }

	  @Override
	  public final Optional<Symbol> symbol() {
	    if (usage == null) {
	      return Optional.empty();
	    }
	    return Optional.of(usage.symbol());
	  }

	  public void setSymbolUsage(Usage usage) {
	    this.usage = usage;
	  }


	  public void setSymbol(Symbol symbol) {
	    this.symbol = symbol;
	  }

	  @Override
	  public TypeSet types() {
	    if (symbol == null) {
	      return types.immutableCopy();
	    } else {
	      return symbol.types();
	    }
	  }

	  @Override
	  public void add(Type type) {
	    if (symbol == null) {
	      types.add(type);
	    } else {
	      symbol.addType(type);
	    }
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
	    return Iterators.<Tree>singletonIterator(nameToken);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitIdentifier(this);
	  }

	  @Override
	  public Scope scope(){
	    return scope;
	  }

	  public void scope(Scope scope) {
	    this.scope = scope;
	  }

	  @Override
	  public List<IdentifierTree> bindingIdentifiers() {
	    return ImmutableList.of((IdentifierTree) this);
	  }
	}