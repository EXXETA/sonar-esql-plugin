package com.exxeta.iss.sonar.esql.check;

import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="UndocumentedModule")
public class UndocumentedModuleCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		String comment = getComment(tree);
		
		if (comment == null || isEmptyComment(comment)){
			addIssue(tree, "Doument this module");
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
