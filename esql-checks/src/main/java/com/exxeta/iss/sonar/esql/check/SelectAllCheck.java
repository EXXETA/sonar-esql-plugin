package com.exxeta.iss.sonar.esql.check;

import java.util.Iterator;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="SelectAll")
public class SelectAllCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitSelectFunction(SelectFunctionTree tree) {
		for (AliasedExpressionTree aliased : tree.selectClause().aliasedFieldReferenceList()){
			if (aliased.expression()!= null && aliased.expression().is(Kind.FIELD_REFERENCE)){
				FieldReferenceTree fieldReference = (FieldReferenceTree) aliased.expression();
				if ("*".equals(fieldReference.pathElement().name().name().text())){
					addIssue(fieldReference, "Specify the needed fields.");
				}
				Iterator<PathElementTree> iter = fieldReference.pathElements().iterator();
				while  (iter.hasNext()){
					PathElementTree element = iter.next();
					if ("*".equals(element.name().name().text())){
						addIssue(fieldReference, "Specify the needed fields.");
					}	
				}
			}
		}
	}
	
}
