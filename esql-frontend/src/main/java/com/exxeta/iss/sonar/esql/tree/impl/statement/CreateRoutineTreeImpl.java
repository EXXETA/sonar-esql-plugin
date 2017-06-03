package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.stream.Stream;

import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.symbols.Scope;

public abstract class CreateRoutineTreeImpl extends EsqlTree implements RoutineDeclarationTree{
	
	  private Scope scope;

	  public final Scope scope() {
	    return scope;
	  }

	  public final void scope(Scope scope) {
	    this.scope = scope;
	  }

	  public Stream<Usage> outerScopeSymbolUsages() {
	    return SymbolUsagesVisitor.outerScopeSymbolUsages(this);
	  }

	  private static class SymbolUsagesVisitor extends DoubleDispatchVisitor {

	    private CreateRoutineTreeImpl functionTree;
	    private Stream.Builder<Usage> outerScopeUsages = Stream.builder();

	    private SymbolUsagesVisitor(CreateRoutineTreeImpl scopeTreeImpl) {
	      this.functionTree = scopeTreeImpl;
	    }

	    private static Stream<Usage> outerScopeSymbolUsages(CreateRoutineTreeImpl createRoutineTree) {
	      SymbolUsagesVisitor symbolUsagesVisitor = new SymbolUsagesVisitor(createRoutineTree);
	      symbolUsagesVisitor.scan(createRoutineTree.routineBody());
	      symbolUsagesVisitor.scan(createRoutineTree.parameterList());
	      return symbolUsagesVisitor.outerScopeUsages.build();
	    }

	    @Override
	    public void visitIdentifier(IdentifierTree tree) {
	      tree.symbolUsage().ifPresent(usage -> {
	        Tree symbolScopeTree = usage.symbol().scope().tree();
	        if (symbolScopeTree.isAncestorOf(functionTree)) {
	          outerScopeUsages.add(usage);
	        }
	      });
	    }
	  }
	  
}
