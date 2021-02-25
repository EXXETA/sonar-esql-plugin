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
package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

public class LiteralTreeImpl extends EsqlTree implements LiteralTree, TypableTree {

	  private final Kind kind;
	  private final SyntaxToken token;
	  private TypeSet types = TypeSet.emptyTypeSet();

	  public LiteralTreeImpl(Kind kind, SyntaxToken token) {
	    this.kind = Preconditions.checkNotNull(kind);
	    this.token = token;
	  }

	  @Override
	  public Kind getKind() {
	    return kind;
	  }

	  @Override
	  public String value() {
	    return token.text();
	  }


	  @Override
	  public TypeSet types() {
	    return types.immutableCopy();
	  }

	  @Override
	  public void add(Type type) {
	    this.types.add(type);
	  }

	  @Override
	  public SyntaxToken token() {
	    return token;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
	    return Iterators.<Tree>singletonIterator(token);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitLiteral(this);
	  }

	  @Override
	  public String toString() {
	    return token.text();
	  }
	}
