package com.exxeta.iss.sonar.esql.tree.symbols.type;

import com.exxeta.iss.sonar.esql.api.symbols.Type;

public enum PrimitiveType implements Type {
  UNKNOWN {
    @Override
    public Kind kind() {
      return Kind.UNKNOWN;
    }

    @Override
    public Callability callability() {
      return Callability.UNKNOWN;
    }
  },
  NUMBER {
    @Override
    public Kind kind() {
      return Kind.NUMBER;
    }

    @Override
    public Callability callability() {
      return Callability.NON_CALLABLE;
    }
  },
  CHARACTER {
    @Override
    public Kind kind() {
      return Kind.CHARACTER;
    }

    @Override
    public Callability callability() {
      return Callability.NON_CALLABLE;
    }
  },
  BOOLEAN {
    @Override
    public Kind kind() {
      return Kind.BOOLEAN;
    }

    @Override
    public Callability callability() {
      return Callability.NON_CALLABLE;
    }
  };

  @Override
  public String toString() {
    return this.kind().name();
  }
}
