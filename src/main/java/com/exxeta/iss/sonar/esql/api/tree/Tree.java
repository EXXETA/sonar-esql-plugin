package com.exxeta.iss.sonar.esql.api.tree;

import org.sonar.sslr.grammar.GrammarRuleKey;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;

public interface Tree {
	boolean is(Kind... kind);

	  void accept(DoubleDispatchVisitor visitor);
	  public enum Kind implements GrammarRuleKey {
		  
	  }
}
