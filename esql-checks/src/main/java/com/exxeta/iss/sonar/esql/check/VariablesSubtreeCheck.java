package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="VariablesSubtree")
public class VariablesSubtreeCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (tree.variableReference() instanceof FieldReferenceTree){
			FieldReferenceTree fieldRef = (FieldReferenceTree)tree.variableReference();
			if ("Environment".equalsIgnoreCase(fieldRef.pathElement().name().name().text()) ){
				
				if (fieldRef.pathElements().isEmpty()
					|| fieldRef.pathElements().get(0).name()==null 
					|| fieldRef.pathElements().get(0).name().name()==null 
					|| !"Variables".equalsIgnoreCase(fieldRef.pathElements().get(0).name().name().text())){

					addIssue(tree, "Environment vaiables should be written to the Variables-subtree.");
					
				}
				
			}
		}
	}
	
}
