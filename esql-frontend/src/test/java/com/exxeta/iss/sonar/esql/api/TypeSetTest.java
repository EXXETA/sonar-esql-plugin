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
package com.exxeta.iss.sonar.esql.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.tree.symbols.type.FunctionType;
import com.exxeta.iss.sonar.esql.tree.symbols.type.RoutineType;
import com.exxeta.iss.sonar.esql.tree.symbols.type.PrimitiveType;

import static org.assertj.core.api.Assertions.assertThat;

class TypeSetTest {

  private TypeSet typeSet1;
  private TypeSet typeSet2;
  private TypeSet typeSet3;

  @BeforeEach
  void setUp() {
    typeSet1 = TypeSet.emptyTypeSet();
    typeSet1.add(PrimitiveType.INTEGER);

    typeSet2 = TypeSet.emptyTypeSet();
    typeSet2.add(PrimitiveType.INTEGER);
    typeSet2.add(PrimitiveType.UNKNOWN);

    typeSet3 = TypeSet.emptyTypeSet();
    typeSet3.add(FunctionType.create());
    typeSet3.add(FunctionType.create());
  }

  @Test
  void size() {
    assertThat(typeSet1.size()).isEqualTo(1);
    assertThat(typeSet2.size()).isEqualTo(2);
    assertThat(typeSet3.size()).isEqualTo(2);
  }

  @Test
  void is_empty() {
    assertThat(TypeSet.emptyTypeSet().isEmpty()).isTrue();
    assertThat(typeSet1.isEmpty()).isFalse();
  }

  @Test
  void contains_object() {
    assertThat(typeSet1.contains(PrimitiveType.INTEGER)).isTrue();
    assertThat(typeSet2.contains(RoutineType.create())).isFalse();
  }

  @Test
  void iterator() {
    assertThat(typeSet1.iterator().hasNext()).isTrue();
    assertThat(TypeSet.emptyTypeSet().iterator().hasNext()).isFalse();
  }

  @Test
  void to_array() {
    Object[] array = typeSet3.toArray();
    assertThat(array.length).isEqualTo(2);
    assertThat(array[0]).isInstanceOf(RoutineType.class);
  }

  @Test
  void to_array_t() {
    Type[] array = typeSet3.toArray(new Type[2]);
    assertThat(array.length).isEqualTo(2);
    assertThat(array[0]).isInstanceOf(RoutineType.class);
  }

  @Test
  void add_type() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    RoutineType type = RoutineType.create();
    typeSet.add(type);
    assertThat(typeSet.size()).isEqualTo(1);
    assertThat(typeSet.element()).isEqualTo(type);
  }

  @Test
  void remove_object() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    RoutineType type = RoutineType.create();

    typeSet.add(type);
    assertThat(typeSet.size()).isEqualTo(1);

    typeSet.remove(type);
    assertThat(typeSet).isEmpty();
  }

  @Test
  void contains_all() {
    assertThat(typeSet2.containsAll(typeSet1)).isTrue();
  }

  @Test
  void add_all() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    typeSet.addAll(typeSet3);
    assertThat(typeSet.containsAll(typeSet3)).isTrue();
  }

  @Test
  void retain_all() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    typeSet.addAll(typeSet2);

    typeSet.retainAll(typeSet1);

    assertThat(typeSet.size()).isEqualTo(1);
    assertThat(typeSet.element()).isEqualTo(PrimitiveType.INTEGER);
  }

  @Test
  void remove_all() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    typeSet.addAll(typeSet2);

    typeSet.removeAll(typeSet1);

    assertThat(typeSet.size()).isEqualTo(1);
    assertThat(typeSet.element()).isEqualTo(PrimitiveType.UNKNOWN);
  }

  @Test
  void clear() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    typeSet.addAll(typeSet2);

    typeSet.clear();

    assertThat(typeSet).isEmpty();
  }

  @Test
  void contains_only_and_unique_type_kind() {
    assertThat(typeSet1.containsOnlyAndUnique(Type.Kind.NUMBER)).isTrue();
    assertThat(typeSet3.containsOnlyAndUnique(Type.Kind.OBJECT)).isFalse();
  }

  @Test
  void contains_type_kind() {
    assertThat(typeSet1.contains(Type.Kind.UNKNOWN)).isFalse();
    assertThat(typeSet2.contains(Type.Kind.UNKNOWN)).isTrue();
    assertThat(typeSet3.contains(Type.Kind.OBJECT)).isTrue();
  }

  @Test
  void contains_only_type_kind() {
    assertThat(typeSet2.containsOnly(Type.Kind.NUMBER)).isFalse();
    assertThat(typeSet3.containsOnly(Type.Kind.OBJECT)).isTrue();
  }

  @Test
  void empty_type_set() {
    assertThat(TypeSet.emptyTypeSet()).isEmpty();
  }

  @Test
  void equals() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    typeSet.add(PrimitiveType.INTEGER);

    assertThat(typeSet1.equals(typeSet2)).isFalse();
    assertThat(typeSet1.equals(typeSet)).isTrue();
  }

  @Test
  void hash_code() {
    TypeSet typeSet = TypeSet.emptyTypeSet();
    typeSet.add(PrimitiveType.INTEGER);

    assertThat(typeSet1.hashCode()).isEqualTo(typeSet.hashCode());
    assertThat(typeSet1.hashCode()).isNotEqualTo(typeSet2.hashCode());
  }

  @Test
  void to_sting() {
    assertThat(TypeSet.emptyTypeSet().toString()).isEqualTo("[]");
    assertThat(typeSet1.toString()).isEqualTo("[NUMBER]");
    assertThat(typeSet2.toString()).contains("NUMBER");
    assertThat(typeSet2.toString()).contains("UNKNOWN");
  }

  @Test
  void immutable_copy() {
    TypeSet copy = typeSet1.immutableCopy();

    assertThat(copy.size()).isEqualTo(1);
    assertThat(copy.element()).isEqualTo(PrimitiveType.INTEGER);

    try {
      copy.add(PrimitiveType.INTEGER);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Test
  void get_unique_type() {
    assertThat(typeSet1.getUniqueType(Type.Kind.NUMBER)).isEqualTo(PrimitiveType.INTEGER);
    assertThat(typeSet3.getUniqueType(Type.Kind.OBJECT)).isNull();
    assertThat(typeSet3.getUniqueType(Type.Kind.UNKNOWN)).isNull();
  }
}
