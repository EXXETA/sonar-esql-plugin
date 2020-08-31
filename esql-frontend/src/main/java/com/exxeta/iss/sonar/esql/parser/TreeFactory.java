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
package com.exxeta.iss.sonar.esql.parser;

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
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.VariableReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.JavaClassloaderServiceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.NameClausesTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.tree.expression.LikeExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
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
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementNameTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementNamespaceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ProgramTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.SchemaNameTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.BetweenExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.BinaryExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.CallExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IdentifierTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.InExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IntervalExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IsExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ParenthesisedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.PrefixExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AliasedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AliasedFieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AsbitstreamFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.CaseFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.CastFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.ExtractFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.ForFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.FromClauseExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.ListConstructorFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.OverlayFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.PassthruFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.PositionFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.RoundFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.RowConstructorFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.SelectClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.SelectFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.SubstringFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.TheFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.TrimFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.WhenClauseExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.WhereClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.AttachStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.BeginEndStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CallStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CaseStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ControlsTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateFunctionStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateModuleStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateProcedureStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareHandlerStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeleteFromStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeleteStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DetachStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ElseClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ElseifClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.EvalStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ExternalRoutineBodyTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ForStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.FromClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.IfStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.InsertStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.IterateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.JavaClassloaderServiceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LabelTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LanguageTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LeaveStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LogStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LoopStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MessageSourceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MoveStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.NameClausesTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.NullableTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ParameterTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ParseClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.PassthruStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.PropagateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RepeatClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RepeatStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ResignalStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ResultSetTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ReturnStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ReturnTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RoutineBodyTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.SetColumnTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.SetStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.SqlStateTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.StatementsTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ThrowStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.UpdateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ValuesClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.WhenClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.WhileStatementTreeImpl;
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
			.put(EsqlNonReservedKeyword.NOT.getValue(), Kind.LOGICAL_COMPLEMENT)
			.put(EsqlPunctuator.PLUS.getValue(), Kind.UNARY_PLUS)
		    .put(EsqlPunctuator.MINUS.getValue(), Kind.UNARY_MINUS)
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
		Kind kind = EXPRESSION_KIND_BY_VALUE.get(token.text().toUpperCase());
		if (kind == null) {
			throw new IllegalArgumentException("Mapping not found for binary operator " + token.text());
		}
		return kind;
	}

	private static Kind getPrefixOperator(InternalSyntaxToken token) {
		Kind kind = PREFIX_KIND_BY_VALUE.get(token.text().toUpperCase());
		if (kind == null) {
			throw new IllegalArgumentException("Mapping not found for unary operator " + token.text().toUpperCase());
		}
		return kind;
	}

	public ElseClauseTreeImpl elseClause(InternalSyntaxToken elseToken,StatementsTreeImpl statements) {
		return new ElseClauseTreeImpl(elseToken, statements);
	}

	public ElseifClauseTreeImpl elseifClause(InternalSyntaxToken elseifToken, ExpressionTree expression,
			InternalSyntaxToken thenToken, StatementsTreeImpl statements) {
		return new ElseifClauseTreeImpl(elseifToken, expression, thenToken, statements);
	}

	public IfStatementTreeImpl ifStatement(InternalSyntaxToken ifToken, ExpressionTree expression,
			InternalSyntaxToken thenToken, StatementsTreeImpl statements,
			Optional<List<ElseifClauseTree>> elseifClauses, Optional<ElseClauseTreeImpl> elseClause,
			InternalSyntaxToken endToken, InternalSyntaxToken ifToken2, InternalSyntaxToken semiToken) {
		return new IfStatementTreeImpl(ifToken, expression, thenToken, statements,
				elseifClauses.isPresent() ? elseifClauses.get() : null,
				elseClause.isPresent() ? elseClause.get() : null, endToken, ifToken2, semiToken);
	}

	public DeclareStatementTreeImpl declareStatement(InternalSyntaxToken declareToken, IdentifierTree identfier,
			Optional<List<Tuple<InternalSyntaxToken,IdentifierTree>>> rest,
			Optional<InternalSyntaxToken> sharedExt, Object dataType, Optional<ExpressionTree> initialValue,
			InternalSyntaxToken semi) {
		if (dataType instanceof Tuple) {
			Tuple<Optional<InternalSyntaxToken>, DataTypeTreeImpl> dt = (Tuple) dataType;
			return new DeclareStatementTreeImpl(declareToken, nameList(identfier, rest),
					sharedExt.isPresent() ? sharedExt.get() : null, dt.first().isPresent() ? dt.first().get() : null,
					dt.second(), initialValue.isPresent() ? initialValue.get() : null, semi);
		} else {
			return new DeclareStatementTreeImpl(declareToken, nameList(identfier, rest),
					sharedExt.isPresent() ? sharedExt.get() : null, (InternalSyntaxToken) dataType,
					initialValue.isPresent() ? initialValue.get() : null, semi);
		}
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

	public <T, U> Tuple<T, U> newTuple28(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple30(T first, U second) {
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

	public <T, U> Tuple<T, U> newTuple42(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple43(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple45(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple46(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple47(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple48(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple60(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple61(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple62(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple63(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple64(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple65(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple66(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple67(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple68(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple69(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple70(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple71(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple72(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple73(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple74(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple76(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple77(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple78(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple79(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple80(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple81(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple82(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple83(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple84(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple85(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple86(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple87(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple88(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple89(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple90(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple91(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple92(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple93(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple94(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple95(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple96(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple97(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple98(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple99(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple100(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple101(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple102(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple103(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple104(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple105(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple106(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple107(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple108(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple109(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple110(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple111(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple112(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple113(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple114(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple115(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple116(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple117(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple118(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U, V> Triple<T, U, V> newTriple2(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public <T, U, V> Triple<T, U, V> newTriple3(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public <T, U, V> Triple<T, U, V> newTriple4(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public <T, U, V> Triple<T, U, V> newTriple5(T first, U second, V third) {
		return newTriple(first, second, third);
	}

	public <T, U, V> Triple<T, U, V> newTriple6(T first, U second, V third) {
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
	private static SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferenceList(AliasedFieldReferenceTreeImpl parameterTree,
			Optional<List<Tuple<InternalSyntaxToken, AliasedFieldReferenceTreeImpl>>> optional) {

		ImmutableList.Builder<AliasedFieldReferenceTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(parameterTree);

		if (optional.isPresent()) {
			for (Tuple<InternalSyntaxToken, AliasedFieldReferenceTreeImpl> pair : optional.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
	}

	private static SeparatedList<AliasedExpressionTree> aliasedExpressionList(AliasedExpressionTreeImpl parameterTree,
			Optional<List<Tuple<InternalSyntaxToken, AliasedExpressionTreeImpl>>> optional) {

		ImmutableList.Builder<AliasedExpressionTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(parameterTree);

		if (optional.isPresent()) {
			for (Tuple<InternalSyntaxToken, AliasedExpressionTreeImpl> pair : optional.get()) {
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

		if (pathElements.isPresent()) {
			for (Tuple<InternalSyntaxToken, PathElementTreeImpl> pair : pathElements.get()) {
				InternalSyntaxToken dotToken = pair.first();

				dots.add(dotToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), dots.build(), false);
	}

	private static SeparatedList<SqlStateTree> sqlStateList(SqlStateTreeImpl sqlStateTree,
			Optional<List<Tuple<InternalSyntaxToken, SqlStateTreeImpl>>> optional) {

		ImmutableList.Builder<SqlStateTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(sqlStateTree);

		if (optional.isPresent()) {
			for (Tuple<InternalSyntaxToken, SqlStateTreeImpl> pair : optional.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
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

		private static SeparatedList<IdentifierTree> nameList(IdentifierTree identfier,
			Optional<List<Tuple<InternalSyntaxToken, IdentifierTree>>> rest) {

		ImmutableList.Builder<IdentifierTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(identfier);

		if (rest.isPresent()) {
			for (Tuple<InternalSyntaxToken, IdentifierTree> pair : rest.get()) {
				InternalSyntaxToken commaToken = pair.first();

				commas.add(commaToken);
				elements.add(pair.second());
			}
		}

		return new SeparatedList<>(elements.build(), commas.build());
	}

	private static SeparatedList<SetColumnTree> setColumnList(SetColumnTreeImpl element,
			Optional<List<Tuple<InternalSyntaxToken, SetColumnTreeImpl>>> rest) {

		ImmutableList.Builder<SetColumnTree> elements = ImmutableList.builder();
		ImmutableList.Builder<InternalSyntaxToken> commas = ImmutableList.builder();

		elements.add(element);

		if (rest.isPresent()) {
			for (Tuple<InternalSyntaxToken, SetColumnTreeImpl> pair : rest.get()) {
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

	public TheFunctionTreeImpl theFunction(InternalSyntaxToken theKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, InternalSyntaxToken closingParenthesis) {
		return new TheFunctionTreeImpl(theKeyword, openingParenthesis, expression, closingParenthesis);
	}

	public EsqlContentsTreeImpl esqlContents(Optional<List<StatementTree>> zeroOrMore) {
		return new EsqlContentsTreeImpl(optionalList(zeroOrMore));

	}

	public CreateFunctionStatementTreeImpl createFunctionStatement(InternalSyntaxToken createKeyword,
			InternalSyntaxToken functionKeyword, IdentifierTree identifier, InternalSyntaxToken openingParenthesis,
			Optional<ParameterTree> parameter, Optional<List<Tuple<InternalSyntaxToken, ParameterTree>>> restParameter,
			InternalSyntaxToken closingParenthesis, Optional<ReturnTypeTreeImpl> returnType,
			Optional<LanguageTreeImpl> language, Optional<ResultSetTreeImpl> resultSet,
			RoutineBodyTreeImpl routineBody) {
		if (parameter.isPresent()) {
			return new CreateFunctionStatementTreeImpl(createKeyword, functionKeyword, identifier, openingParenthesis,
					parameterList(parameter.get(), restParameter), closingParenthesis,
					returnType.isPresent() ? returnType.get() : null, language.isPresent() ? language.get() : null,
					resultSet.isPresent() ? resultSet.get() : null, routineBody);
		} else {
			return new CreateFunctionStatementTreeImpl(createKeyword, functionKeyword, identifier, openingParenthesis,
					new SeparatedList<>(Collections.<ParameterTree>emptyList(),
							Collections.<InternalSyntaxToken>emptyList()),
					closingParenthesis, returnType.isPresent() ? returnType.get() : null,
					language.isPresent() ? language.get() : null, resultSet.isPresent() ? resultSet.get() : null,
					routineBody);
		}
	}

	public CreateProcedureStatementTreeImpl createProcedureStatement(InternalSyntaxToken createKeyword,
			InternalSyntaxToken procedureKeyword, IdentifierTree identifier,
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
					new SeparatedList<>(Collections.<ParameterTree>emptyList(),
							Collections.<InternalSyntaxToken>emptyList()),
					closingParenthesis, returnType.isPresent() ? returnType.get() : null,
					language.isPresent() ? language.get() : null, resultSet.isPresent() ? resultSet.get() : null,
					routineBody);
		}
	}

	public ExternalRoutineBodyTreeImpl externalRoutineBody(InternalSyntaxToken externalKeyword,
			InternalSyntaxToken nameKeyword, InternalSyntaxToken externalRoutineName,
			Optional<JavaClassloaderServiceTreeImpl> classloader,
			InternalSyntaxToken semiToken) {
		return new ExternalRoutineBodyTreeImpl(externalKeyword, nameKeyword, externalRoutineName,
				/*classloader.isPresent() ? classloader.get() : */null, semiToken);
	}

	public RoutineBodyTreeImpl routineBody(Tree firstOf) {
		if (firstOf instanceof StatementTree) {
			return new RoutineBodyTreeImpl((StatementTree) firstOf);
		} else {
			return new RoutineBodyTreeImpl((ExternalRoutineBodyTreeImpl) firstOf);
		}
	}

	public ParameterTreeImpl parameter(Optional<InternalSyntaxToken> directionIndicator, IdentifierTree identifier,
			Optional<Object> optional, Optional<NullableTreeImpl> nullable) {
		if (optional.isPresent()) {
			if (optional.get() instanceof Tuple) {
				Tuple<Optional<InternalSyntaxToken>, DataTypeTreeImpl> t = (Tuple) optional.get();
				return new ParameterTreeImpl(directionIndicator.isPresent() ? directionIndicator.get() : null,
						identifier, t.first().isPresent() ? t.first().get() : null, t.second(), nullable.isPresent()?nullable.get():null);
			} else {
				return new ParameterTreeImpl(directionIndicator.isPresent() ? directionIndicator.get() : null,
						identifier, (InternalSyntaxToken) optional.get(), nullable.isPresent()?nullable.get():null);
			}

		} else {
			return new ParameterTreeImpl(directionIndicator.isPresent() ? directionIndicator.get() : null, identifier, nullable.isPresent()?nullable.get():null);

		}
	}
	public NullableTreeImpl nullable(Object object){
		if (object instanceof Tuple){
			Tuple<InternalSyntaxToken, InternalSyntaxToken> tuple = (Tuple) object;
			return new NullableTreeImpl(tuple.first(),tuple.second());
		} else {
			return new NullableTreeImpl((InternalSyntaxToken) object);
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
			InternalSyntaxToken moduleType, InternalSyntaxToken moduleKeyword, IdentifierTree indentifier,
			Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword, InternalSyntaxToken moduleKeyword2,
			InternalSyntaxToken semi) {
		
		return new CreateModuleStatementTreeImpl(createKeyword, moduleType, moduleKeyword, indentifier,
				statements(statements), endKeyword, moduleKeyword2, semi);
	}

	public ResultSetTreeImpl resultSet(InternalSyntaxToken dynamicKeyword, InternalSyntaxToken resultKeyword,
			InternalSyntaxToken setsKeyword, InternalSyntaxToken integer) {

		return new ResultSetTreeImpl(dynamicKeyword, resultKeyword, setsKeyword, integer);
	}

	public PropagateStatementTreeImpl propagateStatement(InternalSyntaxToken propagateKeyword,
			Optional<Tuple<InternalSyntaxToken, Tuple<InternalSyntaxToken, ExpressionTree>>> target,
			Optional<MessageSourceTreeImpl> messageSource, Optional<ControlsTreeImpl> controls,
			InternalSyntaxToken semi) {

		if (target.isPresent()) {
			return new PropagateStatementTreeImpl(propagateKeyword, target.get().first(), target.get().second().first(),
					target.get().second().second(), messageSource.isPresent() ? messageSource.get() : null,
					controls.isPresent() ? controls.get() : null, semi);
		} else {
			return new PropagateStatementTreeImpl(propagateKeyword,
					messageSource.isPresent() ? messageSource.get() : null,
					controls.isPresent() ? controls.get() : null, semi);
		}

	}

	public MessageSourceTreeImpl messageSource(Optional<Tuple<InternalSyntaxToken, ExpressionTree>> environment,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> message,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> exception) {
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

	public BeginEndStatementTreeImpl beginEndStatement(Optional<Tuple<LabelTreeImpl, InternalSyntaxToken>> label1,
			InternalSyntaxToken beginKeyword,
			Optional<Tuple<Optional<InternalSyntaxToken>, InternalSyntaxToken>> atomic,
			StatementsTreeImpl statements, InternalSyntaxToken endKeyword, Optional<LabelTreeImpl> label2,
			InternalSyntaxToken semiToken) {
		if (label1.isPresent()) {
			return new BeginEndStatementTreeImpl(label1.get().first(), label1.get().second(), beginKeyword,
					atomic.isPresent() && atomic.get().first().isPresent() ? atomic.get().first().get() : null,
					atomic.isPresent() ? atomic.get().second() : null, statements, endKeyword,
					label2.isPresent() ? label2.get() : null, semiToken);
		} else {
			return new BeginEndStatementTreeImpl(beginKeyword,
					atomic.isPresent() && atomic.get().first().isPresent() ? atomic.get().first().get() : null,
					atomic.isPresent() ? atomic.get().second() : null, statements, endKeyword, semiToken);
		}
	}

	public ExpressionTree expression(ExpressionTree logical_OR_EXPRESSION) {
		return logical_OR_EXPRESSION;
	}

	public SeparatedList<ExpressionTree> argumentList(ExpressionTree argument,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> restArguments) {
		List<ExpressionTree> arguments = Lists.newArrayList();
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
			Optional<SeparatedList<ExpressionTree>> arguments, InternalSyntaxToken closeParenToken) {
		return new ParameterListTreeImpl(openParenToken, arguments.isPresent() ? arguments.get()
				: new SeparatedList<>(Collections.<ExpressionTree>emptyList(), Collections.<InternalSyntaxToken>emptyList()),
				closeParenToken);
	}

	public ExpressionTree callExpression(Object firstOf) {
		if (firstOf instanceof FunctionTree) {
			return new CallExpressionTreeImpl((FunctionTree) firstOf);
		} else if (firstOf instanceof Tuple) {
			Tuple<VariableReferenceTree, ParameterListTreeImpl> t = (Tuple) firstOf;
			return new CallExpressionTreeImpl(t.first(), t.second());
		} else if (firstOf instanceof ExpressionTree) {
			return (ExpressionTree)firstOf;
		} else {
			return new CallExpressionTreeImpl((VariableReferenceTree) firstOf);
		}
	}

	public InExpressionTreeImpl inExpression(ExpressionTree expression, Optional<InternalSyntaxToken> notKeyword,  InternalSyntaxToken inKeyword,
			ParameterListTreeImpl argumentList) {
		return new InExpressionTreeImpl(expression, notKeyword.isPresent()?notKeyword.get():null, inKeyword, argumentList);
	}

	public IsExpressionTreeImpl isExpression(ExpressionTree expression, InternalSyntaxToken isKeyword, Optional<InternalSyntaxToken> notKeyword,  
			Optional<InternalSyntaxToken> plusMinus, InternalSyntaxToken with) {
		return new IsExpressionTreeImpl(expression, isKeyword, notKeyword.isPresent()?notKeyword.get():null, plusMinus.isPresent()?plusMinus.get():null, with);
	}

	public BetweenExpressionTreeImpl betweenExpression(ExpressionTree expression, Optional<InternalSyntaxToken> notKeyword,  InternalSyntaxToken betweenKeyword,
			Optional<InternalSyntaxToken> symmetricKeyword ,ExpressionTree endpoint1, InternalSyntaxToken andKeyword, ExpressionTree endpoint2) {
		return new BetweenExpressionTreeImpl(expression, notKeyword.isPresent()?notKeyword.get():null, betweenKeyword, symmetricKeyword.isPresent()?symmetricKeyword.get():null,
				endpoint1, andKeyword, endpoint2);
	}

	public LikeExpressionTreeImpl likeExpression(ExpressionTree source, Optional<InternalSyntaxToken> notKeyword,
			InternalSyntaxToken likeKeyword, ExpressionTree pattern,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> escapeClause) {
		return new LikeExpressionTreeImpl(source, notKeyword.isPresent()?notKeyword.get():null, likeKeyword, pattern, 
				escapeClause.isPresent()?escapeClause.get().first():null, escapeClause.isPresent()?escapeClause.get().second():null);
	}
	

	public LiteralTreeImpl timeLiteral(InternalSyntaxToken timeToken) {
		return new LiteralTreeImpl(Kind.TIME_LITERAL, timeToken);
	}

	public LiteralTreeImpl timestampLiteral(InternalSyntaxToken timestampToken) {
		return new LiteralTreeImpl(Kind.TIMESTAMP_LITERAL, timestampToken);
	}

	public LiteralTreeImpl dateLiteral(InternalSyntaxToken dateToken) {
		return new LiteralTreeImpl(Kind.DATE_LITERAL, dateToken);
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

	public ParenthesisedExpressionTreeImpl parenthesisedExpression(InternalSyntaxToken openParenToken,
			ExpressionTree expression, InternalSyntaxToken closeParenToken) {
		return new ParenthesisedExpressionTreeImpl(openParenToken, expression, closeParenToken);
	}

	public IntervalExpressionTreeImpl intervalExpression(InternalSyntaxToken openParenToken,
			ExpressionTree additiveExpression, InternalSyntaxToken closeParenToken,
			IntervalQualifierTreeImpl intervalQualifier) {
		return new IntervalExpressionTreeImpl(openParenToken, additiveExpression, closeParenToken, intervalQualifier);
	}

	public FieldReferenceTreeImpl fieldReference(PathElementTree first,
			Optional<List<Tuple<InternalSyntaxToken, PathElementTreeImpl>>> zeroOrMore) {
		return new FieldReferenceTreeImpl((PathElementTree) first, pathElementList(zeroOrMore));
		
	}

	public NamespaceTreeImpl namespace(InternalSyntaxToken identifier) {
		return new NamespaceTreeImpl(identifier);
	}

	public IndexTreeImpl index(InternalSyntaxToken openBracket,
			Tuple<Optional<InternalSyntaxToken>, Optional<ExpressionTree>> content, InternalSyntaxToken closeBracket) {

		return new IndexTreeImpl(openBracket, content.first().isPresent() ? content.first().get() : null,
				content.second().isPresent() ? content.second().get() : null, closeBracket);
	}

	public PathElementTreeImpl finishPathElement(PathElementTreeImpl tree){
		return tree;
	}
	
	public PathElementTreeImpl pathElement(Optional<PathElementTypeTreeImpl> type, Optional<PathElementNamespaceTreeImpl> namespace, 
			PathElementNameTreeImpl name, Optional<IndexTreeImpl> index) {

		PathElementTreeImpl pathElement = new PathElementTreeImpl();

		if (type.isPresent()){
			pathElement.type(type.get());
		}
		if (namespace.isPresent()){
			pathElement.namespace(namespace.get());
		}
		pathElement.name(name);
		if (index.isPresent()){
			pathElement.index(index.get());
		}
		return pathElement;
	}
	
	public PathElementTreeImpl pathElement(Optional<PathElementTypeTreeImpl> type, IndexTreeImpl index) {
		PathElementTreeImpl pathElement = new PathElementTreeImpl();
		if (type.isPresent()){
			pathElement.type(type.get());
		}
		pathElement.index(index);
		return pathElement;
	}
	
	public PathElementTreeImpl pathElement(PathElementTypeTreeImpl type) {
		PathElementTreeImpl pathElement = new PathElementTreeImpl();
		pathElement.type(type);
		return pathElement;
	}
	
	public PathElementTypeTreeImpl pathElementType(InternalSyntaxToken openParen, ExpressionTree typeExpression,
			InternalSyntaxToken closeParen){
		return new PathElementTypeTreeImpl(openParen, typeExpression, closeParen);
	}

	public PathElementNamespaceTreeImpl pathElementNamespace(Optional<Object> first, InternalSyntaxToken colon){
			if (first.isPresent()) {
				if (first.get() instanceof Triple) {
					Triple<InternalSyntaxToken, ExpressionTree, InternalSyntaxToken> t = (Triple) first.get();
					return new PathElementNamespaceTreeImpl(t.first(), t.second(), t.third(), colon);
				} else if (first.get() instanceof NamespaceTree) {
					return new PathElementNamespaceTreeImpl((NamespaceTree) first.get(), colon);
				} else {
					return new PathElementNamespaceTreeImpl((InternalSyntaxToken) first.get(),colon);
				}
			} else {
				return new PathElementNamespaceTreeImpl(colon);
			}

	}

	public PathElementNameTreeImpl pathElementName(Object name){
		if (name instanceof Triple) {
			Triple<InternalSyntaxToken, Optional<ExpressionTree>, InternalSyntaxToken> triple = (Triple) name;
			return new PathElementNameTreeImpl(triple.first(), triple.second().isPresent()?triple.second().get():null, triple.third());
		} else if(name instanceof IdentifierTreeImpl)  {
			return new PathElementNameTreeImpl((IdentifierTreeImpl)name);
		} else {
			return new PathElementNameTreeImpl((InternalSyntaxToken) name);
		}
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
			return new DataTypeTreeImpl((IntervalDataTypeTreeImpl) firstOf);
		} else if (firstOf instanceof DecimalDataTypeTreeImpl) {
			return new DataTypeTreeImpl((DecimalDataTypeTreeImpl) firstOf);
		} else if (firstOf instanceof Tuple) {
			Tuple<InternalSyntaxToken, Optional<InternalSyntaxToken>> t = (Tuple) firstOf;
			if (t.second().isPresent()) {
				return new DataTypeTreeImpl(t.first(), t.second().get());
			} else {
				return new DataTypeTreeImpl(t.first());
			}
		} else {
			return new DataTypeTreeImpl((InternalSyntaxToken) firstOf);
		}

	}

	public SetStatementTreeImpl setStatement(InternalSyntaxToken setKeyword, ExpressionTree name,
			Optional<InternalSyntaxToken> type, InternalSyntaxToken equal, ExpressionTree expression,
			InternalSyntaxToken semiToken) {
		if (name instanceof FieldReferenceTreeImpl){
			return new SetStatementTreeImpl(setKeyword, (FieldReferenceTreeImpl)name, type.isPresent() ? type.get() : null, equal,
					expression, semiToken);
		} else {
			return new SetStatementTreeImpl(setKeyword, (IdentifierTreeImpl)name, type.isPresent() ? type.get() : null, equal,
					expression, semiToken);
		}
	}

	public LabelTreeImpl label(IdentifierTree identifierTree) {
		return new LabelTreeImpl(identifierTree);
	}

	public IterateStatementTreeImpl iterateStatement(InternalSyntaxToken iterateKeyword, LabelTreeImpl label,
			InternalSyntaxToken semi) {
		return new IterateStatementTreeImpl(iterateKeyword, label, semi);
	}

	public LeaveStatementTreeImpl leaveStatement(InternalSyntaxToken leaveKeyword, LabelTreeImpl label,
			InternalSyntaxToken semi) {
		return new LeaveStatementTreeImpl(leaveKeyword, label, semi);
	}

	public CallStatementTreeImpl callStatement(InternalSyntaxToken callKeyword,
			SchemaNameTree routineName,
			InternalSyntaxToken openParen,
			Optional<Tuple<ExpressionTree, Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>>>> parameterList,
			InternalSyntaxToken closeParen, Optional<Object> qualifiers,
			Optional<Tuple<InternalSyntaxToken, FieldReferenceTreeImpl>> intoClause, InternalSyntaxToken semi) {

		CallStatementTreeImpl result = new CallStatementTreeImpl(callKeyword,
				routineName, openParen,
				parameterList.isPresent() ? expressionList(parameterList.get().first(), parameterList.get().second())
						: new SeparatedList<>(Collections.<ExpressionTree>emptyList(),
								Collections.<InternalSyntaxToken>emptyList()),
				closeParen, semi);
		
		if (qualifiers.isPresent()) {
			if (qualifiers.get() instanceof Tuple) {
				Tuple<InternalSyntaxToken, FieldReferenceTreeImpl> inClause = (Tuple) qualifiers.get();
				result.inClause(inClause.first(), inClause.second());
			} else {
				Triple<InternalSyntaxToken, InternalSyntaxToken, ExpressionTree> externalSchemaClause = (Triple) qualifiers
						.get();
				result.externalSchema(externalSchemaClause.first(), externalSchemaClause.second(),
						externalSchemaClause.third());
			}
		}
		 
		if (intoClause.isPresent()) {
			result.intoClause(intoClause.get().first(), intoClause.get().second());
		}
		return result;
	}

	public CaseStatementTreeImpl caseStatement(InternalSyntaxToken caseKeyword, Object expressionWhen,
			Optional<Tuple<InternalSyntaxToken, StatementsTreeImpl>> elseClause,
			InternalSyntaxToken endKeyword, InternalSyntaxToken caseKeyword2, InternalSyntaxToken semi) {
		ExpressionTree mainExpression = null;
		List<WhenClauseTreeImpl> whenClauses;
		if (expressionWhen instanceof Tuple) {
			Tuple<ExpressionTree, List<WhenClauseTreeImpl>> t = (Tuple) expressionWhen;
			mainExpression = t.first();
			whenClauses = t.second();
		} else {
			whenClauses = (List<WhenClauseTreeImpl>) expressionWhen;
		}

		return new CaseStatementTreeImpl(caseKeyword, mainExpression, whenClauses,
				elseClause.isPresent() ? elseClause.get().first() : null,
				elseClause.isPresent() ? elseClause.get().second() : null,
				endKeyword, caseKeyword2, semi);
	}

	public WhenClauseTreeImpl whenClause(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, StatementsTreeImpl statements) {
		return new WhenClauseTreeImpl(whenKeyword, expression, thenKeyword,
				statements);
	}

	public LoopStatementTreeImpl loopStatementWoLabel(InternalSyntaxToken loopKeyword,
			StatementsTreeImpl statements, InternalSyntaxToken endKeyword, InternalSyntaxToken loopKeyword2,
			InternalSyntaxToken semi) {
			return new LoopStatementTreeImpl(loopKeyword, statements, endKeyword, loopKeyword2, semi);
	}

	public LoopStatementTreeImpl loopStatementWithLabel(LabelTreeImpl label, InternalSyntaxToken colon,
			InternalSyntaxToken loopKeyword, StatementsTreeImpl statements, InternalSyntaxToken endKeyword,
			InternalSyntaxToken loopKeyword2, Optional<LabelTreeImpl> label2, InternalSyntaxToken semi) {
			return new LoopStatementTreeImpl(label, colon, loopKeyword, statements, endKeyword, loopKeyword2,
					label2.isPresent()?label2.get():null, semi);
	}

	public RepeatStatementTreeImpl repeatStatementWithLabel(LabelTreeImpl label, InternalSyntaxToken colon,
			InternalSyntaxToken repeatKeyword, StatementsTreeImpl statements,
			InternalSyntaxToken untilKeyword, ExpressionTree condition, InternalSyntaxToken endKeyword,
			InternalSyntaxToken repeatKeyword2, Optional<LabelTreeImpl> label2, InternalSyntaxToken semi) {
			return new RepeatStatementTreeImpl(label, colon, repeatKeyword, statements, untilKeyword, condition,
					endKeyword, repeatKeyword2, label2.isPresent()?label2.get():null, semi);
	}

	public RepeatStatementTreeImpl repeatStatementWoLabel(InternalSyntaxToken repeatKeyword,
			StatementsTreeImpl statements, InternalSyntaxToken untilKeyword, ExpressionTree condition,
			InternalSyntaxToken endKeyword, InternalSyntaxToken repeatKeyword2, InternalSyntaxToken semi) {
			return new RepeatStatementTreeImpl(repeatKeyword, statements, untilKeyword, condition, endKeyword,
					repeatKeyword2, semi);
	}

	public WhileStatementTreeImpl whileStatementWithLabel(LabelTreeImpl label, InternalSyntaxToken colon,
			InternalSyntaxToken whileKeyword, ExpressionTree condition, InternalSyntaxToken doKeyword,
			StatementsTreeImpl statements, InternalSyntaxToken endKeyword, InternalSyntaxToken whileKeyword2,
			Optional<LabelTreeImpl> label2, InternalSyntaxToken semi) {
			return new WhileStatementTreeImpl(label, colon, whileKeyword, condition, doKeyword, statements,
					endKeyword, whileKeyword2, label2.isPresent()?label2.get():null, semi);
	}

	public WhileStatementTreeImpl whileStatementWoLabel(InternalSyntaxToken whileKeyword, ExpressionTree condition,
			InternalSyntaxToken doKeyword, StatementsTreeImpl statements, InternalSyntaxToken endKeyword,
			InternalSyntaxToken whileKeyword2, InternalSyntaxToken semi) {
			return new WhileStatementTreeImpl(whileKeyword, condition, doKeyword, statements, endKeyword,
					whileKeyword2, semi);
	}

	public ReturnStatementTreeImpl returnStatement(InternalSyntaxToken returnKeyword,
			Optional<ExpressionTree> expression, InternalSyntaxToken semi) {
		return new ReturnStatementTreeImpl(returnKeyword, expression.isPresent() ? expression.get() : null, semi);
	}

	public ThrowStatementTreeImpl throwStatement(InternalSyntaxToken throwKeyword,
			Optional<InternalSyntaxToken> userKeyword, InternalSyntaxToken exceptionKeyword,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> severity,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> catalog,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> message,
			Optional<Tuple<InternalSyntaxToken, ParameterListTreeImpl>> values, InternalSyntaxToken semi) {
		return new ThrowStatementTreeImpl(throwKeyword, userKeyword.orNull(), exceptionKeyword,
				severity.isPresent() ? severity.get().first() : null,
				severity.isPresent() ? severity.get().second() : null,
				catalog.isPresent() ? catalog.get().first() : null, catalog.isPresent() ? catalog.get().second() : null,
				message.isPresent() ? message.get().first() : null, message.isPresent() ? message.get().second() : null,
				values.isPresent() ? values.get().first() : null, values.isPresent() ? values.get().second() : null,
				semi);
	}

	public AttachStatementTreeImpl attachStatement(InternalSyntaxToken attachKeyword,
			FieldReferenceTreeImpl dynamicReference, InternalSyntaxToken toKeyword,
			FieldReferenceTreeImpl fieldReference, InternalSyntaxToken asKeyword, InternalSyntaxToken location,
			InternalSyntaxToken semi) {
		return new AttachStatementTreeImpl(attachKeyword, dynamicReference, toKeyword, fieldReference, asKeyword,
				location, semi);
	}

	public CreateStatementTreeImpl createStatement(InternalSyntaxToken createKeyword, Object qualifier,
			FieldReferenceTreeImpl target, Optional<Tuple<InternalSyntaxToken, FieldReferenceTreeImpl>> asClause,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> domainClause, Optional<EsqlTree> restClauses,
			InternalSyntaxToken semi) {

		InternalSyntaxToken qualifierName;
		InternalSyntaxToken qualifierOfKeyword = null;
		if (qualifier instanceof Tuple) {
			Tuple<InternalSyntaxToken, InternalSyntaxToken> qualiTuple = (Tuple) qualifier;
			qualifierName = qualiTuple.first();
			qualifierOfKeyword = qualiTuple.second();
		} else {
			qualifierName = (InternalSyntaxToken) qualifier;
		}

		RepeatClauseTreeImpl repeatClause = null;
		ValuesClauseTreeImpl valuesClause = null;
		FromClauseTreeImpl fromClause = null;
		ParseClauseTreeImpl parseClause = null;

		if (restClauses.orNull() instanceof RepeatClauseTreeImpl) {
			repeatClause = (RepeatClauseTreeImpl) restClauses.get();
		} else if (restClauses.orNull() instanceof ValuesClauseTreeImpl) {
			valuesClause = (ValuesClauseTreeImpl) restClauses.get();
		} else if (restClauses.orNull() instanceof FromClauseTreeImpl) {
			fromClause = (FromClauseTreeImpl) restClauses.get();
		} else if (restClauses.orNull() instanceof ParseClauseTreeImpl) {
			parseClause = (ParseClauseTreeImpl) restClauses.get();
		}

		return new CreateStatementTreeImpl(createKeyword, qualifierName, qualifierOfKeyword, target,
				asClause.isPresent() ? asClause.get().first() : null,
				asClause.isPresent() ? asClause.get().second() : null,
				domainClause.isPresent() ? domainClause.get().first() : null,
				domainClause.isPresent() ? domainClause.get().second() : null, repeatClause, valuesClause, fromClause,
				parseClause, semi);
	}

	public RepeatClauseTreeImpl repeatClause(InternalSyntaxToken repeatKeyword,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> value) {
		return new RepeatClauseTreeImpl(repeatKeyword, value.isPresent() ? value.get().first() : null,
				value.isPresent() ? value.get().second() : null);
	}

	public ValuesClauseTreeImpl valuesClause(Optional<Tuple<InternalSyntaxToken, PathElementTree>> identity,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> type,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> namespace,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> name,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> value) {
		return new ValuesClauseTreeImpl(identity.isPresent() ? identity.get().first() : null,
				identity.isPresent() ? identity.get().second() : null, type.isPresent() ? type.get().first() : null,
				type.isPresent() ? type.get().second() : null, namespace.isPresent() ? namespace.get().first() : null,
				namespace.isPresent() ? namespace.get().second() : null, name.isPresent() ? name.get().first() : null,
				name.isPresent() ? name.get().second() : null, value.isPresent() ? value.get().first() : null,
				value.isPresent() ? value.get().second() : null);
	}

	public FromClauseTreeImpl fromClause(InternalSyntaxToken fromKeyword, FieldReferenceTreeImpl fieldReference) {
		return new FromClauseTreeImpl(fromKeyword, fieldReference);
	}

	public ParseClauseTreeImpl parseClause(InternalSyntaxToken parseKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, Optional<ExpressionTree>>>> parameters,
			InternalSyntaxToken closingParenthesis) {

		return new ParseClauseTreeImpl(parseKeyword, openingParenthesis, expression, parameters.isPresent()?parameters.get():null, closingParenthesis);
	}

	public DeleteStatementTreeImpl deleteStatement(InternalSyntaxToken deleteKeyword, Object qualifier,
			FieldReferenceTreeImpl fieldReference, InternalSyntaxToken semi) {

		if (qualifier instanceof Tuple) {
			Tuple<InternalSyntaxToken, InternalSyntaxToken> tuple = (Tuple) qualifier;
			return new DeleteStatementTreeImpl(deleteKeyword, tuple.first(), tuple.second(), fieldReference, semi);
		} else {
			return new DeleteStatementTreeImpl(deleteKeyword, (InternalSyntaxToken) qualifier, null, fieldReference,
					semi);
		}

	}

	public DetachStatementTreeImpl detachStatement(InternalSyntaxToken detachKeyword,
			FieldReferenceTreeImpl fieldReference, InternalSyntaxToken semi) {

		return new DetachStatementTreeImpl(detachKeyword, fieldReference, semi);
	}

	public ResignalStatementTreeImpl resignalStatement(InternalSyntaxToken resignalKeyword, InternalSyntaxToken semi) {
		return new ResignalStatementTreeImpl(resignalKeyword, semi);
	}

	public ForStatementTreeImpl forStatement(InternalSyntaxToken forKeyword, InternalSyntaxToken correlationName,
			InternalSyntaxToken asKeyword, FieldReferenceTreeImpl fieldReference, InternalSyntaxToken doKeyword,
			StatementsTreeImpl statements, InternalSyntaxToken endKeyword, InternalSyntaxToken forKeyword2,
			InternalSyntaxToken semi) {
		return new ForStatementTreeImpl(forKeyword, correlationName, asKeyword, fieldReference, doKeyword,
				statements, forKeyword2, endKeyword, semi);
	}

	public MoveStatementTreeImpl moveStatement(InternalSyntaxToken moveKeyword, InternalSyntaxToken target,
			Object qualifier, InternalSyntaxToken semi) {
		if (qualifier instanceof Tuple) {
			if (((Tuple<Tree, Tree>) qualifier).second() instanceof NameClausesTree) {
				Tuple<InternalSyntaxToken, NameClausesTreeImpl> tuple = (Tuple) qualifier;
				return new MoveStatementTreeImpl(moveKeyword, target, tuple.first(), tuple.second(), semi);
			} else {
				Tuple<InternalSyntaxToken, FieldReferenceTreeImpl> tuple = (Tuple) qualifier;
				return new MoveStatementTreeImpl(moveKeyword, target, tuple.first(), tuple.second(), semi);
			}
		} else {
			return new MoveStatementTreeImpl(moveKeyword, target, (InternalSyntaxToken) qualifier, semi);
		}
	}

	public NameClausesTreeImpl nameClauses(Object params) {
		
		if (params instanceof Triple){
			Triple<Tree,Tree,Tree> t = (Triple)params;
			if (t.first() instanceof InternalSyntaxToken){
				Triple<InternalSyntaxToken, Optional<InternalSyntaxToken>, Optional<InternalSyntaxToken>> repeat = (Triple)params;
				return new NameClausesTreeImpl(null, null, null, null, null, null, null, null, null, repeat.first(), repeat.second().isPresent()?repeat.second().get():null, repeat.third().isPresent()?repeat.third().get():null);
			}else{
				Triple<
					Optional<Tuple<InternalSyntaxToken, ExpressionTree>>, 
					Optional<Tuple<InternalSyntaxToken,Object>>, 
					Optional<Tuple<InternalSyntaxToken,ExpressionTree>>
				> t2 = (Triple)params;

				if (t2.second().isPresent() && t2.second().get().second() instanceof InternalSyntaxToken){
					return new NameClausesTreeImpl(
							t2.first().isPresent()?t2.first().get().first():null, t2.first().isPresent()?t2.first().get().second():null, 
							t2.second().get().first(), null, (InternalSyntaxToken)t2.second().get().second(), 
							t2.third().isPresent()?t2.third().get().first():null, t2.third().isPresent()?t2.third().get().second():null, 
							null, null, null, null, null);
				} else {
					//Expression
					return new NameClausesTreeImpl(t2.first().isPresent()?t2.first().get().first():null, t2.first().isPresent()?t2.first().get().second():null, 
							t2.second().isPresent()?t2.second().get().first():null, (ExpressionTree)(t2.second().isPresent()?t2.second().get().second():null), null, 
							t2.third().isPresent()?t2.third().get().first():null, t2.third().isPresent()?t2.third().get().second():null, 
							null, null, null, null, null);
					
				}
			}
		}else {
			Tuple<InternalSyntaxToken, PathElementTreeImpl> identity = (Tuple)params;
			return new NameClausesTreeImpl(null, null, null, null, null, null, null, identity.first(), identity.second(), null, null, null);
		}
	}

	public DeleteFromStatementTreeImpl deleteFromStatement(InternalSyntaxToken deleteKeyword, InternalSyntaxToken fromKeyword,
			FieldReferenceTreeImpl tableReference, Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> asClause,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> whereClause, InternalSyntaxToken semi) {
		return new DeleteFromStatementTreeImpl(deleteKeyword, fromKeyword, tableReference, asClause.isPresent()?asClause.get().first():null, asClause.isPresent()?asClause.get().second():null, whereClause.isPresent()?whereClause.get().first():null, whereClause.isPresent()?whereClause.get().second():null, semi);
	}

	public InsertStatementTreeImpl insertStatement(InternalSyntaxToken insertKeyword, InternalSyntaxToken intoKeyword,
			FieldReferenceTreeImpl tableReference, Optional<ParameterListTreeImpl> columns,
			InternalSyntaxToken valuesKeyword, InternalSyntaxToken openingParenthesis, ExpressionTree expression,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> restExpression, InternalSyntaxToken closingParenthesis,
			InternalSyntaxToken semi) {
		
		return new InsertStatementTreeImpl(insertKeyword, intoKeyword, tableReference,
				columns.isPresent() ? columns.get() : null, valuesKeyword, openingParenthesis,
				expressionList(expression, restExpression), closingParenthesis, semi);
	}

	public PassthruStatementTreeImpl passthruExpressionList(ParameterListTreeImpl expressionList) {
		return new PassthruStatementTreeImpl(expressionList);
	}

	public PassthruStatementTreeImpl passthruSingleExpression(ExpressionTree expression,
			Optional<Tuple<InternalSyntaxToken, FieldReferenceTreeImpl>> to,
			Optional<Tuple<InternalSyntaxToken, ParameterListTreeImpl>> values) {
		return new PassthruStatementTreeImpl(expression, to.isPresent()?to.get().first():null, to.isPresent()?to.get().second():null,
				values.isPresent()?values.get().first():null, values.isPresent()?values.get().second():null
				);
	}

	public PassthruStatementTreeImpl completePassthruStatement(InternalSyntaxToken passthruKeyword,
			PassthruStatementTreeImpl impl, InternalSyntaxToken semi) {
		return impl.complete(passthruKeyword, semi);
	}

	public SetColumnTreeImpl setColumn(InternalSyntaxToken columnName, InternalSyntaxToken equal,
			ExpressionTree expression) {
		return new SetColumnTreeImpl(columnName, equal, expression);
	}

	public UpdateStatementTreeImpl updateStatement(InternalSyntaxToken updateKeyword, FieldReferenceTreeImpl tableReference,
			Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> as, InternalSyntaxToken setKeyword,
			SetColumnTreeImpl setColumn, Optional<List<Tuple<InternalSyntaxToken, SetColumnTreeImpl>>> restSetColumn,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> whereClause, InternalSyntaxToken semi) {

		SeparatedList<SetColumnTree> setColumns = setColumnList(setColumn, restSetColumn);
		
		return new UpdateStatementTreeImpl(updateKeyword, tableReference, as.isPresent() ? as.get().first() : null,
				as.isPresent() ? as.get().second() : null, setKeyword, setColumns,
				whereClause.isPresent() ? whereClause.get().first() : null,
				whereClause.isPresent() ? whereClause.get().second() : null, semi);
	}

	public SqlStateTreeImpl sqlStateLike(InternalSyntaxToken likeKeyword, LiteralTreeImpl likeText,
			Optional<Tuple<InternalSyntaxToken, LiteralTreeImpl>> escape) {
		return new SqlStateTreeImpl(likeKeyword, likeText, escape.isPresent()?escape.get().first():null, escape.isPresent()?escape.get().second():null);
	}

	public SqlStateTreeImpl sqlStateValue(Optional<InternalSyntaxToken> valueKeyword, LiteralTreeImpl valueText) {
		return new SqlStateTreeImpl(valueKeyword.isPresent()?valueKeyword.get():null, valueText);
	}

	public SqlStateTreeImpl finishSqlState(InternalSyntaxToken sqlstateKeyword, SqlStateTreeImpl sqlState) {
		return sqlState.finish(sqlstateKeyword);
	}

	public DeclareHandlerStatementTreeImpl declareHandlerStatement(InternalSyntaxToken declareKeyword,
			InternalSyntaxToken handlerType, InternalSyntaxToken handlerKeyword, InternalSyntaxToken forKeyword,
			SqlStateTreeImpl sqlState, Optional<List<Tuple<InternalSyntaxToken, SqlStateTreeImpl>>> restSqlState,
			StatementTree statement) {
		
		SeparatedList<SqlStateTree> sqlStates = sqlStateList(sqlState, restSqlState);
	
		return new DeclareHandlerStatementTreeImpl(declareKeyword, handlerType, handlerKeyword, forKeyword, sqlStates, statement);
	}

	public EvalStatementTreeImpl evalStatement(InternalSyntaxToken evalKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, InternalSyntaxToken closingParenthesis, InternalSyntaxToken semi) {
		return new EvalStatementTreeImpl(evalKeyword, openingParenthesis, expression, closingParenthesis, semi);
	}

	public LogStatementTreeImpl logStatement(InternalSyntaxToken logKeyword, Object logType,
			Optional<Tuple<Optional<InternalSyntaxToken>, InternalSyntaxToken>> exception,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> severity,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> catalog,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> message,
			Optional<Tuple<InternalSyntaxToken, ParameterListTreeImpl>> values, InternalSyntaxToken semi) {
		InternalSyntaxToken eventKeyword;
		InternalSyntaxToken userKeyword;
		InternalSyntaxToken traceKeyword;
		if (logType instanceof Tuple) {
			Tuple<InternalSyntaxToken, InternalSyntaxToken> t = (Tuple) logType;
			userKeyword = t.first();
			traceKeyword = t.second();
			eventKeyword = null;
		} else {
			eventKeyword = (InternalSyntaxToken) logType;
			userKeyword = traceKeyword = null;
		}
		return new LogStatementTreeImpl(logKeyword, eventKeyword, userKeyword, traceKeyword,
				exception.isPresent() && exception.get().first().isPresent() ? exception.get().first().get() : null,
				exception.isPresent() ? exception.get().second() : null,
				severity.isPresent() ? severity.get().first() : null,
				severity.isPresent() ? severity.get().second() : null,
				catalog.isPresent() ? catalog.get().first() : null, catalog.isPresent() ? catalog.get().second() : null,
				message.isPresent() ? message.get().first() : null, message.isPresent() ? message.get().second() : null,
				values.isPresent() ? values.get().first() : null, values.isPresent() ? values.get().second() : null,
				semi);
	}

	public ExtractFunctionTreeImpl extractFunction(InternalSyntaxToken extractKeyword, InternalSyntaxToken openingParenthesis,
			InternalSyntaxToken type, InternalSyntaxToken fromKeyword, ExpressionTree sourceDate,
			InternalSyntaxToken closingParenthesis) {
		return new ExtractFunctionTreeImpl(extractKeyword, openingParenthesis, type, fromKeyword, sourceDate, closingParenthesis);
	}

	public RoundFunctionTreeImpl roundFunction(InternalSyntaxToken roundKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceNumber, InternalSyntaxToken comma, ExpressionTree precision,
			Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> mode, InternalSyntaxToken closingParenthesis) {
		
		if (mode.isPresent()){
			return new RoundFunctionTreeImpl(roundKeyword, openingParenthesis, sourceNumber, comma, precision, mode.get().first(), mode.get().second(), closingParenthesis);
		}else{
			return new RoundFunctionTreeImpl(roundKeyword, openingParenthesis, sourceNumber, comma, precision, closingParenthesis);
		}
		
	}

	public OverlayFunctionTreeImpl overlayFunction(InternalSyntaxToken overlayKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceString, InternalSyntaxToken placingKeyword, ExpressionTree sourceString2,
			InternalSyntaxToken fromKeyword, ExpressionTree startPosition,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> forClause, InternalSyntaxToken closingParenthesis) {
		
		if (forClause.isPresent()){
			return new OverlayFunctionTreeImpl( overlayKeyword,  openingParenthesis,
					 sourceString,  placingKeyword,  sourceString2,
					 fromKeyword,  startPosition,
					 forClause.get().first(),forClause.get().second(), closingParenthesis);
		} else {
			return new OverlayFunctionTreeImpl(overlayKeyword,  openingParenthesis,
					 sourceString,  placingKeyword,  sourceString2,
					 fromKeyword,  startPosition, closingParenthesis);
		}
	}
	
	
	
	public PositionFunctionTreeImpl positionFunction(InternalSyntaxToken positionKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree searchExpression, InternalSyntaxToken inKeyword, ExpressionTree sourceExpression,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> fromClause,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> repeatClause, InternalSyntaxToken closingParenthesis) {
		
			return new PositionFunctionTreeImpl( positionKeyword,  openingParenthesis,
					searchExpression,  inKeyword, sourceExpression,
					fromClause.isPresent()?fromClause.get().first():null,  fromClause.isPresent()?fromClause.get().second():null,
							repeatClause.isPresent()?repeatClause.get().first():null,  repeatClause.isPresent()?repeatClause.get().second():null, closingParenthesis);
	}

	public SubstringFunctionTreeImpl substringFunction(InternalSyntaxToken substringKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceExpression, InternalSyntaxToken qualifier, ExpressionTree location,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> forClause, InternalSyntaxToken closingParenthesis) {
		if (forClause.isPresent()){
			return new SubstringFunctionTreeImpl(substringKeyword, openingParenthesis, sourceExpression, qualifier, location, forClause.get().first(), forClause.get().second(), closingParenthesis);
		}else {
			return new SubstringFunctionTreeImpl(substringKeyword, openingParenthesis, sourceExpression, qualifier, location, closingParenthesis);
		}
	}

	public TrimFunctionTreeImpl trimFunction(InternalSyntaxToken trimKeyword, InternalSyntaxToken openingParenthesis,
			Optional<Tuple<Optional<InternalSyntaxToken>,Object>> qualifier,
			ExpressionTree sourceString, InternalSyntaxToken closingParenthesis) {
		
		if (qualifier.isPresent()){
			InternalSyntaxToken prefix = null;
			ExpressionTree trimSingleton = null;
			InternalSyntaxToken fromKeyword = null;
			
			if (qualifier.get().first().isPresent()){
				prefix = qualifier.get().first().get();
			}
			if (qualifier.get().second() instanceof Tuple){
				Tuple<ExpressionTree, InternalSyntaxToken> innerTuple = (Tuple)qualifier.get().second();
				trimSingleton = innerTuple.first();
				fromKeyword = innerTuple.second();
			}else {
				fromKeyword = (InternalSyntaxToken)qualifier.get().second();
			}
			
			
			return new TrimFunctionTreeImpl(trimKeyword, openingParenthesis, 
					prefix, trimSingleton, fromKeyword,
					sourceString, closingParenthesis);
		}else {
			return new TrimFunctionTreeImpl(trimKeyword, openingParenthesis, sourceString, closingParenthesis);
		}
	}

	public AsbitstreamFunctionTreeImpl asbitstreamFunction(InternalSyntaxToken asbitstreamKeyword, InternalSyntaxToken openingParenthesis,
			FieldReferenceTreeImpl fieldReference,
			Optional<List<Tuple<InternalSyntaxToken, Optional<ExpressionTree>>>> parameters, InternalSyntaxToken closingParenthesis) {
		return new AsbitstreamFunctionTreeImpl(asbitstreamKeyword, openingParenthesis, fieldReference, parameters.isPresent()?parameters.get():null, closingParenthesis);
	}

	public ForFunctionTreeImpl forFunction(InternalSyntaxToken forKeyword, Optional<InternalSyntaxToken> qualifier,
			FieldReferenceTreeImpl fieldReference, Optional<Tuple<InternalSyntaxToken, InternalSyntaxToken>> asClause,
			InternalSyntaxToken openingParenthesis, ExpressionTree expression, InternalSyntaxToken closingParenthesis) {
		if (asClause.isPresent()){
			return new ForFunctionTreeImpl(forKeyword, qualifier.isPresent()?qualifier.get():null, fieldReference, asClause.get().first(), asClause.get().second(), openingParenthesis, expression, closingParenthesis);
		} else {
			return new ForFunctionTreeImpl(forKeyword, qualifier.isPresent()?qualifier.get():null, fieldReference, openingParenthesis, expression, closingParenthesis);
		}
	}

	public CastFunctionTreeImpl castFunction(InternalSyntaxToken castKeyword, InternalSyntaxToken openingParenthesis,
			SeparatedList<ExpressionTree> sourceExpressions, InternalSyntaxToken asKeyword, DataTypeTreeImpl dataType,
			Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> parameters, InternalSyntaxToken closingParenthesis) {
		InternalSyntaxToken ccsidKeyword = null;
		ExpressionTree ccsidExpression = null;
		InternalSyntaxToken encodingKeyword = null;
		ExpressionTree encodingExpression = null;
		InternalSyntaxToken formatKeyword = null;
		ExpressionTree formatExpression = null;
		InternalSyntaxToken defaultKeyword = null;
		ExpressionTree defaultExpression = null;
		if (parameters.isPresent()) {
			for (Tuple<InternalSyntaxToken, ExpressionTree> parameter : parameters.get()) {
				if (parameter.first().is(EsqlNonReservedKeyword.CCSID)) {
					ccsidKeyword = parameter.first();
					ccsidExpression = parameter.second();
				} else if (parameter.first().is(EsqlNonReservedKeyword.ENCODING)) {
					encodingKeyword = parameter.first();
					encodingExpression = parameter.second();
				} else if (parameter.first().is(EsqlNonReservedKeyword.FORMAT)) {
					formatKeyword = parameter.first();
					formatExpression = parameter.second();
				} else if (parameter.first().is(EsqlNonReservedKeyword.DEFAULT)) {
					defaultKeyword = parameter.first();
					defaultExpression = parameter.second();
				}
			}
		}
		return new CastFunctionTreeImpl(castKeyword, openingParenthesis, sourceExpressions, asKeyword, dataType,
				ccsidKeyword, ccsidExpression, encodingKeyword, encodingExpression, formatKeyword, formatExpression,
				defaultKeyword, defaultExpression, closingParenthesis);
	}

	public WhenClauseExpressionTreeImpl whenClauseExpression(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, ExpressionTree resultValue) {
		return new WhenClauseExpressionTreeImpl(whenKeyword, expression, thenKeyword, resultValue);
	}

	public CaseFunctionTreeImpl caseFunction(InternalSyntaxToken caseKeyword, Object whenClauses,
			Optional<Tuple<InternalSyntaxToken, ExpressionTree>> elseClause, InternalSyntaxToken endKeyword) {
		
		//Optional<ExpressionTree> sourceValue, List<WhenClauseExpressionTreeImpl> whenClauses
		if (whenClauses instanceof Tuple){
			Tuple<ExpressionTree, List<WhenClauseExpressionTreeImpl>> whenTuple = (Tuple)whenClauses;
			return new CaseFunctionTreeImpl(caseKeyword, whenTuple.first(), 
					whenTuple.second(), elseClause.isPresent() ? elseClause.get().first() : null,
					elseClause.isPresent() ? elseClause.get().second() : null, endKeyword);
		} else {
			return new CaseFunctionTreeImpl(caseKeyword, null, 
					(List<WhenClauseExpressionTreeImpl>)whenClauses, elseClause.isPresent() ? elseClause.get().first() : null,
					elseClause.isPresent() ? elseClause.get().second() : null, endKeyword);
		}
		
		
	}

	public WhereClauseTreeImpl whereClause(InternalSyntaxToken whereKeyword, ExpressionTree expression) {
		return new WhereClauseTreeImpl(whereKeyword, expression);
	}

	public AliasedFieldReferenceTreeImpl aliasFieldReference(FieldReferenceTreeImpl fieldRefernce,
			InternalSyntaxToken asKeyword, InternalSyntaxToken alias) {
		return new AliasedFieldReferenceTreeImpl(fieldRefernce, asKeyword, alias);
	}
	
	public AliasedFieldReferenceTreeImpl aliasFieldReference(FieldReferenceTreeImpl fieldRefernce) {
		return new AliasedFieldReferenceTreeImpl(fieldRefernce, null, null);
	}
	
	public AliasedFieldReferenceTreeImpl finishAliasFieldReference(AliasedFieldReferenceTreeImpl input){
		return input;
	}

	public SelectFunctionTreeImpl selectFunction(InternalSyntaxToken selectKeyword, SelectClauseTreeImpl selectClause,
			FromClauseExpressionTreeImpl fromClause, Optional<WhereClauseTreeImpl> whereClause) {
		return new SelectFunctionTreeImpl(selectKeyword, selectClause,  fromClause, whereClause.isPresent()?whereClause.get():null);
	}

	public FromClauseExpressionTreeImpl fromClauseExpression(InternalSyntaxToken fromKeyword,
			AliasedFieldReferenceTreeImpl aliasedFieldReference,
			Optional<List<Tuple<InternalSyntaxToken, AliasedFieldReferenceTreeImpl>>> rest) {
		
		return new FromClauseExpressionTreeImpl(fromKeyword, aliasedFieldReferenceList(aliasedFieldReference, rest));
	}

	public SelectClauseTreeImpl finishSelectClause(SelectClauseTreeImpl selectClause) {
		return selectClause;
	}

	public SelectClauseTreeImpl selectClauseFields(AliasedExpressionTreeImpl aliasedFieldReference,
			Optional<List<Tuple<InternalSyntaxToken, AliasedExpressionTreeImpl>>> rest) {
		
		return new SelectClauseTreeImpl(aliasedExpressionList(aliasedFieldReference, rest));
	}

	public SelectClauseTreeImpl selectClauseItem(InternalSyntaxToken itemKeyword, ExpressionTree itemExpression) {
		return new SelectClauseTreeImpl(itemKeyword, itemExpression);
	}

	public SelectClauseTreeImpl selectClauseAggregation(InternalSyntaxToken aggregationType, InternalSyntaxToken openingParenthesis,
			ExpressionTree aggregationExpression, InternalSyntaxToken closingParenthesis) {
		return new SelectClauseTreeImpl(aggregationType, openingParenthesis, aggregationExpression, closingParenthesis);
	}

	public AliasedExpressionTreeImpl aliasedExpression(ExpressionTree expression, InternalSyntaxToken asKeyword, FieldReferenceTreeImpl alias) {
		return new AliasedExpressionTreeImpl(expression, asKeyword, alias);
	}

	public AliasedExpressionTreeImpl aliasedExpression(ExpressionTree expression) {
		return new AliasedExpressionTreeImpl(expression, null, null);
	}

	public AliasedExpressionTreeImpl finishAliasedExpression(AliasedExpressionTreeImpl input) {
		return input;
	}

	public RowConstructorFunctionTreeImpl rowConstructorFunction(InternalSyntaxToken rowKeyword, InternalSyntaxToken openingParenthesis,
			AliasedExpressionTreeImpl aliasedExpression,
			Optional<List<Tuple<InternalSyntaxToken, AliasedExpressionTreeImpl>>> rest,
			InternalSyntaxToken closingParenthesis) {

		return new RowConstructorFunctionTreeImpl(rowKeyword, openingParenthesis, aliasedExpressionList(aliasedExpression, rest), closingParenthesis);
	}

	public PassthruFunctionTreeImpl passthruOldSyntax(InternalSyntaxToken comma, SeparatedList<ExpressionTree> argumentList) {
		return new PassthruFunctionTreeImpl(comma, argumentList);
	}

	public PassthruFunctionTreeImpl passthruNewSyntax(
			Optional<Tuple<InternalSyntaxToken, FieldReferenceTreeImpl>> toClause,
			Optional<Tuple<InternalSyntaxToken, ParameterListTreeImpl>> valuesClause) {
		
		return new PassthruFunctionTreeImpl(toClause.isPresent()?toClause.get().first():null,toClause.isPresent()?toClause.get().second():null,valuesClause.isPresent()?valuesClause.get().first():null,valuesClause.isPresent()?valuesClause.get().second():null);
	}

	public PassthruFunctionTreeImpl finishPassthruFunction(InternalSyntaxToken passthruKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, PassthruFunctionTreeImpl tree, InternalSyntaxToken closingParenthesis) {
		
		tree.finish(passthruKeyword, openingParenthesis, expression, closingParenthesis);
		return tree;
	}

	public StatementsTreeImpl statements(Optional<List<StatementTree>> listOfStatements){
		return new StatementsTreeImpl(optionalList(listOfStatements));
	}

	public IdentifierTree identifierReference(InternalSyntaxToken identifier) {
		return new IdentifierTreeImpl(Kind.IDENTIFIER_REFERENCE, identifier);
	}

	public IdentifierTree bindingIdentifier(InternalSyntaxToken identifier) {
		return new IdentifierTreeImpl(Kind.BINDING_IDENTIFIER, identifier);
	}
	public IdentifierTree identifierName(InternalSyntaxToken identifier) {
	    return new IdentifierTreeImpl(Kind.PROPERTY_IDENTIFIER, identifier);
	}

	public VariableReferenceTree variableReference(VariableReferenceTree variableReference) {
		return variableReference;
	}

	public ListConstructorFunctionTreeImpl listConstructorFunction(InternalSyntaxToken listKeyword, InternalSyntaxToken openingCurlyBrace,
			SeparatedList<ExpressionTree> argumentList, InternalSyntaxToken closingCurlyBrace) {
		return new ListConstructorFunctionTreeImpl(listKeyword, openingCurlyBrace, argumentList, closingCurlyBrace);
	}


	public JavaClassloaderServiceTreeImpl javaClassLoaderService(InternalSyntaxToken classloaderKeyword, InternalSyntaxToken classLoaderConfigurableServiceName) {
		return new JavaClassloaderServiceTreeImpl(classloaderKeyword,classLoaderConfigurableServiceName);
	}
}
