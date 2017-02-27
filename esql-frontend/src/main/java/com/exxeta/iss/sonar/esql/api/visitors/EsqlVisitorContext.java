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

