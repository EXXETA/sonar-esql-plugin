package com.exxeta.iss.sonar.esql.api.tree.symbols;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Symbol.Kind;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModelBuilder;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;
import com.exxeta.iss.sonar.esql.tree.symbols.SymbolVisitor;

public class SymbolModelImpl implements SymbolModel, SymbolModelBuilder {

	  private Set<Symbol> symbols = new HashSet<>();
	  private Set<Scope> scopes = new HashSet<>();
	  private Scope globalScope;

	  public static void build(TreeVisitorContext context, @Nullable Settings settings) {
	    Map<Tree, Scope> treeScopeMap = getScopes(context);

	   // new HoistedSymbolVisitor(treeScopeMap).scanTree(context);
	    new SymbolVisitor(treeScopeMap).scanTree(context);
	    //new TypeVisitor(settings).scanTree(context);
	  }

	  private static Map<Tree, Scope> getScopes(TreeVisitorContext context) {
	    ScopeVisitor scopeVisitor = new ScopeVisitor();
	    scopeVisitor.scanTree(context);
	    return scopeVisitor.getTreeScopeMap();
	  }

	  @Override
	  public Scope globalScope() {
	    return globalScope;
	  }

	  @Override
	  public void addScope(Scope scope) {
	    if (scopes.isEmpty()) {
	      globalScope = scope;
	    }
	    scopes.add(scope);
	  }

	  @Override
	  public Set<Scope> getScopes() {
	    return scopes;
	  }

	  @Override
	  public Symbol declareSymbol(String name, Symbol.Kind kind, Scope scope) {
	    Symbol symbol = scope.getSymbol(name);
	    if (symbol == null) {
	      symbol = new Symbol(name, kind, scope);
	      scope.addSymbol(symbol);
	      symbols.add(symbol);

	    } else if (kind.equals(Kind.FUNCTION)) {
	      symbol.setKind(Kind.FUNCTION);

	    }
	    return symbol;
	  }

	  @Override
	  public Symbol declareBuiltInSymbol(String name, Symbol.Kind kind, Scope scope) {
	    Symbol symbol = scope.getSymbol(name);
	    if (symbol == null) {
	      symbol = new Symbol(name, kind, scope);
	      symbol.setBuiltIn(true);
	      scope.addSymbol(symbol);
	      symbols.add(symbol);
	    }
	    return symbol;
	  }

	  /**
	   * Returns all symbols in script
	   */
	  @Override
	  public Set<Symbol> getSymbols() {
	    return Collections.unmodifiableSet(symbols);
	  }

	  /**
	   * @param kind kind of symbols to look for
	   * @return list of symbols with the given kind
	   */
	  @Override
	  public Set<Symbol> getSymbols(Symbol.Kind kind) {
	    Set<Symbol> result = new HashSet<>();
	    for (Symbol symbol : symbols) {
	      if (kind.equals(symbol.kind())) {
	        result.add(symbol);
	      }
	    }
	    return result;
	  }

	  /**
	   * @param name name of symbols to look for
	   * @return list of symbols with the given name
	   */
	  @Override
	  public Set<Symbol> getSymbols(String name) {
	    Set<Symbol> result = new HashSet<>();
	    for (Symbol symbol : symbols) {
	      if (name.equals(symbol.name())) {
	        result.add(symbol);
	      }
	    }
	    return result;
	  }

	  @Nullable
	  @Override
	  public Scope getScope(Tree tree) {
	    for (Scope scope : getScopes()) {
	      if (scope.tree().equals(tree)) {
	        return scope;
	      }
	    }
	    return null;
	  }

	}
