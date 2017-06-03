package com.exxeta.iss.sonar.esql.tree.symbols;

import java.util.Map;

import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModelBuilder;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BlockTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
/**
 * This visitor creates symbols for:
 *  - explicitly declared symbols (function declaration, local variable with var/let/const)
 *  - built-in symbols (this, arguments)
 *  - parameters
 *  - imported symbols
 */
public class HoistedSymbolVisitor extends DoubleDispatchVisitor {

  private SymbolModelBuilder symbolModel;
  private Scope currentScope;
  private Map<Tree, Scope> treeScopeMap;

  public HoistedSymbolVisitor(Map<Tree, Scope> treeScopeMap) {
    this.treeScopeMap = treeScopeMap;
  }

  @Override
  public void visitProgram(ProgramTree tree) {
    this.symbolModel = (SymbolModelBuilder) getContext().getSymbolModel();

    enterScope(tree);

    super.visitProgram(tree);

    leaveScope();
  }

  @Override
  public void visitBlock(BlockTree tree) {
    if (!treeScopeMap.containsKey(tree)) {
      super.visitBlock(tree);

    } else {
      enterScope(tree);
      super.visitBlock(tree);
      leaveScope();
    }
  }

  @Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
	  symbolModel.declareSymbol(tree.identifier().name(), Symbol.Kind.FUNCTION, getFunctionScope())
      .addUsage(tree.identifier(), Usage.Kind.DECLARATION);

    enterScope(tree);

    declareParameters( tree.parameterList());

    super.visitCreateFunctionStatement(tree);

    leaveScope();
    
	}
  
  @Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
	  symbolModel.declareSymbol(tree.identifier().name(), Symbol.Kind.PROCEDURE, getFunctionScope())
      .addUsage(tree.identifier(), Usage.Kind.DECLARATION);

    enterScope(tree);

    declareParameters( tree.parameterList());

    super.visitCreateProcedureStatement(tree);

    leaveScope();
    
	}
  
  @Override
  public void visitRepeatStatement(RepeatStatementTree tree) {
    enterScope(tree);
    super.visitRepeatStatement(tree);
    leaveScope();
  }
