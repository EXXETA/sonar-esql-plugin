package com.exxeta.iss.sonar.esql.api.visitors;


public interface TreeVisitor {
	  TreeVisitorContext getContext();

	  void scanTree(TreeVisitorContext context);
}
