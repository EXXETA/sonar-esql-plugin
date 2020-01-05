/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.tree.symbols;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModelBuilder;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

import java.util.Map;

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
    public void visitBeginEndStatement(BeginEndStatementTree tree) {
        if (!treeScopeMap.containsKey(tree)) {
            super.visitBeginEndStatement(tree);

        } else {
            enterScope(tree);
            super.visitBeginEndStatement(tree);
            leaveScope();
        }

    }

    @Override
    public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
        symbolModel.declareSymbol(tree.identifier().name(), Symbol.Kind.FUNCTION, getFunctionScope())
                .addUsage(tree.identifier(), Usage.Kind.DECLARATION);

        enterScope(tree);

        declareParameters(tree.parameterList());

        super.visitCreateFunctionStatement(tree);

        leaveScope();

    }

    @Override
    public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
        symbolModel.declareSymbol(tree.identifier().name(), Symbol.Kind.PROCEDURE, getFunctionScope())
                .addUsage(tree.identifier(), Usage.Kind.DECLARATION);

        enterScope(tree);

        declareParameters(tree.parameterList());

        super.visitCreateProcedureStatement(tree);

        leaveScope();

    }


    @Override
    public void visitRepeatStatement(RepeatStatementTree tree) {
        scan(tree.condition());
        enterScope(tree);
        super.visitRepeatStatement(tree);
        leaveScope();
    }

    @Override
    public void visitLoopStatement(LoopStatementTree tree) {
        enterScope(tree);
        super.visitLoopStatement(tree);
        leaveScope();
    }

    @Override
    public void visitCaseStatement(CaseStatementTree tree) {
        scan(tree.mainExpression());
        enterScope(tree);
        super.visitCaseStatement(tree);
        leaveScope();
    }

    @Override
    public void visitDeclareStatement(DeclareStatementTree tree) {
        addUsages(tree);
        super.visitDeclareStatement(tree);
    }

    private void addUsages(DeclareStatementTree tree) {
        Scope scope = getFunctionScope();

        for (IdentifierTree bindingElement : tree.nameList()) {
            Symbol.Kind variableKind = getVariableKind(tree);

            if (tree.initialValueExpression() != null) {
                symbolModel.declareSymbol(bindingElement.name(), variableKind, scope).addUsage(bindingElement,
                        Usage.Kind.DECLARATION_WRITE);
            } else {
                symbolModel.declareSymbol(bindingElement.name(), variableKind, scope).addUsage(bindingElement,
                        Usage.Kind.DECLARATION);
            }
        }
    }

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
