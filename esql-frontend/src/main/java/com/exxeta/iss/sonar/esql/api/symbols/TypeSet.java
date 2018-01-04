/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.symbols.Type.Kind;


public class TypeSet implements Set<Type> {
	  private Set<Type> types;

	  public TypeSet() {
	    types = new HashSet<>();
	  }

	  @Override
	  public int size() {
	    return types.size();
	  }

	  @Override
	  public boolean isEmpty() {
	    return types.isEmpty();
	  }

	  @Override
	  public boolean contains(Object o) {
	    return types.contains(o);
	  }

	  @Override
	  public Iterator<Type> iterator() {
	    return types.iterator();
	  }

	  @Override
	  public Object[] toArray() {
	    return types.toArray();
	  }

	  @Override
	  public <T> T[] toArray(T[] a) {
	    return types.toArray(a);
	  }

	  @Override
	  public boolean add(Type type) {
	    return types.add(type);
	  }

	  @Override
	  public boolean remove(Object o) {
	    return types.remove(o);
	  }

	  @Override
	  public boolean containsAll(Collection<?> c) {
	    return types.containsAll(c);
	  }

	  @Override
	  public boolean addAll(Collection<? extends Type> c) {
	    return types.addAll(c);
	  }

	  @Override
	  public boolean retainAll(Collection<?> c) {
	    return types.retainAll(c);
	  }

	  @Override
	  public boolean removeAll(Collection<?> c) {
	    return types.removeAll(c);
	  }

	  @Override
	  public void clear() {
	    types.clear();
	  }

	  public boolean containsOnlyAndUnique(Type.Kind kind) {
	    return size() == 1 && iterator().next().kind() == kind;
	  }

	  /*
	 * @return true if set contains instances of specified kind and only them.
	 */
	  public boolean contains(Type.Kind kind) {
	    for (Type type : types) {
	      if (type.kind() == kind) {
	        return true;
	      }
	    }
	    return false;
	  }

	  /*
	   * @return true if set contains instances of specified kind and only them.
	   */
	  public boolean containsOnly(Type.Kind kind) {
	    for (Type type : types) {
	      if (type.kind() != kind) {
	        return false;
	      }
	    }
	    return !types.isEmpty();
	  }

	  public static TypeSet emptyTypeSet() {
	    return new TypeSet();
	  }

	  @Nullable
	  public Type element() {
	    if (isEmpty()) {
	      return null;
	    } else {
	      return iterator().next();
	    }
	  }

	  /**
	   * Returns Type, which is the only not UNKNOWN element of TypeSet.
	   * Otherwise (if the only element is UNKNOWN or there are more than one element) result is null.
	   */
	  @Nullable
	  public Type getUniqueKnownType() {
	    if (size() == 1) {
	      Type type = iterator().next();
	      if (!type.kind().equals(Kind.UNKNOWN)) {
	        return type;
	      }
	    }
	    return null;
	  }

	  @Override
	  public boolean equals(Object o) {
	    return types.equals(o);
	  }

	  @Override
	  public int hashCode() {
	    return types.hashCode();
	  }

	  @Override
	  public String toString() {
	    return types.toString();
	  }

	  public TypeSet immutableCopy() {
	    TypeSet copy = new TypeSet();
	    copy.types = Collections.unmodifiableSet(types);
	    return copy;
	  }

	  /*
	   * @return the instance of type with specified kind. Return null if there are several or none of types of such kind.
	   */
	  @Nullable
	  public Type getUniqueType(Type.Kind kind) {
	    Type result = null;
	    for (Type type : types) {
	      if (type.kind().equals(kind)) {
	        if (result == null) {
	          result = type;
	        } else {
	          return null;
	        }
	      }
	    }
	    return result;
	  }

	}
