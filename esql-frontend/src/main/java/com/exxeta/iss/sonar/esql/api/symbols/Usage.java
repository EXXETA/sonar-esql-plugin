package com.exxeta.iss.sonar.esql.api.symbols;

import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;

public class Usage {
	 public enum Kind {
		    DECLARATION,
		    DECLARATION_WRITE,
		    // parameters in function signature
		    LEXICAL_DECLARATION,
		    WRITE,
		    READ,
		    READ_WRITE;
		  }

		  private Kind kind;
		  private IdentifierTree identifierTree;

		  /**
		   * @param identifierTree - this tree contains only symbol name identifier (we need it for symbol highlighting)
		   * @param kind           - kind of usage
		   */
		  private Usage(IdentifierTree identifierTree, Kind kind) {
		    this.kind = kind;
		    this.identifierTree = identifierTree;
		  }

		  public Symbol symbol() {
		    return identifierTree.symbol();
		  }

		  public Kind kind() {
		    return kind;
		  }

		  public IdentifierTree identifierTree() {
		    return identifierTree;
		  }

		  public static Usage create(IdentifierTree symbolTree, Kind kind) {
		    return new Usage(symbolTree, kind);
		  }

		  public boolean isDeclaration() {
		    return kind == Kind.DECLARATION_WRITE || kind == Kind.DECLARATION;
		  }

		  public boolean isWrite() {
		    return kind == Kind.DECLARATION_WRITE || kind == Kind.WRITE || kind == Kind.READ_WRITE;
		  }
}
