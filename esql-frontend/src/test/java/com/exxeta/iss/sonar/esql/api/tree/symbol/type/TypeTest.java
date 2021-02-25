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
package com.exxeta.iss.sonar.esql.api.tree.symbol.type;

import org.sonar.api.batch.fs.InputFile;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import com.exxeta.iss.sonar.esql.utils.TestUtils;

public abstract class TypeTest extends EsqlTreeModelTest<Tree> {
  protected ProgramTree ROOT_NODE;
  protected SymbolModel SYMBOL_MODEL;

  protected Symbol getSymbol(String name) {
    return SYMBOL_MODEL.getSymbols(name).iterator().next();
  }

  protected void setUp(String filename) throws Exception {
    InputFile file = TestUtils.createTestInputFile("src/test/resources/ast/resolve/type/", filename);
    ROOT_NODE = parse(file.contents());
    SYMBOL_MODEL = new EsqlVisitorContext(ROOT_NODE, file, null).getSymbolModel();
  }
}
