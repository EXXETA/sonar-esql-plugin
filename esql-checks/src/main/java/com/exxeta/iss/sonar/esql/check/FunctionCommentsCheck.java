package com.exxeta.iss.sonar.esql.check;

import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

/**
 * This Java class is created to check function header comments
 * @author 
 *
 */
@Rule(key="FunctionComments")
public class FunctionCommentsCheck extends DoubleDispatchVisitorCheck{


	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		String comment = getComment(tree);
		
		// check comments
		
		if( "FUNCTION".equals(tree.routineType().toString())){
			
			if (comment == null || isEmptyComment(comment)){
				addIssue(tree, "Document this function with all parameters and return types.");
			}else{				
					
				if( !comment.contains("Parameters:") && 
							!comment.contains("IN:") &&
							!comment.contains("INOUT:") &&
							!comment.contains("OUT:") &&
							!comment.contains("RETURNS:")){
						addIssue(tree, "Document this function with all parameters and return types.");
				}				
			}
		}
		
	}	
		
	private static String getComment(Tree tree){
		for (SyntaxTrivia syntaxTrivia : tree.firstToken().trivias()){
			if (syntaxTrivia.text().startsWith("/*")){
				return syntaxTrivia.text();
			}
		}
		
		return null;
	}
	
	
	 private static boolean isEmptyComment(String comment) {
		    //remove start and end of doc as well as stars.
		    String cleanedupJavadoc = comment.trim().substring(2).replace("*/", "").replace("*", "").trim();
		    return StringUtils.isBlank(cleanedupJavadoc);
		  }
	

}
