package com.exxeta.iss.sonar.esql.api.symbols;

import java.util.Set;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.Scope;


public interface SymbolModel {
	  /**
	   * Returns all symbols in script
	   */
	  Set<Symbol> getSymbols();

	  /**
	   * @param kind kind of symbols to look for
	   * @return list of symbols with the given kind
	   */
	  Set<Symbol> getSymbols(Symbol.Kind kind);

	  /**
	   * @param name name of symbols to look for
	   * @return list of symbols with the given name
	   */
	  Set<Symbol> getSymbols(String name);

	  /**
	   * @param tree
	   * @return scope corresponding to this tree. Returns Null if no scope found
	   */
	  @Nullable
	  Scope getScope(Tree tree);
}
