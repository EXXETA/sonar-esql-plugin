package com.exxeta.iss.sonar.esql.api.visitors;

import java.io.File;


import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;

public interface TreeVisitorContext {
	
	
	  ProgramTree getTopTree();

	  EsqlFile getEsqlFile();

	  SymbolModel getSymbolModel();
	  
	  File getFile();

}
