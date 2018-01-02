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
package com.exxeta.iss.sonar.esql.api.tree.symbol.type;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Type;

public class RoutineTypeTest extends TypeTest {

  @Before
  public void setUp() throws Exception {
    super.setUp("routineType.esql");
  }

  @Test
  public void function_declaration() {
    Symbol f1 = getSymbol("f1");
    assertThat(f1.types().containsOnlyAndUnique(Type.Kind.FUNCTION)).isTrue();
  }

  @Test
  public void procedure_declaration() {
    Symbol proc1 = getSymbol("proc1");
    assertThat(proc1.types().containsOnlyAndUnique(Type.Kind.PROCEDURE)).isTrue();

  }

 
  @Test
  public void parameter_types() {
    Symbol p1 = getSymbol("p1");
    assertThat(p1.types().containsOnlyAndUnique(Type.Kind.NUMBER)).isTrue();

    Symbol p2 = getSymbol("p2");
    assertThat(p2.types().containsOnlyAndUnique(Type.Kind.CHARACTER)).isTrue();

    Symbol p3 = getSymbol("p3");
    assertThat(p3.types().containsOnlyAndUnique(Type.Kind.BOOLEAN)).isTrue();
  }

}