/*
  @Override
  public void visitForObjectStatement(ForObjectStatementTree tree) {
    enterScope(tree);
    insideForLoopVariable = true;
    scan(tree.variableOrExpression());
    insideForLoopVariable = false;
    scan(tree.expression());
    scan(tree.statement());
    leaveScope();
  }

  @Override
  public void visitSwitchStatement(SwitchStatementTree tree) {
    scan(tree.expression());

    enterScope(tree);
    scan(tree.cases());
    leaveScope();
  }

  @Override
  public void visitSpecifier(SpecifierTree tree) {
    if (tree.is(Kind.IMPORT_SPECIFIER, Kind.NAMESPACE_IMPORT_SPECIFIER)) {
      IdentifierTree localName;
      if (tree.localName() != null) {
        localName = tree.localName();

      } else {
        localName = (IdentifierTree) tree.name();
      }

      declareImportedSymbol(localName);
    }
    super.visitSpecifier(tree);
  }

  @Override
  public void visitImportClause(ImportClauseTree tree) {
    if (tree.defaultImport() != null) {
      declareImportedSymbol(tree.defaultImport());
    }
    super.visitImportClause(tree);
  }

  private void declareImportedSymbol(IdentifierTree identifierTree) {
    symbolModel.declareSymbol(identifierTree.name(), Symbol.Kind.IMPORT, symbolModel.globalScope())
      .addUsage(identifierTree, Usage.Kind.DECLARATION);
  }

  private void addExternalSymbols() {
    for (String globalSymbolName : globalVariableNames.names()) {
      symbolModel.declareExternalSymbol(globalSymbolName, Symbol.Kind.VARIABLE, currentScope);
    }

    Symbol windowSymbol = symbolModel.declareExternalSymbol("window", Symbol.Kind.VARIABLE, currentScope);
    windowSymbol.addType(ObjectType.WebApiType.WINDOW);

    addThisSymbol();
  }

  @Override
  public void visitMethodDeclaration(MethodDeclarationTree tree) {
    enterScope(tree);

    declareParameters(((ParameterListTreeImpl) tree.parameterClause()).parameterIdentifiers());
    addFunctionBuiltInSymbols();

    super.visitMethodDeclaration(tree);

    leaveScope();
  }

  @Override
  public void visitAccessorMethodDeclaration(AccessorMethodDeclarationTree tree) {
    enterScope(tree);

    declareParameters(((ParameterListTreeImpl) tree.parameterClause()).parameterIdentifiers());
    addFunctionBuiltInSymbols();

    super.visitAccessorMethodDeclaration(tree);

    leaveScope();
  }

  private void addFunctionBuiltInSymbols() {
    String arguments = "arguments";
    if (currentScope.symbols.get(arguments) == null) {
      symbolModel.declareExternalSymbol(arguments, Symbol.Kind.VARIABLE, currentScope);
    }
  }

  private void addThisSymbol() {
    Symbol thisSymbol = symbolModel.declareExternalSymbol("this", Symbol.Kind.VARIABLE, currentScope);
    thisSymbol.addType(ObjectType.create());
  }

  private void addThisSymbol(ClassTree tree) {
    Symbol thisSymbol = symbolModel.declareExternalSymbol("this", Symbol.Kind.VARIABLE, currentScope);
    thisSymbol.addType(((ClassTreeImpl) tree).classType().createObject());
  }

  @Override
  public void visitCatchBlock(CatchBlockTree tree) {
    enterScope(tree);

    for (IdentifierTree identifier : ((CatchBlockTreeImpl) tree).parameterIdentifiers()) {
      symbolModel.declareSymbol(identifier.name(), Symbol.Kind.VARIABLE, currentScope)
        .addUsage(identifier, Usage.Kind.DECLARATION);
    }

    super.visitCatchBlock(tree);

    leaveScope();
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclarationTree tree) {
    symbolModel.declareSymbol(tree.name().name(), Symbol.Kind.FUNCTION, getFunctionScope())
      .addUsage(tree.name(), Usage.Kind.DECLARATION);

    enterScope(tree);

    declareParameters(((ParameterListTreeImpl) tree.parameterClause()).parameterIdentifiers());
    addFunctionBuiltInSymbols();
    addThisSymbol();

    super.visitFunctionDeclaration(tree);

    leaveScope();
  }

  @Override
  public void visitArrowFunction(ArrowFunctionTree tree) {
    enterScope(tree);

    declareParameters(((ArrowFunctionTreeImpl) tree).parameterIdentifiers());

    super.visitArrowFunction(tree);

    leaveScope();
  }

  @Override
  public void visitFunctionExpression(FunctionExpressionTree tree) {
    enterScope(tree);

    IdentifierTree name = tree.name();
    if (name != null) {
      // Not available in enclosing scope
      symbolModel.declareSymbol(name.name(), Symbol.Kind.FUNCTION, currentScope).addUsage(name, Usage.Kind.DECLARATION);

    }
    declareParameters(((ParameterListTreeImpl) tree.parameterClause()).parameterIdentifiers());
    addFunctionBuiltInSymbols();
    addThisSymbol();

    super.visitFunctionExpression(tree);

    leaveScope();
  }

  @Override
  public void visitClass(ClassTree tree) {
    enterScope(tree);

    addThisSymbol(tree);
    super.visitClass(tree);

    leaveScope();
  }*/
  
  @Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
	    addUsages(tree);
		super.visitDeclareStatement(tree);
	}

  private void addUsages(DeclareStatementTree tree) {
	    Scope scope = currentScope;

	      scope = getFunctionScope();

	    // todo Consider other BindingElementTree types
	    for (IdentifierTree bindingElement : tree.nameList()) {
	      Symbol.Kind variableKind = getVariableKind(tree);

	      if (tree.initialValueExpression()!=null) {
	          symbolModel.declareSymbol(bindingElement.name(), variableKind, scope)
	            .addUsage(bindingElement, Usage.Kind.DECLARATION_WRITE);
	      }else 
	        symbolModel.declareSymbol(bindingElement.name(), variableKind, scope)
	          .addUsage(bindingElement, Usage.Kind.DECLARATION);
	      }
	  }
  
  /*

  @Override
  public void visitVariableDeclaration(VariableDeclarationTree tree) {
    addUsages(tree);
    super.visitVariableDeclaration(tree);
  }

*/

  private void declareParameters(SeparatedList<ParameterTree> identifiers) {
    for (ParameterTree identifier : identifiers) {
      symbolModel.declareSymbol(identifier.identifier().name(), Symbol.Kind.PARAMETER, currentScope)
        .addUsage(identifier.identifier(), Usage.Kind.LEXICAL_DECLARATION);
    }
  }

  private Scope getFunctionScope() {
    Scope scope = currentScope;
    while (scope.isBlock()) {
      scope = scope.outer();
    }
    return scope;
  }

  private static Symbol.Kind getVariableKind(DeclareStatementTree declaration) {
    if (declaration.isExternal()) {
      return Symbol.Kind.EXTERNAL_VARIABLE;

    } else if (declaration.isConstant()) {
      return Symbol.Kind.CONST_VARIABLE;

    } else {
      return Symbol.Kind.VARIABLE;
    }
  }

  private void enterScope(Tree tree) {
    currentScope = treeScopeMap.get(tree);
    if (currentScope == null) {
      throw new IllegalStateException("No scope found for the tree");
    }
  }

  private void leaveScope() {
    if (currentScope != null) {
      currentScope = currentScope.outer();
    }
  }

}
