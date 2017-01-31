package com.exxeta.iss.sonar.esql.se;

public enum Truthiness {
  TRUTHY, FALSY, UNKNOWN;

  public Truthiness not() {
    switch (this) {
      case TRUTHY:
        return FALSY;
      case FALSY:
        return TRUTHY;
      default:
        return UNKNOWN;
    }
  }
}
