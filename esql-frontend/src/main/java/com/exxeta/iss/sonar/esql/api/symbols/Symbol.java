package com.exxeta.iss.sonar.esql.api.symbols;

import com.exxeta.iss.sonar.esql.api.tree.symbols.Scope;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IdentifierTreeImpl;
import com.google.common.annotations.Beta;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Beta
public class Symbol {

  public enum Kind {
    VARIABLE("variable"),
    FUNCTION("function"),
    PROCEDURE("procedure"),
    MODULE("module");

    private final String value;

    Kind(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

  }
  private final String name;

  private Kind kind;
  private boolean builtIn;
  private Scope scope;
  private List<Usage> usages = new LinkedList<>();
  private TypeSet types;

  public Symbol(String name, Kind kind, Scope scope) {
    this.name = name;
    this.kind = kind;
    this.builtIn = false;
    this.scope = scope;
    this.types = TypeSet.emptyTypeSet();
  }

  public void addUsage(Usage usage) {
    usages.add(usage);
    ((IdentifierTreeImpl) usage.identifierTree()).setSymbol(this);
  }

  public Collection<Usage> usages() {
    return Collections.unmodifiableList(usages);
  }

  public Symbol setBuiltIn(boolean isBuiltIn) {
    this.builtIn = isBuiltIn;
    return this;
  }

  public Scope scope() {
    return scope;
  }

  public String name() {
    return name;
  }

  public boolean builtIn() {
    return builtIn;
  }

  public boolean is(Symbol.Kind kind) {
    return kind.equals(this.kind);
  }

  public Kind kind() {
    return kind;
  }

  public void setKind(Kind kind) {
    this.kind = kind;
  }

  public void addTypes(Set<Type> type) {
    types.addAll(type);
  }

  public void addType(Type type) {
    types.add(type);
  }

  public TypeSet types() {
    return types.immutableCopy();
  }

  /**
   * @return true if symbol created with var, let or const keywords or implicitly
   */
 /* public boolean isVariable() {
    return kind == Kind.LET_VARIABLE || kind == Kind.CONST_VARIABLE || kind == Kind.VARIABLE;
  }*/

  @Override
  public String toString() {
    return name();
  }
}
