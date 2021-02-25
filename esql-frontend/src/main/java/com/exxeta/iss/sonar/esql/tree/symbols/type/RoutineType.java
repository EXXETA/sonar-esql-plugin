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
package com.exxeta.iss.sonar.esql.tree.symbols.type;

import com.exxeta.iss.sonar.esql.api.symbols.Type;

public class RoutineType implements Type {

  protected Callability callability;

  protected RoutineType(Callability callability) {
    this.callability = callability;
  }

  @Override
  public Kind kind() {
    return Kind.OBJECT;
  }

  @Override
  public Callability callability() {
    return callability;
  }

  public static RoutineType create() {
    return create(Callability.UNKNOWN);
  }

  public static RoutineType create(Callability callability) {
    return new RoutineType(callability);
  }

  @Override
  public String toString() {
    return this.kind().name();
  }

 

  public enum BuiltInObjectType implements Type {
    DATE {
      @Override
      public Kind kind() {
        return Kind.DATE;
      }

      @Override
      public Callability callability() {
        return Callability.NON_CALLABLE;
      }
    },
  }

 
}
