/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.visitors;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Configuration;

import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.tree.symbols.SymbolModelImpl;

public class EsqlVisitorContext implements TreeVisitorContext {

	  private final ProgramTree tree;
	  private final EsqlFile esqlFile;
	  private final SymbolModel symbolModel;

	  public EsqlVisitorContext(ProgramTree tree, InputFile inputFile, Configuration configuration) {
	    this.tree = tree;
	    this.esqlFile = new EsqlFileImpl(inputFile);

	    this.symbolModel = new SymbolModelImpl();
	    SymbolModelImpl.build(this, configuration);
	  }

	  @Override
	  public ProgramTree getTopTree() {
	    return tree;
	  }

	  @Override
	  public EsqlFile getEsqlFile() {
	    return esqlFile;
	  }

	  @Override
	  public SymbolModel getSymbolModel() {
	    return symbolModel;
	  }
	  
}
