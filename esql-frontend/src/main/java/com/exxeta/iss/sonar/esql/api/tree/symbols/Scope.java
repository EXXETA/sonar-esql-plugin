package com.exxeta.iss.sonar.esql.api.tree.symbols;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Symbol.Kind;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.google.common.collect.Maps;

public class Scope {
	  private Scope outer;
	  private final Tree tree;
	  protected Map<String, Symbol> symbols = Maps.newHashMap();
	  private final boolean isBlock;

	  /**
	   *
	   * @param outer parent scope
	   * @param tree syntax tree holding this scope (e.g. ScriptTree or BlockTree)
	   * @param isBlock pass true for block scopes (loops, if, etc.), false for function scopes (script, functions, getter, etc.)
	   */
	  public Scope(Scope outer, Tree tree, boolean isBlock) {
	    this.outer = outer;
	    this.tree = tree;
	    this.isBlock = isBlock;
	  }

	  public Tree tree() {
	    return tree;
	  }

	  public Scope outer() {
	    return outer;
	  }

	  /**
	   * @param name of the symbol to look for
	   * @return the symbol corresponding to the given name
	   */
	  public Symbol lookupSymbol(String name) {
	    Scope scope = this;
	    while (scope != null && !scope.symbols.containsKey(name)) {
	      scope = scope.outer;
	    }
	    return scope == null ? null : scope.symbols.get(name);
	  }

	  /**
	   * @param kind of the symbols to look for
	   * @return the symbols corresponding to the given kind
	   */
	  public List<Symbol> getSymbols(Symbol.Kind kind) {
	    List<Symbol> result = new LinkedList<>();
	    for (Symbol symbol : symbols.values()) {
	      if (symbol.is(kind)) {
	        result.add(symbol);
	      }
	    }
	    return result;
	  }

	  public boolean isGlobal() {
	    return tree.is(Tree.Kind.SCRIPT);
	  }

	  public void addSymbol(Symbol symbol) {
	    symbols.put(symbol.name(), symbol);
	  }

	  @Nullable
	  public Symbol getSymbol(String name) {
	    return symbols.get(name);
	  }

	  /**
	   * Returns true for block scopes (loops, if, etc.), false for function scopes (script, functions, getter, etc.)
	   */
	  public boolean isBlock() {
	    return isBlock;
	  }

}
