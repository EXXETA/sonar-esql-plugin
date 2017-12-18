/**
 * This java class is created to implement the logic for checking if variable is initialised or not, 
 * if more than one variable of same datatype is found uninitialised then declare statement could be combined.
 * 
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.HashSet;
import java.util.Set;
import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

@Rule(key = "DeclareCombine")
public class DeclareCombineCheck extends DoubleDispatchVisitorCheck {

	public static final String MESSAGE = "If  more than one variable of same datatype is found uninitialised then declare could be combined.";
	private static Set<String> Uninitialised_Datatype = new HashSet<String>();
	private static Set<String> Uninitialised_Constant = new HashSet<String>();
	private static Set<String> Uninitialised_Shared = new HashSet<String>();
	private static Set<String> Uninitialised_External = new HashSet<String>();
	private static Set<String> Uninitialised_Namespace = new HashSet<String>();
	private static Set<String> Uninitialised_Decimal = new HashSet<String>();
	private static Set<String> Uninitialised_Interval = new HashSet<String>();

	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		
		for (int i = 0; i < tree.nameList().size(); i++) {
			if (tree.dataType() != null) {
				if (tree.isExternal()) {
					if (tree.initialValueExpression() != null) {
						continue;
					} else {
						if (Uninitialised_External.contains(tree.dataType().dataType().text()))
						{
							addIssue(new LineIssue(this, tree, MESSAGE));
						}
						else
						{
							Uninitialised_External.add(tree.dataType().dataType().text().replaceAll("\\s+", ""));
						}
					}
				}

				else if (tree.isConstant())
				{
					
					if (tree.initialValueExpression() != null) {
						continue;
					} 
					else {
						if (Uninitialised_Constant.contains(tree.dataType().dataType().text()))

						{
							addIssue(new LineIssue(this, tree, MESSAGE));
						}

						else 
						{
							Uninitialised_Constant.add(tree.dataType().dataType().text().replaceAll("\\s+", ""));
						}
					}
				}
				
				else if (tree.sharedExt() != null) {
					if (tree.initialValueExpression() != null) {
						continue;
					} else {
						if (Uninitialised_Shared.contains(tree.dataType().dataType().text()))

						{
							addIssue(new LineIssue(this, tree, MESSAGE));
						}

						else 
						{
							Uninitialised_Shared.add(tree.dataType().dataType().text().replaceAll("\\s+", ""));
						}
					}
				}
				
				else if (tree.dataType().decimalDataType() != null) {
					if (tree.initialValueExpression() != null) {
						continue;
					} else {
						if (Uninitialised_Decimal.contains(tree.dataType().decimalDataType().decimalKeyword().text()))

						{
							addIssue(new LineIssue(this, tree, MESSAGE));
						}

						else if (tree.dataType().decimalDataType().decimalKeyword().text() != null) {
							Uninitialised_Decimal.add(tree.dataType().decimalDataType().decimalKeyword().text().replaceAll("\\s+", ""));
						}
					}
				}

				else if (tree.dataType().intervalDataType() != null) {
					if (tree.initialValueExpression() != null)
					{
						continue;
					} 
					else 
					{
						if (Uninitialised_Interval.contains(tree.dataType().intervalDataType().intervalKeyword().text()))
						{
							addIssue(new LineIssue(this, tree, MESSAGE));
						}

						else if (tree.dataType().intervalDataType().intervalKeyword().text() != null) {
							Uninitialised_Interval.add(tree.dataType().intervalDataType().intervalKeyword().text().replaceAll("\\s+", ""));
						}
					}
				} 
				
				
				else if (tree.dataType().dataType()!= null)
				   {
					if (tree.initialValueExpression()!= null) 
					{
						  continue;
					}

					else {
						    if (Uninitialised_Datatype.contains(tree.dataType().dataType().text()))
						   {
							addIssue(new LineIssue(this, tree, MESSAGE));
						   } 
						    else 
						   {
							Uninitialised_Datatype.add(tree.dataType().dataType().text().replaceAll("\\s+", ""));
						   }

					    }	
				   } 
			
				}

			if (tree.namesapce()!=null)	
			{
			if (tree.initialValueExpression()!= null) 
			{
				continue;
			} 
			else {
				
				   if (Uninitialised_Namespace.contains(tree.namesapce().text()))

				    {
					    addIssue(new LineIssue(this, tree, MESSAGE));
				   }

				else 
					if (tree.namesapce().text()!= null)
					{
					      Uninitialised_Namespace.add(tree.namesapce().text().replaceAll("\\s+", ""));
				    }
			
			
			     }
		 
				
			}
		}
	}
}
