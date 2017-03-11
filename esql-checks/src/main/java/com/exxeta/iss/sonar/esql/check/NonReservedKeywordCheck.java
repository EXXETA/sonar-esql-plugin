package com.exxeta.iss.sonar.esql.check;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.google.common.collect.ImmutableSet;

@Rule(key = NonReservedKeywordCheck.CHECK_KEY)
public class NonReservedKeywordCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "NonReservedKeyword";
	private static final Set<String> nonResewrvedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	public static final String MESSAGE = "ESQL keyword \"%s\" should not be used as an identifier.";
	//@Override
	public void avisitIdentifier(IdentifierTree tree) {
			if (nonResewrvedKeywords.contains(tree.name().toUpperCase())){
				super.addIssue(tree, "ESQL keyword \""+tree.name()+"\" should not be used as an identifier.");
			}
		super.visitIdentifier(tree);
	}

	@Override
	public void visitProgram(ProgramTree tree) {
		 SymbolModel symbolModel = getContext().getSymbolModel();
		    List<Symbol> symbols = new LinkedList<>();
		    for (String name : nonResewrvedKeywords) {
		      symbols.addAll(symbolModel.getSymbols(name));
		    }
		    for (Symbol symbol :symbols) {
		        raiseIssuesOnDeclarations(symbol, MESSAGE);
		      }
	}

	private void raiseIssuesOnDeclarations(Symbol symbol, String message) {
		//TODO Look for declaration? 
		 addIssue(symbol.usages().iterator().next().identifierTree(), message);
		
	}
}
