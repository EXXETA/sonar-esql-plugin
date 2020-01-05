/**
 * This java class is created to implement the logic for checking if variable is initialised or not, 
 * if more than one variable of same datatype is found uninitialised then declare statement could be combined.
 * 
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;



@Rule(key ="DeclareCombine")
public class DeclareCombineCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "If  more than one variable of same datatype is found uninitialised then declare could be combined.";

				 

	@Override
	public void visitStatements(StatementsTree tree) {
		Iterator<Tree> declareIterator = tree.childrenStream()
				.filter(t -> 
					t.is(Kind.DECLARE_STATEMENT) 
					&& ((DeclareStatementTree)t).initialValueExpression()==null
				)
				.iterator();
		Map<String, List<DeclareStatementTree>> declares= new TreeMap<>();
		
		while (declareIterator.hasNext()){
			DeclareStatementTree declareStatement = (DeclareStatementTree) declareIterator.next();
			String dataType = getDataType(declareStatement);
			
			if ( !declares.containsKey(dataType)){
				declares.put(dataType, new ArrayList<>());
			}
			 declares.get(dataType).add(declareStatement);
		}

		for (Map.Entry<String, List<DeclareStatementTree>> entry : declares.entrySet()){
			if (entry.getValue().size()>1){
				addIssue(entry.getValue());
			}
		}
		
		super.visitStatements(tree);
	}

	private String getDataType(DeclareStatementTree declareStatement) {
		String prefix = "";
		if (declareStatement.sharedExt()!=null){
			prefix+=declareStatement.sharedExt().text();
		}
		if (declareStatement.namespace()!=null){
			prefix+=declareStatement.namespace().text();
		}
		if (declareStatement.constantKeyword()!=null){
			prefix+=declareStatement.constantKeyword().text();
		}
		if (declareStatement.dataType()==null) {
			return prefix;
		}
		if (declareStatement.dataType().dataType()!=null){
			return prefix + declareStatement.dataType().dataType().text();
		} else if (declareStatement.dataType().decimalDataType()!=null){
			return prefix + declareStatement.dataType().decimalDataType().childrenStream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(" "));
		} else {
			return prefix;
		}
	}

	private void addIssue(List<DeclareStatementTree> declareStatements) {

	    PreciseIssue issue = addIssue(declareStatements.get(1), MESSAGE);

	    for (int i = 2; i < declareStatements.size(); i++) {
	      issue.secondary(new IssueLocation(declareStatements.get(i)));
	    }
	}			
}


