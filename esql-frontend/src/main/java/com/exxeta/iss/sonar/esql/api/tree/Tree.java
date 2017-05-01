/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree;

import org.sonar.sslr.grammar.GrammarRuleKey;

import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.InExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IntervalExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CastFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ExtractFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ForFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.OverlayFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.PassthruFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.PositionFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.RoundFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.RowConstructorFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SubstringFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TrimFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhenClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.AttachStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ControlsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareHandlerStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DetachStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.EvalStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ExternalRoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ForStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.FromClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.InsertStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LabelTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LogStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MessageSourceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MoveStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.NameClausesTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResignalStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.UpdateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ValuesClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.Kinds;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

public interface Tree {
	boolean is(Kind... kind);

	void accept(DoubleDispatchVisitor visitor);

	public enum Kind implements GrammarRuleKey, Kinds {
	    TOKEN(SyntaxToken.class),
		TRIVIA(SyntaxTrivia.class), 
		ELSE_CLAUSE(ElseClauseTree.class), 
		IF_STATEMENT(IfStatementTree.class), 
		ELSEIF_CLAUSE(ElseifClauseTree.class), 
		DECLARE_STATEMENT(DeclareStatementTree.class), 
		PROGRAM(ProgramTree.class), 
		SCHEMA_NAME(SchemaNameTree.class), 
		BROKER_SCHEMA_STATEMENT(BrokerSchemaStatementTree.class), 
		PATH_CLAUSE(PathClauseTree.class), 
		THE_FUNCTION(TheFunctionTree.class), 
		ESQL_CONTENTS(EsqlContentsTree.class), 
		CREATE_FUNCTION_STATEMENT(CreateFunctionStatementTree.class), 
		ROUTINE_BODY(RoutineBodyTree.class), 
		EXTERNAL_ROUTINE_BODY(ExternalRoutineBodyTree.class), 
		PARAMETER(ParameterTree.class), 
		RETURN_TYPE(ReturnTypeTree.class), 
		LANGUAGE(LanguageTree.class), 
		CREATE_MODULE_STATEMENT(CreateModuleStatementTree.class), 
		ROUTINE_DECLARATION(RoutineDeclarationTree.class), 
		RESULT_SET(ResultSetTree.class), 
		CREATE_PROCEDURE_STATEMENT(CreateProcedureStatementTree.class), 
		PROPAGATE_STATEMENT(PropagateStatementTree.class), 
		MESSAGE_SOURCE(MessageSourceTree.class), 
		CONTROLS(ControlsTree.class), 
		BEGIN_END_STATEMENT(BeginEndStatementTree.class), 
		CONDITIONAL_OR(BinaryExpressionTree.class), 
		CONDITIONAL_AND(BinaryExpressionTree.class), 
		EQUAL_TO(BinaryExpressionTree.class), 
		LESS_THAN(BinaryExpressionTree.class), 
		GREATER_THAN(BinaryExpressionTree.class), 
		LESS_THAN_OR_EQUAL_TO(BinaryExpressionTree.class), 
		GREATER_THAN_OR_EQUAL_TO(BinaryExpressionTree.class), 
		PLUS(BinaryExpressionTree.class), 
		MINUS(BinaryExpressionTree.class), 
		MULTIPLY(BinaryExpressionTree.class), 
		NOT_EQUAL_TO(BinaryExpressionTree.class), 
		DIVIDE(BinaryExpressionTree.class), 
		CONCAT(BinaryExpressionTree.class), 
		ARGUMENTS(ParameterListTree.class), 
		PARAMETER_LIST(ParameterListTree.class), 
		LIST_LITERAL(LiteralTree.class), 
		TIME_LITERAL(LiteralTree.class), 
		DATE_LITERAL(LiteralTree.class), 
		ARRAY_LITERAL(LiteralTree.class), 
		INTERVAL_LITERAL(LiteralTree.class), 
		NULL_LITERAL(LiteralTree.class), 
		BOOLEAN_LITERAL(LiteralTree.class), 
		HEX_LITERAL(LiteralTree.class), 
		STRING_LITERAL(LiteralTree.class), 
		NUMERIC_LITERAL(LiteralTree.class), 
		PARENTHESISED_EXPRESSION(ParenthesisedExpressionTree.class), 
		INTERVAL_EXPRESSION(IntervalExpressionTree.class), 
		FIELD_REFERENCE(FieldReferenceTree.class), 
		NAMESPACE(NamespaceTree.class), 
		PATH_ELEMENT(PathElementTree.class), 
		INDEX(IndexTree.class), 
		CALL_EXPRESSION(CallExpressionTree.class), 
		IN_EXPRESSION(InExpressionTree.class),
		INTERVAL_QUALIFIER(IntervalQualifierTree.class), 
		INTERVAL_DATA_TYPE(IntervalDataTypeTree.class), 
		DECIMAL_DATA_TYPE(DecimalDataTypeTree.class), 
		DATA_TYPE(DataTypeTree.class), 
		SET_STATEMENT(SetStatementTree.class),
		ITERATE_STATEMENT(IterateStatementTree.class), 
		LABEL(LabelTree.class), 
		CALL_STATEMENT(CallStatementTree.class), 
		CASE_STATEMENT(CaseStatementTree.class), 
		WHEN_CLAUSE(WhenClauseTree.class), 
		LEAVE_STATEMENT(LeaveStatementTree.class), 
		LOOP_STATEMENT(LoopStatementTree.class), 
		REPEAT_STATEMENT(RepeatStatementTree.class), 
		RETURN_STATEMENT(ReturnStatementTree.class),
		THROW_STATEMENT(ThrowStatementTree.class), 
		LOGICAL_COMPLEMENT(UnaryExpressionTree.class), 
		WHILE_STATEMENT(WhileStatementTree.class), 
		ATTACH_STATEMENT(AttachStatementTree.class), 
		CREATE_STATEMENT(CreateStatementTree.class), 
		REPEAT_CLAUSE(RepeatClauseTree.class), 
		VALUES_CLAUSE(ValuesClauseTree.class), 
		FROM_CLAUSE(FromClauseTree.class), 
		PARSE_CLAUSE(ParseClauseTree.class), 
		DELETE_STATEMENT(DeleteStatementTree.class), 
		DETACH_STATEMENT(DetachStatementTree.class), 
		RESIGNAL_STATEMENT(ResignalStatementTree.class), 
		FOR_STATEMENT(ForStatementTree.class), 
		MOVE_STATEMENT(MoveStatementTree.class), 
		NAME_CLAUSES(NameClausesTree.class), 
		DELETE_FROM_STATEMENT(DeleteFromStatementTree.class), 
		INSERT_STATEMENT(InsertStatementTree.class), 
		PASSTHRU_STATEMENT(PassthruStatementTree.class), 
		UPDATE_STATEMENT(UpdateStatementTree.class), 
		SET_COLUMN(SetColumnTree.class), 
		SQL_STATE(SqlStateTree.class), 
		DECLARE_HANDLER_STATEMENT(DeclareHandlerStatementTree.class), 
		EVAL_STATEMENT(EvalStatementTree.class), 
		LOG_STATEMENT(LogStatementTree.class), 
		EXTRACT_FUNCTION(ExtractFunctionTree.class), 
		ROUND_FUNCTION(RoundFunctionTree.class), 
		OVERLAY_FUNCTION(OverlayFunctionTree.class), 
		POSITION_FUNCTION(PositionFunctionTree.class), 
		SUBSTRING_FUNCTION(SubstringFunctionTree.class), 
		TRIM_FUNCTION(TrimFunctionTree.class),
		ASBITSTREAM_FUNCTION(AsbitstreamFunctionTree.class), 
		FOR_FUNCTION(ForFunctionTree.class), 
		CAST_FUNCTION(CastFunctionTree.class), 
		CASE_FUNCTION(CaseFunctionTree.class), 
		WHEN_CLAUSE_EXPRESSION(WhenClauseExpressionTree.class), 
		SELECT_FUNCTION(SelectFunctionTree.class), 
		WHERE_CLAUSE(WhenClauseTree.class), 
		SELECT_CLAUSE(SelectClauseTree.class), 
		ALIASED_FIELD_REFERENCE(AliasedFieldReferenceTree.class), 
		FROM_CLAUSE_EXPRESSION(FromClauseTree.class), 
		ROW_CONSTRUCTOR_FUNCTION(RowConstructorFunctionTree.class), 
		ALIASED_EXPRESSION(AliasedExpressionTree.class), 
		PASSTHRU_FUNCTION(PassthruFunctionTree.class),
		STATEMENTS(StatementsTree.class);

		final Class<? extends Tree> associatedInterface;

		Kind(Class<? extends Tree> associatedInterface) {
			this.associatedInterface = associatedInterface;
		}

		public Class<? extends Tree> getAssociatedInterface() {
			return associatedInterface;
		}
		
	    @Override
	    public boolean contains(Kinds other) {
	      return this.equals(other);
	    }

	}
}
