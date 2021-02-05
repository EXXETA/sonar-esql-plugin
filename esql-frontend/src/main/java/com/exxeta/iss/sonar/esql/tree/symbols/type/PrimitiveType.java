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

public enum PrimitiveType implements Type {
  UNKNOWN {
    @Override
    public Kind kind() {
      return Kind.UNKNOWN;
    }

    @Override
    public Callability callability() {
      return Callability.UNKNOWN;
    }
  },
  INTEGER {
    @Override
    public Kind kind() {
      return Kind.NUMBER;
    }

    @Override
    public Callability callability() {
      return Callability.NON_CALLABLE;
    }
  },
  CHARACTER {
    @Override
    public Kind kind() {
      return Kind.CHARACTER;
    }

    @Override
    public Callability callability() {
      return Callability.NON_CALLABLE;
    }
  },
  BOOLEAN {
    @Override
    public Kind kind() {
      return Kind.BOOLEAN;
    }

    @Override
    public Callability callability() {
      return Callability.NON_CALLABLE;
    }
  };

  @Override
  public String toString() {
    return this.kind().name();
  }
}
