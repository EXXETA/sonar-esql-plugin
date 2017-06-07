package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="ParameterWithDirection")
public class ParameterWithDirectionCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitParameter(ParameterTree tree) {
		
		if (tree.directionIndicator()==null){
			addIssue(tree, "Add a direction to the parameter \""+tree.identifier().name()+"\".");
		}
		
		super.visitParameter(tree);
	}
	
	
	
}
