package com.exxeta.iss.sonar.esql.api.visitors;

import java.io.File;

import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.SymbolModelImpl;

public class EsqlVisitorContext implements TreeVisitorContext {

	  private final ProgramTree tree;
	  private final File file;
	  private final SymbolModel symbolModel;

	  public EsqlVisitorContext(ProgramTree tree, File file, Settings settings) {
	    this.tree = tree;
	    this.file = file;

	    this.symbolModel = new SymbolModelImpl();
	    SymbolModelImpl.build(this, settings);
	  }

	  @Override
	  public ProgramTree getTopTree() {
	    return tree;
	  }

	  @Override
	  public File getFile() {
	    return file;
	  }

	  @Override
	  public SymbolModel getSymbolModel() {
	    return symbolModel;
	  }
	}

