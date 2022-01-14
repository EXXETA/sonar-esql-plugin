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
package com.exxeta.iss.sonar.esql.tree.symbols.type;

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;

public class ProcedureType extends RoutineType {

  private CreateProcedureStatementTree createProcedureStatementTree;

  protected ProcedureType() {
    super(Callability.CALLABLE);
  }

  @Override
  public Kind kind() {
    return Kind.PROCEDURE;
  }

  public static ProcedureType create(CreateProcedureStatementTree createProcedureStatementTree) {
    ProcedureType type = new ProcedureType();
    type.createProcedureStatementTree = createProcedureStatementTree;
    return type;
  }

  public CreateProcedureStatementTree createProcedureStatementTree() {
    return createProcedureStatementTree;
  }
}
