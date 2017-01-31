package com.exxeta.iss.sonar.esql.tree.declaration;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;

public interface BindingElementTree extends Tree {

	  List<IdentifierTree> bindingIdentifiers();

	}
