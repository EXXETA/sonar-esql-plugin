/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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

import java.io.File;

import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.SymbolModelImpl;
import com.exxeta.iss.sonar.esql.compat.CompatibleInputFile;

public class EsqlVisitorContext implements TreeVisitorContext {

	  private final ProgramTree tree;
	  private final CompatibleInputFile compatibleInputFile;
	  private final SymbolModel symbolModel;

	  public EsqlVisitorContext(ProgramTree tree, CompatibleInputFile compatibleInputFile, Settings settings) {
	    this.tree = tree;
	    this.compatibleInputFile = compatibleInputFile;

	    this.symbolModel = new SymbolModelImpl();
	    SymbolModelImpl.build(this, settings);
	  }

	  @Override
	  public ProgramTree getTopTree() {
	    return tree;
	  }

	  @Override
	  public EsqlFile getEsqlFile() {
	    return compatibleInputFile;
	  }

	  @Override
	  public SymbolModel getSymbolModel() {
	    return symbolModel;
	  }
	  
	  @Override
	  public File getFile() {
	    return compatibleInputFile.file();
	  }
	}

