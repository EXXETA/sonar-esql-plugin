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
import com.exxeta.iss.sonar.esql.api.tree.function.RoundFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.NameClausesTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.parser.TreeFactory.Tuple;
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
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ProgramTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.SchemaNameTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ArrayLiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.BinaryExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.CallExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.InExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IntervalExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ParenthesisedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.PrefixExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.ExtractFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.OverlayFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.PositionFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.RoundFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.SubstringFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.TheFunctionTreeImpl;
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
import com.exxeta.iss.sonar.esql.tree.impl.statement.LabelTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LanguageTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LeaveStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LogStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LoopStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MessageSourceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MoveStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.NameClausesTreeImpl;
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
			.put(EsqlNonReservedKeyword.NOT.getValue(), Kind.LOGICAL_COMPLEMENT).build();

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
			InternalSyntaxToken thenToken, Optional<List<StatementTree>> statements) {
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

	public DeclareStatementTreeImpl declareStatement(InternalSyntaxToken declareToken, InternalSyntaxToken name,
			Optional<List<Tuple<InternalSyntaxToken, InternalSyntaxToken>>> restNames,
			Optional<InternalSyntaxToken> sharedExt, Object dataType, Optional<ExpressionTree> initialValue,
			InternalSyntaxToken semi) {
		if (dataType instanceof Tuple) {
			Tuple<Optional<InternalSyntaxToken>, DataTypeTreeImpl> dt = (Tuple) dataType;
			return new DeclareStatementTreeImpl(declareToken, nameList(name, restNames),
					sharedExt.isPresent() ? sharedExt.get() : null, dt.first().isPresent() ? dt.first().get() : null,
					dt.second(), initialValue.isPresent() ? initialValue.get() : null, semi);
		} else {
			return new DeclareStatementTreeImpl(declareToken, nameList(name, restNames),
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

	public <T, U> Tuple<T, U> newTuple42(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple43(T first, U second) {
		return newTuple(first, second);
	}

	public <T, U> Tuple<T, U> newTuple44(T first, U second) {
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

	public <T, U> Tuple<T, U> newTuple75(T first, U second) {
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

	public <T, U, V> Triple<T, U, V> newTriple1(T first, U second, V third) {
		return newTriple(first, second, third);
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

	private static SeparatedList<InternalSyntaxToken> tokenList(InternalSyntaxToken element,
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

	private static SeparatedList<InternalSyntaxToken> nameList(InternalSyntaxToken element,
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
			InternalSyntaxToken functionKeyword, InternalSyntaxToken identifier, InternalSyntaxToken openingParenthesis,
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
					new SeparatedList<>(Collections.<ParameterTree>emptyList(),
							Collections.<InternalSyntaxToken>emptyList()),
					closingParenthesis, returnType.isPresent() ? returnType.get() : null,
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
			Optional<List<StatementTree>> optional, InternalSyntaxToken endKeyword, InternalSyntaxToken moduleKeyword2,
			InternalSyntaxToken semi) {
		List<StatementTree> moduleStatementsList = optionalList(optional);
		return new CreateModuleStatementTreeImpl(createKeyword, moduleType, moduleKeyword, moduleName,
				moduleStatementsList, endKeyword, moduleKeyword2);
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

	public BeginEndStatementTreeImpl beginEndStatement(Optional<Tuple<LabelTreeImpl, InternalSyntaxToken>> label1,
			InternalSyntaxToken beginKeyword,
			Optional<Tuple<Optional<InternalSyntaxToken>, InternalSyntaxToken>> atomic,
			Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword, Optional<LabelTreeImpl> label2,
			InternalSyntaxToken semiToken) {
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

	public InExpressionTreeImpl inExpression(FieldReferenceTreeImpl fieldReference, InternalSyntaxToken inKeyword,
			SeparatedList<Tree> argumentList) {
		return new InExpressionTreeImpl(fieldReference, inKeyword, argumentList);
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
		if (first instanceof ExpressionTree) {
			return new FieldReferenceTreeImpl((ExpressionTree) first, pathElementList(zeroOrMore));
		} else if (first instanceof PathElementTree) {
			return new FieldReferenceTreeImpl((PathElementTree) first, pathElementList(zeroOrMore));
		} else {
			return new FieldReferenceTreeImpl((InternalSyntaxToken) first, pathElementList(zeroOrMore));
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
			Optional<Triple<InternalSyntaxToken, Tuple<InternalSyntaxToken, Optional<List<Tuple<InternalSyntaxToken, InternalSyntaxToken>>>>, InternalSyntaxToken>> type,
			Optional<Tuple<Optional<Object>, InternalSyntaxToken>> namespace, Object name,
			Optional<IndexTreeImpl> index) {

		PathElementTreeImpl pathElement = new PathElementTreeImpl();

		if (type.isPresent()) {
			pathElement.setType(type.get().first(),
					tokenList(type.get().second().first(), type.get().second().second()), type.get().third());
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

	public SetStatementTreeImpl setStatement(InternalSyntaxToken setKeyword, FieldReferenceTreeImpl fieldReference,
			Optional<InternalSyntaxToken> type, InternalSyntaxToken equal, ExpressionTree expression,
			InternalSyntaxToken semiToken) {
		return new SetStatementTreeImpl(setKeyword, fieldReference, type.isPresent() ? type.get() : null, equal,
				expression, semiToken);
	}

	public LabelTreeImpl label(InternalSyntaxToken labelName) {
		return new LabelTreeImpl(labelName);
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
			Optional<Tuple<SchemaNameTree, InternalSyntaxToken>> schemaName, InternalSyntaxToken routineName,
			InternalSyntaxToken openParen,
			Optional<Tuple<ExpressionTree, Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>>>> parameterList,
			InternalSyntaxToken closeParen, Optional<Object> qualifiers,
			Optional<Tuple<InternalSyntaxToken, FieldReferenceTreeImpl>> intoClause, InternalSyntaxToken semi) {

		CallStatementTreeImpl result = new CallStatementTreeImpl(callKeyword,
				schemaName.isPresent() ? schemaName.get().first() : null,
				schemaName.isPresent() ? schemaName.get().second() : null, routineName, openParen,
				parameterList.isPresent() ? expressionList(parameterList.get().first(), parameterList.get().second())
						: new SeparatedList<>(Collections.<ExpressionTree>emptyList(),
								Collections.<InternalSyntaxToken>emptyList()),
				closeParen, semi);
		/*
		 * if (qualifiers.isPresent()){ if (qualifiers.get() instanceof Tuple){
		 * Tuple<InternalSyntaxToken,FieldReferenceTreeImpl> inClause =
		 * (Tuple)qualifiers.get(); result.inClause(inClause.first(),
		 * inClause.second()); }else {
		 * Triple<InternalSyntaxToken,InternalSyntaxToken,InternalSyntaxToken>
		 * externalSchemaClause = (Triple)qualifiers.get();
		 * result.externalSchema(externalSchemaClause.first(),
		 * externalSchemaClause.second(), externalSchemaClause.third()); } }
		 */
		if (intoClause.isPresent()) {
			result.intoClause(intoClause.get().first(), intoClause.get().second());
		}
		return result;
	}

	public CaseStatementTreeImpl caseStatement(InternalSyntaxToken caseKeyword, Object expressionWhen,
			Optional<Tuple<InternalSyntaxToken, Optional<List<StatementTree>>>> elseClause,
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
				elseClause.isPresent() && elseClause.get().second().isPresent() ? elseClause.get().second().get()
						: null,
				endKeyword, caseKeyword2, semi);
	}

	public WhenClauseTreeImpl whenClause(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, Optional<List<StatementTree>> statements) {
		return new WhenClauseTreeImpl(whenKeyword, expression, thenKeyword,
				statements.isPresent() ? statements.get() : Collections.emptyList());
	}

	public LoopStatementTreeImpl loopStatementWoLabel(InternalSyntaxToken loopKeyword,
			Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword, InternalSyntaxToken loopKeyword2,
			InternalSyntaxToken semi) {
		if (statements.isPresent()) {
			return new LoopStatementTreeImpl(loopKeyword, statements.get(), endKeyword, loopKeyword2, semi);
		} else {
			return new LoopStatementTreeImpl(loopKeyword, Collections.emptyList(), endKeyword, loopKeyword2, semi);
		}
	}

	public LoopStatementTreeImpl loopStatementWithLabel(LabelTreeImpl label, InternalSyntaxToken colon,
			InternalSyntaxToken loopKeyword, Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword,
			InternalSyntaxToken loopKeyword2, LabelTreeImpl label2, InternalSyntaxToken semi) {
		if (statements.isPresent()) {
			return new LoopStatementTreeImpl(label, colon, loopKeyword, statements.get(), endKeyword, loopKeyword2,
					label2, semi);
		} else {
			return new LoopStatementTreeImpl(label, colon, loopKeyword, Collections.emptyList(), endKeyword,
					loopKeyword2, label2, semi);
		}
	}

	public RepeatStatementTreeImpl repeatStatementWithLabel(LabelTreeImpl label, InternalSyntaxToken colon,
			InternalSyntaxToken repeatKeyword, Optional<List<StatementTree>> statements,
			InternalSyntaxToken untilKeyword, ExpressionTree condition, InternalSyntaxToken endKeyword,
			InternalSyntaxToken repeatKeyword2, LabelTreeImpl label2, InternalSyntaxToken semi) {
		if (statements.isPresent()) {
			return new RepeatStatementTreeImpl(label, colon, repeatKeyword, statements.get(), untilKeyword, condition,
					endKeyword, repeatKeyword2, label2, semi);
		} else {
			return new RepeatStatementTreeImpl(label, colon, repeatKeyword, Collections.emptyList(), untilKeyword,
					condition, endKeyword, repeatKeyword2, label2, semi);
		}
	}

	public RepeatStatementTreeImpl repeatStatementWoLabel(InternalSyntaxToken repeatKeyword,
			Optional<List<StatementTree>> statements, InternalSyntaxToken untilKeyword, ExpressionTree condition,
			InternalSyntaxToken endKeyword, InternalSyntaxToken repeatKeyword2, InternalSyntaxToken semi) {
		if (statements.isPresent()) {
			return new RepeatStatementTreeImpl(repeatKeyword, statements.get(), untilKeyword, condition, endKeyword,
					repeatKeyword2, semi);
		} else {
			return new RepeatStatementTreeImpl(repeatKeyword, Collections.emptyList(), untilKeyword, condition,
					endKeyword, repeatKeyword2, semi);
		}
	}

	public WhileStatementTreeImpl whileStatementWithLabel(LabelTreeImpl label, InternalSyntaxToken colon,
			InternalSyntaxToken whileKeyword, ExpressionTree condition, InternalSyntaxToken doKeyword,
			Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword, InternalSyntaxToken whileKeyword2,
			LabelTreeImpl label2, InternalSyntaxToken semi) {
		if (statements.isPresent()) {
			return new WhileStatementTreeImpl(label, colon, whileKeyword, condition, doKeyword, statements.get(),
					endKeyword, whileKeyword2, label2, semi);
		} else {
			return new WhileStatementTreeImpl(label, colon, whileKeyword, condition, doKeyword, Collections.emptyList(),
					endKeyword, whileKeyword2, label2, semi);
		}
	}

	public WhileStatementTreeImpl whileStatementWoLabel(InternalSyntaxToken whileKeyword, ExpressionTree condition,
			InternalSyntaxToken doKeyword, Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword,
			InternalSyntaxToken whileKeyword2, InternalSyntaxToken semi) {
		if (statements.isPresent()) {
			return new WhileStatementTreeImpl(whileKeyword, condition, doKeyword, statements.get(), endKeyword,
					whileKeyword2, semi);
		} else {
			return new WhileStatementTreeImpl(whileKeyword, condition, doKeyword, Collections.emptyList(), endKeyword,
					whileKeyword2, semi);
		}
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

		InternalSyntaxToken qualifierName = null;
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

	public ValuesClauseTreeImpl valuesClause(Optional<Tuple<InternalSyntaxToken, FieldReferenceTreeImpl>> identity,
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
			SeparatedList<Tree> options, Optional<List<Tuple<InternalSyntaxToken, ExpressionTree>>> parameters,
			InternalSyntaxToken closingParenthesis) {

		InternalSyntaxToken encodingKeyword = null;
		ExpressionTree encoding = null;
		InternalSyntaxToken ccsidKeyword = null;
		ExpressionTree ccsid = null;
		InternalSyntaxToken setKeyword = null;
		ExpressionTree set = null;
		InternalSyntaxToken typeKeyword = null;
		ExpressionTree type = null;
		InternalSyntaxToken formatKeyword = null;
		ExpressionTree format = null;

		if (parameters.isPresent())
			for (Tuple<InternalSyntaxToken, ExpressionTree> param : parameters.get()) {
				if ("ENCODING".equalsIgnoreCase(param.first().text())) {
					encodingKeyword = param.first();
					encoding = param.second();
				} else if ("CCSID".equalsIgnoreCase(param.first().text())) {
					ccsidKeyword = param.first();
					ccsid = param.second();
				} else if ("SET".equalsIgnoreCase(param.first().text())) {
					setKeyword = param.first();
					set = param.second();
				} else if ("TYPE".equalsIgnoreCase(param.first().text())) {
					typeKeyword = param.first();
					type = param.second();
				} else if ("FORMAT".equalsIgnoreCase(param.first().text())) {
					formatKeyword = param.first();
					format = param.second();
				}
			}
		return new ParseClauseTreeImpl(parseKeyword, openingParenthesis, options, encodingKeyword, encoding,
				ccsidKeyword, ccsid, setKeyword, set, typeKeyword, type, formatKeyword, format, closingParenthesis);
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
			Optional<List<StatementTree>> statements, InternalSyntaxToken endKeyword, InternalSyntaxToken forKeyword2,
			InternalSyntaxToken semi) {
		return new ForStatementTreeImpl(forKeyword, correlationName, asKeyword, fieldReference, doKeyword,
				optionalList(statements), forKeyword2, endKeyword, semi);
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
					return new NameClausesTreeImpl(t2.first().isPresent()?t2.first().get().first():null, t2.first().isPresent()?t2.first().get().second():null, 
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
		InternalSyntaxToken eventKeyword, userKeyword, traceKeyword;
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
	
}
