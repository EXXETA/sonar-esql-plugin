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
package com.exxeta.iss.sonar.esql.tree.impl;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class SeparatedList<T> implements List<T> {

  private final List<T> list;
  private final List<InternalSyntaxToken> separators;
  private final boolean elementFirst;

  public SeparatedList(List<T> list, List<InternalSyntaxToken> separators) {
	  this(list, separators, true);
  }
  public SeparatedList(List<T> list, List<InternalSyntaxToken> separators, boolean elementFirst) {
    Preconditions.checkArgument(
      list.size() == separators.size() + 1 || list.size() == separators.size(),
      "Instanciating a SeparatedList with inconsistent number of elements (%s) and separators (%s)",
      list.size(), separators.size());
    this.list = list;
    this.separators = separators;
    this.elementFirst=elementFirst;
  }

  public InternalSyntaxToken getSeparator(int i) {
    return separators.get(i);
  }

  public List<InternalSyntaxToken> getSeparators() {
    return separators;
  }


  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean add(T e) {
    return list.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    return list.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public T get(int index) {
    return list.get(index);
  }

  @Override
  public T set(int index, T element) {
    return list.set(index, element);
  }

  @Override
  public void add(int index, T element) {
    list.add(index, element);
  }

  @Override
  public T remove(int index) {
    return list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  public Iterator<Tree> elementsAndSeparators(final Function<T, ? extends Tree> elementTransformer) {
    return new ElementAndSeparatorIterator(elementTransformer);
  }

  private final class ElementAndSeparatorIterator extends UnmodifiableIterator<Tree> {

    private final Function<T, ? extends Tree> elementTransformer;
    private final Iterator<T> elementIterator = list.iterator();
    private final Iterator<InternalSyntaxToken> separatorIterator = separators.iterator();
    private boolean nextIsElement = elementFirst;

    private ElementAndSeparatorIterator(Function<T, ? extends Tree> elementTransformer) {
      this.elementTransformer = elementTransformer;
    }

    @Override
    public boolean hasNext() {
      return elementIterator.hasNext() || separatorIterator.hasNext();
    }

    @Override
    public Tree next() {
      Tree next = nextIsElement ? elementTransformer.apply(elementIterator.next()) : separatorIterator.next();
      nextIsElement = !nextIsElement;
      return next;
    }
  }

}
