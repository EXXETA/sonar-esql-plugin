/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.exxeta.iss.sonar.esql.tree.symbols.type;

import com.exxeta.iss.sonar.esql.api.symbols.Type;

public class ObjectType implements Type {

  protected Callability callability;

  protected ObjectType(Callability callability) {
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

  public static ObjectType create() {
    return create(Callability.UNKNOWN);
  }

  public static ObjectType create(Callability callability) {
    return new ObjectType(callability);
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
