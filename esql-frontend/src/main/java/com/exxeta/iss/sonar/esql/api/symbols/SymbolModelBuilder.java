package com.exxeta.iss.sonar.esql.api.symbols;

import java.util.Set;

import com.exxeta.iss.sonar.esql.api.tree.symbols.Scope;


public interface SymbolModelBuilder {
	  Scope globalScope();

	  void addScope(Scope scope);

	  Set<Scope> getScopes();

	  Symbol declareSymbol(String name, Symbol.Kind kind, Scope scope);

	  Symbol declareBuiltInSymbol(String name, Symbol.Kind kind, Scope scope);

}
