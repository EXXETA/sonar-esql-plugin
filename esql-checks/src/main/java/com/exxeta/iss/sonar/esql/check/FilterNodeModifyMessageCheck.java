/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.google.common.collect.ImmutableList;

/**
 * @author C50679
 *
 */
@Rule(key = "FilterNodeModifyMessage")
public class FilterNodeModifyMessageCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "The filter node cannot modify the message";
	private boolean insideFilterModule;

	private List<String> rootElements = ImmutableList.of("Root", "InputRoot", "OutputRoot");

	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		if ("FILTER".equalsIgnoreCase(tree.moduleType().text())){
			this.insideFilterModule=true;
		}
		super.visitCreateModuleStatement(tree);
		this.insideFilterModule=false;
	}
	
	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (this.insideFilterModule && tree.variableReference() instanceof FieldReferenceTree) {
			FieldReferenceTree fieldReference = (FieldReferenceTree) tree.variableReference();
			if (rootElements.contains(fieldReference.pathElement().name().name().text())) {
				addIssue(new LineIssue(this, tree, MESSAGE));
			}

		}

		super.visitSetStatement(tree);
	}
}
