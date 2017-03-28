package com.exxeta.iss.sonar.esql.highlighter;

import java.util.LinkedList;
import java.util.List;

import org.sonar.api.batch.sensor.symbol.NewSymbol;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;


public class HighlightSymbolTableBuilder {

  private HighlightSymbolTableBuilder() {
  }

  public static void build(NewSymbolTable newSymbolTable, TreeVisitorContext context) {
   
    (new BracesVisitor(newSymbolTable)).scanTree(context);
    newSymbolTable.save();
  }

  private static void highlightSymbol(NewSymbolTable newSymbolTable, Symbol symbol) {
    if (!symbol.usages().isEmpty()) {
      List<Usage> usagesList = new LinkedList<>(symbol.usages());
      SyntaxToken token = (usagesList.get(0).identifierTree()).identifierToken();
      NewSymbol newSymbol = getHighlightedSymbol(newSymbolTable, token);
      for (int i = 1; i < usagesList.size(); i++) {
        SyntaxToken referenceToken = getToken(usagesList.get(i).identifierTree());
        addReference(newSymbol, referenceToken);
      }

    }
  }

  private static void addReference(NewSymbol symbol, SyntaxToken referenceToken) {
    symbol.newReference(referenceToken.line(), referenceToken.column(), referenceToken.line(), referenceToken.column() + referenceToken.text().length());
  }

  private static NewSymbol getHighlightedSymbol(NewSymbolTable newSymbolTable, SyntaxToken token) {
    return newSymbolTable.newSymbol(token.line(), token.column(), token.line(), token.column() + token.text().length());
  }

  private static SyntaxToken getToken(IdentifierTree identifierTree) {
    return (identifierTree).identifierToken();
  }

  private static class BracesVisitor extends DoubleDispatchVisitor {

    private final NewSymbolTable newSymbolTable;

    BracesVisitor(NewSymbolTable newSymbolTable) {
      this.newSymbolTable = newSymbolTable;
    }



    private void highlightBraces(SyntaxToken left, SyntaxToken right) {
      NewSymbol symbol = getHighlightedSymbol(newSymbolTable, left);
      addReference(symbol, right);
    }
  }
}
