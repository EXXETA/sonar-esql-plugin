/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.symbols.type;

import java.util.List;

import javax.annotation.Nullable;

import org.sonar.api.config.Configuration;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.google.common.base.Preconditions;

public class TypeVisitor extends DoubleDispatchVisitor {

	public TypeVisitor( @Nullable Configuration configuration) {

	}

	@Override
	public void visitLiteral(LiteralTree tree) {
		if (tree.is(Tree.Kind.NUMERIC_LITERAL)) {
			addType(tree, PrimitiveType.INTEGER);

		} else if (tree.is(Tree.Kind.STRING_LITERAL)) {
			addType(tree, PrimitiveType.CHARACTER);

		} else if (tree.is(Tree.Kind.BOOLEAN_LITERAL)) {
			addType(tree, PrimitiveType.BOOLEAN);
		}
	}

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		IdentifierTree name = tree.identifier();
		String nameName = name.name();

		Preconditions.checkState(name.symbol().isPresent(),
				"Symbol has not been created for this function %s declared at line %s", nameName,
				tree.firstToken().line());

		name.symbol().get().addType(FunctionType.create(tree));
		setParameterTypes(tree.parameterList());
		super.visitCreateFunctionStatement(tree);
	}

	private void setParameterTypes(List<ParameterTree> parameterList) {
		for (ParameterTree parameter : parameterList) {
			setParameterType(parameter);
		}
	}

	private void setParameterType(ParameterTree tree) {
		IdentifierTree name = tree.identifier();
		String nameName = name.name();

		Preconditions.checkState(name.symbol().isPresent(),
				"Symbol has not been created for this parameter %s declared at line %s", nameName,
				tree.firstToken().line());

		addTypes(name.symbol().get(), getTypeForDataType(tree.dataType()));

	}

	private TypeSet getTypeForDataType(DataTypeTreeImpl dataType) {
		TypeSet result = new TypeSet();
		if (dataType!=null && dataType.dataType() != null) {
			if ("CHAR".equalsIgnoreCase(dataType.dataType().text())
					|| "CHARACTER".equalsIgnoreCase(dataType.dataType().text())) {
				result.add(PrimitiveType.CHARACTER);
			} else if ("INTEGER".equalsIgnoreCase(dataType.dataType().text())
					|| "INT".equalsIgnoreCase(dataType.dataType().text())) {
				result.add(PrimitiveType.INTEGER);
			} else if ("BOOLEAN".equalsIgnoreCase(dataType.dataType().text())) {
				result.add(PrimitiveType.BOOLEAN);
			}
		}
		return result;
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		IdentifierTree name = tree.identifier();
		String nameName = name.name();

		Preconditions.checkState(name.symbol().isPresent(),
				"Symbol has not been created for this procedure %s declared at line %s", nameName,
				tree.firstToken().line());

		name.symbol().get().addType(ProcedureType.create(tree));
		setParameterTypes(tree.parameterList());
		super.visitCreateProcedureStatement(tree);
	}

	private static void addType(ExpressionTree tree, Type type) {
		((TypableTree) tree).add(type);
	}

	@Override
	public void visitParenthesisedExpression(ParenthesisedExpressionTree tree) {
		super.visitParenthesisedExpression(tree);
		addTypes(tree, tree.expression().types());
	}

	@Override
	public void visitBinaryExpression(BinaryExpressionTree tree) {
		super.visitBinaryExpression(tree);

		Type resultType = PrimitiveOperations.getType(tree.leftOperand(), tree.rightOperand(),
				((EsqlTree) tree).getKind());

		if (resultType != null) {
			addType(tree, resultType);
		}
	}

	@Override
	public void visitUnaryExpression(UnaryExpressionTree tree) {
		super.visitUnaryExpression(tree);

		Type resultType = PrimitiveOperations.getType(tree);
		if (resultType != null) {
			addType(tree, resultType);
		}
	}

	private static void addTypes(Symbol symbol, TypeSet types) {
		if (types.isEmpty()) {
			symbol.addType(PrimitiveType.UNKNOWN);
		} else {
			symbol.addTypes(types);
		}
	}

	private static void addTypes(ExpressionTree tree, TypeSet types) {
		for (Type type : types) {
			addType(tree, type);
		}
	}

}
