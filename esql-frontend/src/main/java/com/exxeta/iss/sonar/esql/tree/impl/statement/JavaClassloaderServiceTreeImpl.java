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

package com.exxeta.iss.sonar.esql.tree.impl.statement;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.JavaClassloaderServiceTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

import java.util.Iterator;

public class JavaClassloaderServiceTreeImpl extends EsqlTree implements JavaClassloaderServiceTree {
    private final InternalSyntaxToken classloaderKeyword;
    private final InternalSyntaxToken classLoaderConfigurableServiceName;

    public JavaClassloaderServiceTreeImpl(InternalSyntaxToken classloaderKeyword, InternalSyntaxToken classLoaderConfigurableServiceName) {
        this.classloaderKeyword = classloaderKeyword;
        this.classLoaderConfigurableServiceName = classLoaderConfigurableServiceName;
    }

    @Override
    public InternalSyntaxToken classloaderKeyword(){
        return classloaderKeyword;
    }

    @Override
    public InternalSyntaxToken classLoaderConfigurableServiceName(){
        return classLoaderConfigurableServiceName;
    }

    @Override
    public Kind getKind() {
        return Kind.JAVA_CLASSLOADER_SERVICE;
    }

    @Override
    public Iterator<Tree> childrenIterator() {
        return Iterators.forArray(classloaderKeyword, classLoaderConfigurableServiceName);
    }
    @Override
    public void accept(DoubleDispatchVisitor visitor) {
        visitor.javaClassloaderService(this);
    }
}
