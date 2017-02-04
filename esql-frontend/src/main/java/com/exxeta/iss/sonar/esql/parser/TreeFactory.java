/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.exxeta.iss.sonar.esql.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.BrokerSchemaStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.EsqlContentsTree;
import com.exxeta.iss.sonar.esql.api.tree.NamespaceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.BrokerSchemaStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DecimalDataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.EsqlContentsTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.IndexTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.IntervalDataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.IntervalQualifierTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.NamespaceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ProgramTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.SchemaNameTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ArrayLiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.BinaryExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.CallExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IntervalExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ParenthesisedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.PrefixExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.TheFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.BeginEndStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ControlsTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateFunctionStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateModuleStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateProcedureStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ElseClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ElseifClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ExternalRoutineBodyTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.IfStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LanguageTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MessageSourceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ParameterTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.PropagateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ResultSetTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ReturnTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RoutineBodyTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.SetStatementTreeImpl;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.typed.Optional;

public class TreeFactory {

	private static final Map<String, Kind> EXPRESSION_KIND_BY_VALUE = new HashMap<>();

	static {
		EXPRESSION_KIND_BY_VALUE.put(EsqlNonReservedKeyword.OR.getValue(), Kind.CONDITIONAL_OR);
		EXPRESSION_KIND_BY_VALUE.put(EsqlNonReservedKeyword.AND.getValue(), Kind.CONDITIONAL_AND);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.EQUAL.getValue(), Kind.EQUAL_TO);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.NOTEQUAL.getValue(), Kind.NOT_EQUAL_TO);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.LT.getValue(), Kind.LESS_THAN);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.GT.getValue(), Kind.GREATER_THAN);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.LE.getValue(), Kind.LESS_THAN_OR_EQUAL_TO);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.GE.getValue(), Kind.GREATER_THAN_OR_EQUAL_TO);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.PLUS.getValue(), Kind.PLUS);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.MINUS.getValue(), Kind.MINUS);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.STAR.getValue(), Kind.MULTIPLY);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.CONCAT.getValue(), Kind.CONCAT);
		EXPRESSION_KIND_BY_VALUE.put(EsqlPunctuator.DIV.getValue(), Kind.DIVIDE);
	}

	private static final Map<String, Kind> PREFIX_KIND_BY_VALUE = ImmutableMap.<String, Tree.Kind>builder()
			// .put(JavaScriptPunctuator.INC.getValue(), Kind.PREFIX_INCREMENT)
			// .put(JavaScriptPunctuator.DEC.getValue(), Kind.PREFIX_DECREMENT)
			// .put(JavaScriptPunctuator.PLUS.getValue(), Kind.UNARY_PLUS)
			// .put(JavaScriptPunctuator.MINUS.getValue(), Kind.UNARY_MINUS)
			// .put(JavaScriptPunctuator.TILDA.getValue(),
			// Kind.BITWISE_COMPLEMENT)
			// .put(JavaScriptPunctuator.BANG.getValue(),
			// Kind.LOGICAL_COMPLEMENT)
			// .put(JavaScriptKeyword.DELETE.getValue(), Kind.DELETE)
			// .put(JavaScriptKeyword.VOID.getValue(), Kind.VOID)
			// .put(JavaScriptKeyword.TYPEOF.getValue(), Kind.TYPEOF)
			// .put(JavaScriptKeyword.AWAIT.getValue(), Kind.AWAIT)
			.build();

	private static ExpressionTree buildBinaryExpression(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		if (!operatorAndOperands.isPresent()) {
			return expression;
		}

		ExpressionTree result = expression;

		for (Tuple<InternalSyntaxToken, ExpressionTree> t : operatorAndOperands.get()) {
			result = new BinaryExpressionTreeImpl(getBinaryOperator(t.first()), result, t.first(), t.second());
		}
		return result;
	}

	public ExpressionTree prefixExpression(InternalSyntaxToken operator, ExpressionTree expression) {
		return new PrefixExpressionTreeImpl(getPrefixOperator(operator), operator, expression);
	}

	public ExpressionTree newRelational(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		return buildBinaryExpression(expression, operatorAndOperands);
	}

	public ExpressionTree newAdditive(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		return buildBinaryExpression(expression, operatorAndOperands);
	}

	public ExpressionTree newMultiplicative(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		return buildBinaryExpression(expression, operatorAndOperands);
	}

	public ExpressionTree newConditionalOr(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		return buildBinaryExpression(expression, operatorAndOperands);
	}

	public ExpressionTree newConditionalAnd(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		return buildBinaryExpression(expression, operatorAndOperands);
	}

	public ExpressionTree newEquality(ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> operatorAndOperands) {
		return buildBinaryExpression(expression, operatorAndOperands);
	}

	private static Kind getBinaryOperator(InternalSyntaxToken token) {
		Kind kind = EXPRESSION_KIND_BY_VALUE.get(token.text());
		if (kind == null) {
			throw new IllegalArgumentException("Mapping not found for binary operator " + token.text());
		}
		return kind;
	}

	private static Kind getPrefixOperator(InternalSyntaxToken token) {
		Kind kind = PREFIX_KIND_BY_VALUE.get(token.text());
		if (kind == null) {
			throw new IllegalArgumentException("Mapping not found for unary operator " + token.text());
		}
		return kind;
	}

	public ElseClauseTreeImpl elseClause(InternalSyntaxToken elseToken, Optional<List<StatementTree>> statements) {
		return new ElseClauseTreeImpl(elseToken, optionalList(statements));
	}

	public ElseifClauseTreeImpl elseifClause(InternalSyntaxToken elseifToken, ExpressionTree expression,
			InternalSyntaxToken thenToken,
			Optional<List<StatementTree>> statements) {
		return new ElseifClauseTreeImpl(elseifToken, expression, thenToken, optionalList(statements));
	}

	public IfStatementTreeImpl ifStatement(InternalSyntaxToken ifToken, ExpressionTree expression,
			InternalSyntaxToken thenToken, Optional<List<StatementTree>> statements,
			Optional<List<ElseifClauseTree>> elseifClauses, Optional<ElseClauseTreeImpl> elseClause,
			InternalSyntaxToken endToken, InternalSyntaxToken ifToken2, InternalSyntaxToken semiToken) {
		return new IfStatementTreeImpl(ifToken, expression, thenToken, optionalList(statements),
				elseifClauses.isPresent() ? elseifClauses.get() : null,
				elseClause.isPresent() ? elseClause.get() : null, endToken, ifToken2, semiToken);
	}

	public DeclareStatementTreeImpl declareStatement(InternalSyntaxToken declareToken) {
		return new DeclareStatementTreeImpl(declareToken, null, null, null, null, null, null, null);
	}

	public static class Tuple<T, U> {

		private final T first;
		private final U second;

		public Tuple(T first, U second) {
			super();

			this.first = first;
			this.second = second;
		}

		public T first() {
			return first;
		}

		public U second() {
			return second;
		}
	}

	public static class Triple<T, U, V> {

		private final T first;
		private final U second;
		private final V third;

		public Triple(T first, U second, V third) {
			super();

			this.first = first;
			this.second = second;
			this.third = third;
		}

		public T first() {
			return first;
		}

		public U second() {
			return second;
		}

		public V third() {
			return third;
		}
	}

	private static <T> List<T> optionalList(Optional<List<T>> list) {
		if (list.isPresent()) {
			return list.get();
		} else {
			return Collections.emptyList();
		}
	}

	private static <T, U> Tuple<T, U> newTuple(T first, U second) {
		return new Tuple<>(first, second);
	}

	private static <T, U, V> Triple<T, U, V> newTriple(T first, U second, V third) {
		return new Triple<>(first, second, third);
	}

	public <T, U> Tuple<T, U> newTuple1(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple2(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple3(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple4(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple5(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple6(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple7(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple8(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple9(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple10(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple11(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple12(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple13(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple14(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple15(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple16(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple17(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple18(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple19(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple20(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple21(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple22(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple23(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple24(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple25(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple26(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple27(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple28(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple29(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple30(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple31(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple32(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple33(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple34(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple35(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple36(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple37(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple38(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple39(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple40(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple41(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple48(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple49(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple50(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple51(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple52(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple53(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple54(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple55(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple56(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple57(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple58(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple59(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U, V> Triple<T, U, V> newTriple1(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public <T, U, V> Triple<T, U, V> newTriple2(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public <T, U, V> Triple<T, U, V> newTriple3(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public ProgramTreeImpl program(Optional<BrokerSchemaStatementTree> brokerSchema,
			Optional<PathClauseTree> pathClause, Optional<InternalSyntaxToken> semi, EsqlContentsTree esqlContents,
			Tree spacing, InternalSyntaxToken eof) {
		return new ProgramTreeImpl(brokerSchema.isPresent() ? brokerSchema.get() : null,
				pathClause.isPresent() ? pathClause.get() : null, semi.isPresent() ? semi.get() : null, esqlContents,
				eof);
	}

	public PathClauseTreeImpl pathClause(InternalSyntaxToken pathToken, SchemaNameTree schemaNameTree,
			Optional<List<Tuple<InternalSyntaxToken, SchemaNameTree>>> optional) {
		return new PathClauseTreeImpl(pathToken, schemaNameList(schemaNameTree, optional));
	}

	private static SeparatedList<SchemaNameTree> schemaNameList(SchemaNameTree schemaNameTree,
			Optional<List<Tuple<InternalSyntaxToken, SchemaNameTree>>> optional) {

		ImmutableList.Builder<SchemaNameTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(schemaNameTree);

		if (optional.isPresent()) {
			for (Tuple<InternalSyntaxToken, SchemaNameTree> pair : optional.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
	}

	private static SeparatedList<ParameterTree> parameterList(ParameterTree parameterTree,
			Optional<List<Tuple<InternalSyntaxToken, ParameterTree>>> optional) {

		ImmutableList.Builder<ParameterTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(parameterTree);

		if (optional.isPresent()) {
			for (Tuple<InternalSyntaxToken, ParameterTree> pair : optional.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
	}

	private static SeparatedList<ExpressionTree> expressionList(ExpressionTree expressionTree,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> optional) {

		ImmutableList.Builder<ExpressionTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(expressionTree);

		if (optional.isPresent()) {
			for (Tuple<InternalSyntaxToken, ExpressionTree> pair : optional.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
	}

	private static SeparatedList<PathElementTree> pathElementList(
			Optional<List<Tuple<InternalSyntaxToken, PathElementTreeImpl>>> pathElements) {
		ImmutableList.Builder<PathElementTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> dots = ImmutableList.builder();

		// elements.add(parameterTree);

		if (pathElements.isPresent()) {
			for (Tuple<InternalSyntaxToken, PathElementTreeImpl> pair : pathElements.get()) {
				InternalSyntaxToken dotToken = pair.first();

				dots.add(dotToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), dots.build(), false);
	}


	public SchemaNameTreeImpl schemaName(InternalSyntaxToken first,
			Optional<List<Tuple<InternalSyntaxToken, InternalSyntaxToken>>> tuple) {
		return new SchemaNameTreeImpl(schemaNameList2(first, tuple));
	}

	private static SeparatedList<InternalSyntaxToken> schemaNameList2(InternalSyntaxToken element,
			Optional<List<Tuple<InternalSyntaxToken, InternalSyntaxToken>>> rest) {

		ImmutableList.Builder<InternalSyntaxToken> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(element);

		if (rest.isPresent()) {
			for (Tuple<InternalSyntaxToken, InternalSyntaxToken> pair : rest.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
	}

	public BrokerSchemaStatementTreeImpl brokerSchema(InternalSyntaxToken brokerToken, InternalSyntaxToken schemaToken,
			SchemaNameTree schemaNameTree) {
		return new BrokerSchemaStatementTreeImpl(brokerToken, schemaToken, schemaNameTree);
	}

	public TheFunctionTree theFunction(InternalSyntaxToken theKeyword, InternalSyntaxToken openingParenthesis,
			InternalSyntaxToken expression, InternalSyntaxToken closingParenthesis) {
		return new TheFunctionTreeImpl(theKeyword, openingParenthesis, expression, closingParenthesis);
	}

	public EsqlContentsTreeImpl esqlContents(Optional<List<StatementTree>> zeroOrMore) {
		return new EsqlContentsTreeImpl(optionalList(zeroOrMore));

	}

	public CreateFunctionStatementTreeImpl createFunctionStatement(InternalSyntaxToken createKeyword,
			InternalSyntaxToken functionKeyword, InternalSyntaxToken identifier, InternalSyntaxToken openingParenthesis,
			Optional<ParameterTree> parameter, Optional<List<Tuple<InternalSyntaxToken, ParameterTree>>> restParameter,
			InternalSyntaxToken closingParenthesis, Optional<ReturnTypeTreeImpl> returnType,
			Optional<LanguageTreeImpl> language, Optional<ResultSetTreeImpl> resultSet,
			RoutineBodyTreeImpl routineBody) {
		if (parameter.isPresent()) {
			return new CreateFunctionStatementTreeImpl(createKeyword, functionKeyword, identifier, openingParenthesis,
					parameterList(parameter.get(), restParameter),
					closingParenthesis,
					returnType.isPresent() ? returnType.get() : null, language.isPresent() ? language.get() : null,
					resultSet.isPresent() ? resultSet.get() : null, routineBody);
		} else {
			return new CreateFunctionStatementTreeImpl(createKeyword, functionKeyword, identifier, openingParenthesis,
					new SeparatedList<>(Collections.<ParameterTree>emptyList(), Collections.<InternalSyntaxToken>emptyList()), closingParenthesis, returnType.isPresent() ? returnType.get() : null,
					language.isPresent() ? language.get() : null, resultSet.isPresent() ? resultSet.get() : null,
					routineBody);
		}
	}

	public CreateProcedureStatementTreeImpl createProcedureStatement(InternalSyntaxToken createKeyword,
			InternalSyntaxToken procedureKeyword, InternalSyntaxToken identifier,
			InternalSyntaxToken openingParenthesis, Optional<ParameterTree> parameter,
			Optional<List<Tuple<InternalSyntaxToken, ParameterTree>>> restParameter,
			InternalSyntaxToken closingParenthesis, Optional<ReturnTypeTreeImpl> returnType,
			Optional<LanguageTreeImpl> language, Optional<ResultSetTreeImpl> resultSet,
			RoutineBodyTreeImpl routineBody) {
		if (parameter.isPresent()) {
			return new CreateProcedureStatementTreeImpl(createKeyword, procedureKeyword, identifier, openingParenthesis,
					parameterList(parameter.get(), restParameter), closingParenthesis,
					returnType.isPresent() ? returnType.get() : null, language.isPresent() ? language.get() : null,
					resultSet.isPresent() ? resultSet.get() : null, routineBody);
		} else {
			return new CreateProcedureStatementTreeImpl(createKeyword, procedureKeyword, identifier, openingParenthesis,
					null, closingParenthesis, returnType.isPresent() ? returnType.get() : null,
					language.isPresent() ? language.get() : null, resultSet.isPresent() ? resultSet.get() : null,
					routineBody);
		}
	}

	public ExternalRoutineBodyTreeImpl externalRoutineBody(InternalSyntaxToken externalKeyword,
			InternalSyntaxToken nameKeyword, InternalSyntaxToken expression) {
		return new ExternalRoutineBodyTreeImpl(externalKeyword, nameKeyword, expression);
	}

	public RoutineBodyTreeImpl routineBody(Tree firstOf) {
		if (firstOf instanceof StatementTree) {
			return new RoutineBodyTreeImpl((StatementTree) firstOf);
		} else {
			return new RoutineBodyTreeImpl((ExternalRoutineBodyTreeImpl) firstOf);
		}
	}

	public ParameterTreeImpl parameter(Optional<InternalSyntaxToken> directionIndicator, InternalSyntaxToken expression,
			Optional<Object> optional) {
		if (optional.isPresent()) {
			if (optional.get() instanceof Tuple) {
				Tuple<Optional<InternalSyntaxToken>, InternalSyntaxToken> t = (Tuple) optional.get();
				return new ParameterTreeImpl(directionIndicator.isPresent() ? directionIndicator.get() : null,
						expression, t.first().isPresent() ? t.first().get() : null, t.second());
			} else {
				return new ParameterTreeImpl(directionIndicator.isPresent() ? directionIndicator.get() : null,
						expression, (InternalSyntaxToken) optional.get());
			}

		} else {
			return new ParameterTreeImpl(directionIndicator.isPresent() ? directionIndicator.get() : null, expression);

		}
	}

	public ReturnTypeTreeImpl returnType(InternalSyntaxToken returnsToken, DataTypeTreeImpl dataType,
			Optional<Object> nullIndicator) {
		if (!nullIndicator.isPresent()) {
			return new ReturnTypeTreeImpl(returnsToken, dataType);
		} else if (nullIndicator.get() instanceof Tuple) {
			Tuple<InternalSyntaxToken, InternalSyntaxToken> t = (Tuple) nullIndicator.get();
			return new ReturnTypeTreeImpl(returnsToken, dataType, t.first(), t.second());
		} else {
			return new ReturnTypeTreeImpl(returnsToken, dataType, (InternalSyntaxToken) nullIndicator.get());
		}
	}

	public LanguageTreeImpl language(InternalSyntaxToken languageKeyword, InternalSyntaxToken languageName) {
		return new LanguageTreeImpl(languageKeyword, languageName);
	}

	public CreateModuleStatementTreeImpl createModuleStatement(InternalSyntaxToken createKeyword,
			InternalSyntaxToken moduleType, InternalSyntaxToken moduleKeyword, InternalSyntaxToken moduleName,
			Optional<List<StatementTree>> optional, InternalSyntaxToken endKeyword,
			InternalSyntaxToken moduleKeyword2, InternalSyntaxToken semi) {
		List<StatementTree> moduleStatementsList = optionalList(optional);
		return new CreateModuleStatementTreeImpl(createKeyword, moduleType, moduleKeyword, moduleName,
				moduleStatementsList, endKeyword, moduleKeyword2);
	}

	public ResultSetTreeImpl resultSet(InternalSyntaxToken dynamicKeyword, InternalSyntaxToken resultKeyword,
			InternalSyntaxToken setsKeyword, InternalSyntaxToken integer) {

		return new ResultSetTreeImpl(dynamicKeyword, resultKeyword, setsKeyword, integer);
	}

	public PropagateStatementTreeImpl propagateStatement(InternalSyntaxToken propagateKeyword,
			Optional<Tuple<InternalSyntaxToken, Tuple<InternalSyntaxToken, InternalSyntaxToken>>> target,
			Optional<MessageSourceTreeImpl> messageSource, Optional<ControlsTreeImpl> controls) {

		if (target.isPresent()) {
			return new PropagateStatementTreeImpl(propagateKeyword, target.get().first(), target.get().second().first(),
					target.get().second().second(), messageSource.isPresent() ? messageSource.get() : null,
					controls.isPresent() ? controls.get() : null);
		} else {
			return new PropagateStatementTreeImpl(propagateKeyword,
					messageSource.isPresent() ? messageSource.get() : null,
					controls.isPresent() ? controls.get() : null);
		}

	}

	public MessageSourceTreeImpl messageSource(Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> environment,
			Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> message,
			Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> exception) {
		return new MessageSourceTreeImpl(environment.isPresent() ? environment.get().first() : null,
				environment.isPresent() ? environment.get().second() : null,
				message.isPresent() ? message.get().first() : null, message.isPresent() ? message.get().second() : null,
				exception.isPresent() ? exception.get().first() : null,
				exception.isPresent() ? exception.get().second() : null);
	}

	public ControlsTreeImpl controls(Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> finalize,
			Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> delete) {
		return new ControlsTreeImpl(finalize.isPresent() ? finalize.get().first() : null,
				finalize.isPresent() ? finalize.get().second() : null, delete.isPresent() ? delete.get().first() : null,
				delete.isPresent() ? delete.get().second() : null);
	}

	public BeginEndStatementTreeImpl beginEndStatement(Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> label1,
			InternalSyntaxToken beginKeyword,
			Optional<Tuple<Optional<InternalSyntaxToken>, InternalSyntaxToken>> atomic,
			Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword,
			Optional<InternalSyntaxToken> label2, InternalSyntaxToken semiToken) {
		if (label1.isPresent()) {
			return new BeginEndStatementTreeImpl(label1.get().first(), label1.get().second(), beginKeyword,
					atomic.isPresent() && atomic.get().first().isPresent() ? atomic.get().first().get() : null,
					atomic.isPresent() ? atomic.get().second() : null, optionalList(statements), endKeyword,
					label2.isPresent() ? label2.get() : null, semiToken);
		} else {
			return new BeginEndStatementTreeImpl(beginKeyword,
					atomic.isPresent() && atomic.get().first().isPresent() ? atomic.get().first().get() : null,
					atomic.isPresent() ? atomic.get().second() : null, optionalList(statements), endKeyword, semiToken);
		}
	}

	public ExpressionTree expression(ExpressionTree logical_OR_EXPRESSION) {
		return logical_OR_EXPRESSION;
	}

	public SeparatedList<Tree> argumentList(ExpressionTree argument,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> restArguments) {
		List<Tree> arguments = Lists.newArrayList();
		List<InternalSyntaxToken> commas = Lists.newArrayList();

		arguments.add(argument);

		if (restArguments.isPresent()) {
			for (Tuple<InternalSyntaxToken, ExpressionTree> t : restArguments.get()) {
				commas.add(t.first());
				arguments.add(t.second());
			}
		}

		return new SeparatedList<>(arguments, commas);
	}

	public ParameterListTreeImpl argumentClause(InternalSyntaxToken openParenToken,
			Optional<SeparatedList<Tree>> arguments, InternalSyntaxToken closeParenToken) {
		return new ParameterListTreeImpl(openParenToken, arguments.isPresent() ? arguments.get()
				: new SeparatedList<>(Collections.<Tree>emptyList(), Collections.<InternalSyntaxToken>emptyList()),
				closeParenToken);
	}

	public CallExpressionTreeImpl callExpression(Object firstOf) {
		if (firstOf instanceof FunctionTree) {
			return new CallExpressionTreeImpl((FunctionTree) firstOf);
		} else if (firstOf instanceof Tuple) {
			Tuple<FieldReferenceTreeImpl, ParameterListTreeImpl> t = (Tuple) firstOf;
			return new CallExpressionTreeImpl(t.first(), t.second());
		} else {
			return new CallExpressionTreeImpl((FieldReferenceTreeImpl) firstOf);
		}
	}

	public LiteralTreeImpl listLiteral(InternalSyntaxToken listToken) {
		return new LiteralTreeImpl(Kind.LIST_LITERAL, listToken);
	}

	public LiteralTreeImpl timeLiteral(InternalSyntaxToken timeToken) {
		return new LiteralTreeImpl(Kind.TIME_LITERAL, timeToken);
	}

	public LiteralTreeImpl dateLiteral(InternalSyntaxToken dateToken) {
		return new LiteralTreeImpl(Kind.DATE_LITERAL, dateToken);
	}

	public LiteralTreeImpl arrayLiteral(InternalSyntaxToken arrayToken) {
		return new LiteralTreeImpl(Kind.ARRAY_LITERAL, arrayToken);
	}

	public LiteralTreeImpl intervalLiteral(InternalSyntaxToken intervalToken) {
		return new LiteralTreeImpl(Kind.INTERVAL_LITERAL, intervalToken);
	}

	public LiteralTreeImpl nullLiteral(InternalSyntaxToken nullToken) {
		return new LiteralTreeImpl(Kind.NULL_LITERAL, nullToken);
	}

	public LiteralTreeImpl booleanLiteral(InternalSyntaxToken trueFalseToken) {
		return new LiteralTreeImpl(Kind.BOOLEAN_LITERAL, trueFalseToken);
	}

	public LiteralTreeImpl hexLiteral(InternalSyntaxToken hexToken) {
		return new LiteralTreeImpl(Kind.HEX_LITERAL, hexToken);
	}

	public LiteralTreeImpl numericLiteral(InternalSyntaxToken hexToken) {
		return new LiteralTreeImpl(Kind.NUMERIC_LITERAL, hexToken);
	}

	public LiteralTreeImpl stringLiteral(InternalSyntaxToken hexToken) {
		return new LiteralTreeImpl(Kind.STRING_LITERAL, hexToken);
	}

	public ArrayLiteralTreeImpl newArrayLiteralWithElements(Optional<List<InternalSyntaxToken>> commaTokens,
			ExpressionTree element, Optional<List<Tuple<List<InternalSyntaxToken>, ExpressionTree>>> restElements) {
		List<Tree> elementsAndCommas = Lists.newArrayList();

		// Elided array element at the beginning, e.g [ ,a]
		if (commaTokens.isPresent()) {
			elementsAndCommas.addAll(commaTokens.get());
		}

		// First element
		elementsAndCommas.add(element);

		// Other elements
		if (restElements.isPresent()) {
			for (Tuple<List<InternalSyntaxToken>, ExpressionTree> t : restElements.get()) {
				elementsAndCommas.addAll(t.first());
				elementsAndCommas.add(t.second());
			}
		}

		return new ArrayLiteralTreeImpl(elementsAndCommas);
	}

	public ArrayLiteralTreeImpl completeArrayLiteral(InternalSyntaxToken openBracketToken,
			Optional<ArrayLiteralTreeImpl> elements, InternalSyntaxToken closeBracket) {
		if (elements.isPresent()) {
			return elements.get().complete(openBracketToken, closeBracket);
		}
		return new ArrayLiteralTreeImpl(openBracketToken, closeBracket);
	}

	public ArrayLiteralTreeImpl newArrayLiteralWithElidedElements(List<InternalSyntaxToken> commaTokens) {
		return new ArrayLiteralTreeImpl(new ArrayList<Tree>(commaTokens));
	}

	public ParenthesisedExpressionTreeImpl parenthesisedExpression(InternalSyntaxToken openParenToken,
			ExpressionTree expression, InternalSyntaxToken closeParenToken) {
		return new ParenthesisedExpressionTreeImpl(openParenToken, expression, closeParenToken);
	}

	public IntervalExpressionTreeImpl intervalExpression(InternalSyntaxToken openParenToken,
			ExpressionTree additiveExpression, InternalSyntaxToken closeParenToken,
			IntervalQualifierTreeImpl intervalQualifier) {
		return new IntervalExpressionTreeImpl(openParenToken, additiveExpression, closeParenToken, intervalQualifier);
	}

	public FieldReferenceTreeImpl fieldReference(Object first,
			Optional<List<Tuple<InternalSyntaxToken, PathElementTreeImpl>>> zeroOrMore) {
		if (first instanceof ExpressionTree){
			return new FieldReferenceTreeImpl((ExpressionTree)first, pathElementList(zeroOrMore));
		} else{
			return new FieldReferenceTreeImpl((InternalSyntaxToken)first, pathElementList(zeroOrMore));
		}
	}

	public NamespaceTreeImpl namespace(InternalSyntaxToken identifier) {
		return new NamespaceTreeImpl(identifier);
	}

	public IndexTreeImpl index(InternalSyntaxToken openBracket,
			Tuple<Optional<InternalSyntaxToken>, Optional<ExpressionTree>> content, InternalSyntaxToken closeBracket) {

		return new IndexTreeImpl(openBracket, content.first().isPresent() ? content.first().get() : null,
				content.second().isPresent() ? content.second().get() : null, closeBracket);
	}

	public PathElementTreeImpl pathElement(
			Optional<Triple<InternalSyntaxToken, Tuple<ExpressionTree, Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>>>, InternalSyntaxToken>> type,
			Optional<Tuple<Optional<Object>, InternalSyntaxToken>> namespace, Object name,
			Optional<IndexTreeImpl> index) {

		PathElementTreeImpl pathElement = new PathElementTreeImpl();

		if (type.isPresent()) {
			pathElement.setType(type.get().first(),
					expressionList(type.get().second().first(), type.get().second().second()), type.get().third());
		}

		if (namespace.isPresent()) {
			Optional<Object> first = namespace.get().first();
			if (first.isPresent()) {
				if (first.get() instanceof Triple) {
					Triple<InternalSyntaxToken, ExpressionTree, InternalSyntaxToken> t = (Triple) first.get();
					pathElement.namespace(t.first(), t.second(), t.third(), namespace.get().second());
				} else if (first.get() instanceof NamespaceTree) {
					pathElement.namespace((NamespaceTree) first.get(), namespace.get().second());
				} else {
					pathElement.namesapce((InternalSyntaxToken) first.get(), namespace.get().second());
				}
			} else {
				pathElement.namespace(namespace.get().second());
			}
		}
		if (name instanceof Triple) {
			Triple<InternalSyntaxToken, ExpressionTree, InternalSyntaxToken> triple = (Triple) name;
			pathElement.name(triple.first(), triple.second(), triple.third());
		} else {
			pathElement.name((InternalSyntaxToken) name);
		}
		if (index.isPresent()) {
			pathElement.index(index.get());
		}

		return pathElement;
	}

	public DecimalDataTypeTreeImpl decimalSize(InternalSyntaxToken openParen, InternalSyntaxToken precision,
			InternalSyntaxToken comma, InternalSyntaxToken scale, InternalSyntaxToken closeParen) {
		return new DecimalDataTypeTreeImpl(openParen, precision, comma, scale, closeParen);
	}

	public DecimalDataTypeTreeImpl decimalDataType(InternalSyntaxToken decimalKeyword,
			Optional<DecimalDataTypeTreeImpl> size) {
		if (size.isPresent()) {
			return size.get().complete(decimalKeyword);
		} else {
			return new DecimalDataTypeTreeImpl(decimalKeyword);
		}
	}

	public IntervalDataTypeTreeImpl intervalDataType(InternalSyntaxToken intervalKeyword,
			Optional<IntervalQualifierTreeImpl> qualifier) {
		if (qualifier.isPresent()) {
			return new IntervalDataTypeTreeImpl(intervalKeyword, qualifier.get());
		} else {
			return new IntervalDataTypeTreeImpl(intervalKeyword);
		}
	}

	public IntervalQualifierTreeImpl intervalQualifier(Object firstOf) {
		if (firstOf instanceof Tuple) {
			Tuple<InternalSyntaxToken, Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>>> t = (Tuple) firstOf;
			InternalSyntaxToken from = t.first();
			if (t.second().isPresent()) {
				InternalSyntaxToken toKeyword = t.second().get().first();
				InternalSyntaxToken to = t.second().get().second();
				return new IntervalQualifierTreeImpl(from, toKeyword, to);
			} else {
				return new IntervalQualifierTreeImpl(from);
			}

		} else {
			return new IntervalQualifierTreeImpl((InternalSyntaxToken) firstOf);
		}
	}

	public DataTypeTreeImpl dataType(Object firstOf) {

		if (firstOf instanceof IntervalDataTypeTreeImpl) {
			return new DataTypeTreeImpl((IntervalDataTypeTreeImpl)firstOf);
		} else if (firstOf instanceof DecimalDataTypeTreeImpl) {
			return new DataTypeTreeImpl((DecimalDataTypeTreeImpl)firstOf);
		} else if (firstOf instanceof Tuple) {
			Tuple<InternalSyntaxToken, Optional<InternalSyntaxToken>> t = (Tuple)firstOf;
			if (t.second().isPresent()){
				return new DataTypeTreeImpl(t.first(), t.second().get());	
			}else {
				return new DataTypeTreeImpl(t.first());	
			}
		} else {
			return new DataTypeTreeImpl((InternalSyntaxToken)firstOf);
		}

	}

	public SetStatementTreeImpl setStatement(InternalSyntaxToken setKeyword, FieldReferenceTreeImpl fieldReference,
			Optional<InternalSyntaxToken> type, InternalSyntaxToken equal, ExpressionTree expression, InternalSyntaxToken semiToken) {
		return new SetStatementTreeImpl(setKeyword, fieldReference, type.isPresent()?type.get():null, equal, expression, semiToken);
	}

}
