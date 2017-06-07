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
package com.exxeta.iss.sonar.esql.parser;

import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.COLON;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.COMMA;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.DOT;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.LPARENTHESIS;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.RPARENTHESIS;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.SEMI;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.PathClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.VariableReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ComplexFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.DateTimeFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FieldFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ListFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.MiscellaneousFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.NumericFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.StringManipulationFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.lexer.EsqlTokenType;
import com.exxeta.iss.sonar.esql.tree.expression.LikeExpressionTreeImpl;
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
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementNameTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementNamespaceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ProgramTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IntervalExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ParenthesisedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AliasedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AliasedFieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AsbitstreamFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.CaseFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.CastFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.ExtractFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.ForFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.FromClauseExpressionTreeImpl;
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
import com.exxeta.iss.sonar.esql.tree.impl.statement.StatementsTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ThrowStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.UpdateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ValuesClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.WhenClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.WhileStatementTreeImpl;
import com.sonar.sslr.api.typed.GrammarBuilder;

public class EsqlGrammar {
	private final GrammarBuilder<InternalSyntaxToken> b;
	private final TreeFactory f;

	public EsqlGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
		this.b = b;
		this.f = f;
	}

	public ProgramTree PROGRAM() {

		return b.<ProgramTreeImpl>nonterminal(EsqlLegacyGrammar.PROGRAM)
				.is(f.program(b.optional(BROKER_SCHEMA_STATEMENT()), b.optional(PATH_CLAUSE()),
						b.optional(b.token(SEMI)), ESQL_CONTENTS(), 
						b.token(EsqlLegacyGrammar.SPACING_NOT_SKIPPED),
						b.token(EsqlLegacyGrammar.EOF)));

	}

	public EsqlContentsTreeImpl ESQL_CONTENTS() {
		return b.<EsqlContentsTreeImpl>nonterminal(Kind.ESQL_CONTENTS)
				.is(f.esqlContents(b.zeroOrMore(b.firstOf(CREATE_FUNCTION_STATEMENT(), CREATE_MODULE_STATEMENT(),
						CREATE_PROCEDURE_STATEMENT(), DECLARE_STATEMENT()))));
	}

	public CreateFunctionStatementTreeImpl CREATE_FUNCTION_STATEMENT() {
		return b.<CreateFunctionStatementTreeImpl>nonterminal(Kind.CREATE_FUNCTION_STATEMENT)
				.is(f.createFunctionStatement(b.token(EsqlNonReservedKeyword.CREATE),
						b.token(EsqlNonReservedKeyword.FUNCTION), BINDING_IDENTIFIER(),
						b.token(EsqlPunctuator.LPARENTHESIS), b.optional(PARAMETER()),
						b.zeroOrMore(f.newTuple22(b.token(EsqlPunctuator.COMMA), PARAMETER())),
						b.token(EsqlPunctuator.RPARENTHESIS), b.optional(RETURN_TYPE()), b.optional(LANGUAGE()),
						b.optional(RESULT_SET()), ROUTINE_BODY()

		));

	}

	public IdentifierTree IDENTIFIER_REFERENCE() {
		return b.<IdentifierTree>nonterminal(EsqlLegacyGrammar.IDENTIFIER_REFERENCE)
				.is(f.identifierReference(b.token(EsqlTokenType.IDENTIFIER)));
	}

	public IdentifierTree BINDING_IDENTIFIER() {
		    return b.<IdentifierTree>nonterminal(EsqlLegacyGrammar.BINDING_IDENTIFIER)
		      .is(f.bindingIdentifier(b.token(EsqlTokenType.IDENTIFIER))
		      );
	}

	public IdentifierTree IDENTIFIER_NAME() {
	    return b.<IdentifierTree>nonterminal()
	      .is(f.identifierName(b.token(EsqlLegacyGrammar.IDENTIFIER_NAME)));
	}
	public ResultSetTreeImpl RESULT_SET() {
		return b.<ResultSetTreeImpl>nonterminal(Kind.RESULT_SET)
				.is(f.resultSet(b.token(EsqlNonReservedKeyword.DYNAMIC), b.token(EsqlNonReservedKeyword.RESULT),
						b.token(EsqlNonReservedKeyword.SETS), b.token(EsqlLegacyGrammar.NUMERIC_LITERAL)));

	}

	public LanguageTreeImpl LANGUAGE() {
		return b.<LanguageTreeImpl>nonterminal(Kind.LANGUAGE)
				.is(f.language(b.token(EsqlNonReservedKeyword.LANGUAGE),
						b.firstOf(b.token(EsqlNonReservedKeyword.ESQL), b.token(EsqlNonReservedKeyword.DATABASE),
								b.token(EsqlNonReservedKeyword.DOT_NET), b.token(EsqlNonReservedKeyword.CLR),
								b.token(EsqlNonReservedKeyword.JAVA))));

	}

	public ReturnTypeTreeImpl RETURN_TYPE() {
		return b.<ReturnTypeTreeImpl>nonterminal(Kind.RETURN_TYPE).is(f.returnType(
				b.token(EsqlNonReservedKeyword.RETURNS), DATA_TYPE(),
				b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.NULLABLE),
						f.newTuple7(b.token(EsqlNonReservedKeyword.NOT), b.token(EsqlNonReservedKeyword.NULL))))));
	}

	public ParameterTreeImpl PARAMETER() {
		return b.<ParameterTreeImpl>nonterminal(Kind.PARAMETER).is(f.parameter(
				b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.IN), b.token(EsqlNonReservedKeyword.OUT),
						b.token(EsqlNonReservedKeyword.INOUT))),
				BINDING_IDENTIFIER(),
				b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.NAME), b.token(EsqlNonReservedKeyword.NAMESPACE),
						f.newTuple8(b.optional(b.token(EsqlNonReservedKeyword.CONSTANT)),
								DATA_TYPE())))));
	}

	public RoutineBodyTreeImpl ROUTINE_BODY() {
		return b.<RoutineBodyTreeImpl>nonterminal(Kind.ROUTINE_BODY)
				.is(f.routineBody(b.firstOf(STATEMENT(), EXTERNAL_ROUTINE_BODY())));
	}

	public ExternalRoutineBodyTreeImpl EXTERNAL_ROUTINE_BODY() {
		return b.<ExternalRoutineBodyTreeImpl>nonterminal(Kind.EXTERNAL_ROUTINE_BODY)
				.is(f.externalRoutineBody(b.token(EsqlNonReservedKeyword.EXTERNAL),
						b.token(EsqlNonReservedKeyword.NAME), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME_WITH_QUOTES),
						b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public CreateProcedureStatementTree CREATE_PROCEDURE_STATEMENT() {
		return b.<CreateProcedureStatementTree>nonterminal(Kind.CREATE_PROCEDURE_STATEMENT)
				.is(f.createProcedureStatement(b.token(EsqlNonReservedKeyword.CREATE),
						b.token(EsqlNonReservedKeyword.PROCEDURE), BINDING_IDENTIFIER(),
						b.token(EsqlPunctuator.LPARENTHESIS), b.optional(PARAMETER()),
						b.zeroOrMore(f.newTuple10(b.token(EsqlPunctuator.COMMA), PARAMETER())),
						b.token(EsqlPunctuator.RPARENTHESIS), b.optional(RETURN_TYPE()), b.optional(LANGUAGE()),
						b.optional(RESULT_SET()), ROUTINE_BODY()

		));
	}

	public CreateModuleStatementTreeImpl CREATE_MODULE_STATEMENT() {
		return b.<CreateModuleStatementTreeImpl>nonterminal(Kind.CREATE_MODULE_STATEMENT)
				.is(f.createModuleStatement(b.token(EsqlNonReservedKeyword.CREATE),
						b.firstOf(b.token(EsqlNonReservedKeyword.COMPUTE), b.token(EsqlNonReservedKeyword.DATABASE),
								b.token(EsqlNonReservedKeyword.FILTER)),
						b.token(EsqlNonReservedKeyword.MODULE), BINDING_IDENTIFIER(),
						b.zeroOrMore(b.firstOf(ROUTINE_DECLARATION(), DECLARE_STATEMENT())),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.MODULE),
						b.token(EsqlLegacyGrammar.EOS)));

	}

	public RoutineDeclarationTree ROUTINE_DECLARATION() {
		return b.<RoutineDeclarationTree>nonterminal(Kind.ROUTINE_DECLARATION)
				.is(b.firstOf(CREATE_FUNCTION_STATEMENT(), CREATE_PROCEDURE_STATEMENT()));

	}

	public BrokerSchemaStatementTreeImpl BROKER_SCHEMA_STATEMENT() {
		return b.<BrokerSchemaStatementTreeImpl>nonterminal(Kind.BROKER_SCHEMA_STATEMENT)
				.is(f.brokerSchema(b.token(EsqlNonReservedKeyword.BROKER), b.token(EsqlNonReservedKeyword.SCHEMA),
						SCHEMA_NAME()));

	}

	public PathClauseTree PATH_CLAUSE() {
		return b.<PathClauseTree>nonterminal(Kind.PATH_CLAUSE)
				.is(f.pathClause(b.token(EsqlNonReservedKeyword.PATH), SCHEMA_NAME(),
						b.zeroOrMore(f.newTuple4(b.token(COMMA), SCHEMA_NAME()))));
	}

	public SchemaNameTree SCHEMA_NAME() {
		return b.<SchemaNameTree>nonterminal(Kind.SCHEMA_NAME).is(f.schemaName(b.token(EsqlLegacyGrammar.IDENTIFIER_NAME),
				b.zeroOrMore(f.newTuple5(b.token(DOT), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME)))));
	}

	public StatementTree STATEMENT() {
		return b.<StatementTree>nonterminal(Kind.STATEMENT).is(b.firstOf(
				BASIC_STATEMENT() , MESSAGE_TREE_MANIPULATION_STATEMENT(),
				NODE_INTERACTION_STATEMENT() , DATABASE_UPDATE_STATEMENT(),
				OTHER_STATEMENT()));
	}

	public StatementsTreeImpl STATEMENTS() {
		return b.<StatementsTreeImpl>nonterminal(Kind.STATEMENTS).is(
				f.statements(b.zeroOrMore(STATEMENT()))
		);
	}
	
	private FunctionTree FUNCTION() {
		return b.firstOf(/*DATABASE_STATE_FUNCTION(),*/DATETIME_FUNCTION(),NUMERIC_FUNCTION(), STRING_MANIPULATION_FUNCTION(), FIELD_FUNCTION(), LIST_FUNCTION(), COMPLEX_FUNCTION(), MISCELLANEOUS_FUNCTION());
	}
	
	private DateTimeFunctionTree DATETIME_FUNCTION() {
		return EXTRACT_FUNCTION();
	}
	
	private FieldFunctionTree FIELD_FUNCTION() {
		return b.firstOf(ASBITSTREAM_FUNCTION(), FOR_FUNCTION());
	}
	
	private ListFunctionTree LIST_FUNCTION() {
		return THE_FUNCTION();
	}
	
	private ComplexFunctionTree COMPLEX_FUNCTION(){
		return b.firstOf(CAST_FUNCTION(), CASE_FUNCTION(), SELECT_FUNCTION(), ROW_CONSTRUCTOR_FUNCTION());
	}	

	private MiscellaneousFunctionTree MISCELLANEOUS_FUNCTION(){
		return b.firstOf(PASSTHRU_FUNCTION());
	}
	
	private NumericFunctionTree NUMERIC_FUNCTION(){
		return ROUND_FUNCTION();
	}
	
	private StringManipulationFunctionTree STRING_MANIPULATION_FUNCTION(){
		return b.firstOf(OVERLAY_FUNCTION(), POSITION_FUNCTION(), SUBSTRING_FUNCTION(), TRIM_FUNCTION());
	}

	public ExtractFunctionTreeImpl EXTRACT_FUNCTION() {
		return b.<ExtractFunctionTreeImpl>nonterminal(Kind.EXTRACT_FUNCTION).is(f.extractFunction(
				b.token(EsqlNonReservedKeyword.EXTRACT), b.token(EsqlPunctuator.LPARENTHESIS),
				b.firstOf(b.token(EsqlNonReservedKeyword.DAYS), b.token(EsqlNonReservedKeyword.DAYOFYEAR),
						b.token(EsqlNonReservedKeyword.DAYOFWEEK), b.token(EsqlNonReservedKeyword.YEAR),
						b.token(EsqlNonReservedKeyword.MONTH), b.token(EsqlNonReservedKeyword.DAY),
						b.token(EsqlNonReservedKeyword.HOUR), b.token(EsqlNonReservedKeyword.MINUTE),
						b.token(EsqlNonReservedKeyword.SECOND), b.token(EsqlNonReservedKeyword.MONTHS),
						b.token(EsqlNonReservedKeyword.QUARTER), b.token(EsqlNonReservedKeyword.QUARTERS),
						b.token(EsqlNonReservedKeyword.WEEKS), b.token(EsqlNonReservedKeyword.WEEKOFYEAR),
						b.token(EsqlNonReservedKeyword.WEEKOFMONTH), b.token(EsqlNonReservedKeyword.ISLEAPYEAR)),
				b.token(EsqlReservedKeyword.FROM), b.firstOf(DATE_LITERAL(), EXPRESSION()), b.token(EsqlPunctuator.RPARENTHESIS)));
	}
	
	public AsbitstreamFunctionTreeImpl ASBITSTREAM_FUNCTION() {
		return b.<AsbitstreamFunctionTreeImpl>nonterminal(Kind.ASBITSTREAM_FUNCTION).is(f.asbitstreamFunction(
				b.token(EsqlNonReservedKeyword.ASBITSTREAM), b.token(EsqlPunctuator.LPARENTHESIS),
				FIELD_REFERENCE(),b.optional(b.firstOf( 
					b.oneOrMore(f.newTuple106(b.token(EsqlPunctuator.COMMA), b.optional(CALL_EXPRESSION()))),
					b.oneOrMore(f.newTuple107(b.firstOf(
						b.token(EsqlNonReservedKeyword.OPTIONS), 
						b.token(EsqlNonReservedKeyword.ENCODING), 
						b.token(EsqlNonReservedKeyword.CCSID), 
						b.token(EsqlNonReservedKeyword.SET), 
						b.token(EsqlNonReservedKeyword.TYPE), 
						b.token(EsqlNonReservedKeyword.FORMAT)
					), b.optional(CALL_EXPRESSION())))
					  )),
				b.token(EsqlPunctuator.RPARENTHESIS)
				
		));
	}
	
	public ForFunctionTreeImpl FOR_FUNCTION() {
		return b.<ForFunctionTreeImpl>nonterminal(Kind.FOR_FUNCTION).is(f.forFunction(
				b.token(EsqlNonReservedKeyword.FOR), b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.ALL),b.token(EsqlNonReservedKeyword.SOME),b.token(EsqlNonReservedKeyword.ANY))),
				FIELD_REFERENCE(), b.optional(f.newTuple108(b.token(EsqlNonReservedKeyword.AS), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME))), //TODO use AliasedFieldReference
				b.token(EsqlPunctuator.LPARENTHESIS), EXPRESSION(), b.token(EsqlPunctuator.RPARENTHESIS) 
				));
	}

	public TheFunctionTreeImpl THE_FUNCTION() {
		return b.<TheFunctionTreeImpl>nonterminal(Kind.THE_FUNCTION)
				.is(f.theFunction(b.token(EsqlNonReservedKeyword.THE), b.token(EsqlPunctuator.LPARENTHESIS),
						EXPRESSION(), b.token(EsqlPunctuator.RPARENTHESIS)));
	}
	
	public RoundFunctionTreeImpl ROUND_FUNCTION() {
		return b.<RoundFunctionTreeImpl>nonterminal(Kind.ROUND_FUNCTION).is(f.roundFunction(
				b.token(EsqlNonReservedKeyword.ROUND), b.token(EsqlPunctuator.LPARENTHESIS), EXPRESSION(),
				b.token(EsqlPunctuator.COMMA), EXPRESSION(),
				b.optional(f.newTuple99(b.token(EsqlNonReservedKeyword.MODE), b.firstOf(
						b.token(EsqlNonReservedKeyword.ROUND_UP), b.token(EsqlNonReservedKeyword.ROUND_DOWN),
						b.token(EsqlNonReservedKeyword.ROUND_CEILING), b.token(EsqlNonReservedKeyword.ROUND_FLOOR),
						b.token(EsqlNonReservedKeyword.ROUND_HALF_UP), b.token(EsqlNonReservedKeyword.ROUND_HALF_EVEN),
						b.token(EsqlNonReservedKeyword.ROUND_HALF_DOWN)))),
				b.token(EsqlPunctuator.RPARENTHESIS)));
	}
	
	public OverlayFunctionTreeImpl OVERLAY_FUNCTION(){
		return b.<OverlayFunctionTreeImpl>nonterminal(Kind.OVERLAY_FUNCTION).is(
			f.overlayFunction(b.token(EsqlNonReservedKeyword.OVERLAY), b.token(EsqlPunctuator.LPARENTHESIS),
					EXPRESSION(), b.token(EsqlNonReservedKeyword.PLACING), EXPRESSION(),  b.token(EsqlReservedKeyword.FROM), EXPRESSION(),
					b.optional(f.newTuple100(b.token(EsqlNonReservedKeyword.FOR) , EXPRESSION())), b.token(EsqlPunctuator.RPARENTHESIS)
					));
	}

	public PositionFunctionTreeImpl POSITION_FUNCTION(){
		return b.<PositionFunctionTreeImpl>nonterminal(Kind.POSITION_FUNCTION).is(
			f.positionFunction(b.token(EsqlNonReservedKeyword.POSITION), b.token(EsqlPunctuator.LPARENTHESIS),
					CALL_EXPRESSION(), b.token(EsqlNonReservedKeyword.IN), CALL_EXPRESSION(),  b.optional(f.newTuple101(b.token(EsqlReservedKeyword.FROM), EXPRESSION())),
					b.optional(f.newTuple102(b.token(EsqlNonReservedKeyword.REPEAT) , EXPRESSION())), b.token(EsqlPunctuator.RPARENTHESIS)
					));
	}
	
	public SubstringFunctionTreeImpl SUBSTRING_FUNCTION(){
		return b.<SubstringFunctionTreeImpl>nonterminal(Kind.SUBSTRING_FUNCTION).is(
			f.substringFunction(b.token(EsqlNonReservedKeyword.SUBSTRING), b.token(EsqlPunctuator.LPARENTHESIS),
					EXPRESSION(), b.firstOf(b.token(EsqlNonReservedKeyword.FROM), b.token(EsqlNonReservedKeyword.BEFORE), b.token(EsqlNonReservedKeyword.AFTER)),
					EXPRESSION(), b.optional(f.newTuple103( b.token(EsqlNonReservedKeyword.FOR), EXPRESSION())),
			b.token(EsqlPunctuator.RPARENTHESIS)
		));
	}

	public TrimFunctionTreeImpl TRIM_FUNCTION() {
		return b.<TrimFunctionTreeImpl>nonterminal(Kind.TRIM_FUNCTION)
				.is(f.trimFunction(b.token(EsqlNonReservedKeyword.TRIM), b.token(EsqlPunctuator.LPARENTHESIS),
						b.optional(f.newTuple105(b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.BOTH),
								b.token(EsqlNonReservedKeyword.LEADING), b.token(EsqlNonReservedKeyword.TRAILING))),
								b.firstOf(b.token(EsqlNonReservedKeyword.FROM),f.newTuple104(EXPRESSION(), b.token(EsqlNonReservedKeyword.FROM)))
								)),
						CALL_EXPRESSION(), b.token(EsqlPunctuator.RPARENTHESIS)));
	}

	private StatementTree OTHER_STATEMENT() {
		return b.firstOf(DECLARE_HANDLER_STATEMENT(),EVAL_STATEMENT(), LOG_STATEMENT(), RESIGNAL_STATEMENT()); 
	}

	private StatementTree DATABASE_UPDATE_STATEMENT() {
		return b.firstOf(DELETE_FROM_STATEMENT(), INSERT_STATEMENT(), PASSTHRU_STATEMENT(), UPDATE_STATEMENT());
	}

	private StatementTree NODE_INTERACTION_STATEMENT() {
		return PROPAGATE_STATEMENT();
	}

	private StatementTree MESSAGE_TREE_MANIPULATION_STATEMENT() {
		return b.firstOf(ATTACH_STATEMENT(), CREATE_STATEMENT(), DELETE_STATEMENT(), DETACH_STATEMENT(), FOR_STATEMENT(),MOVE_STATEMENT());
	}


	private StatementTree BASIC_STATEMENT() {
		return b.firstOf(BEGIN_END_STATEMENT(), CALL_STATEMENT(), CASE_STATEMENT(), DECLARE_STATEMENT(), IF_STATEMENT(), 
				ITERATE_STATEMENT(), LEAVE_STATEMENT(), LOOP_STATEMENT(), REPEAT_STATEMENT(), RETURN_STATEMENT(), SET_STATEMENT(), 
				THROW_STATEMENT(), WHILE_STATEMENT());
	}

	public BeginEndStatementTreeImpl BEGIN_END_STATEMENT() {
		return b.<BeginEndStatementTreeImpl>nonterminal(Kind.BEGIN_END_STATEMENT)
				.is(f.beginEndStatement(
						b.optional(f.newTuple18(LABEL(), b.token(EsqlPunctuator.COLON))),
						b.token(EsqlNonReservedKeyword.BEGIN),
						b.optional(f.newTuple19(b.optional(b.token(EsqlNonReservedKeyword.NOT)),
								b.token(EsqlNonReservedKeyword.ATOMIC))),
						STATEMENTS(), b.token(EsqlNonReservedKeyword.END),
						b.optional(LABEL()), b.token(EsqlLegacyGrammar.EOS)));
	}
	public LabelTreeImpl LABEL(){
		return b.<LabelTreeImpl>nonterminal(Kind.LABEL).is(f.label(IDENTIFIER_NAME()));
	}
	
	public CallStatementTreeImpl CALL_STATEMENT() {
		return b.<CallStatementTreeImpl>nonterminal(Kind.CALL_STATEMENT)
				.is(f.callStatement(
						b.token(EsqlNonReservedKeyword.CALL), 
						SCHEMA_NAME(),
						b.token(LPARENTHESIS),b.optional(f.newTuple45(EXPRESSION(),b.zeroOrMore(f.newTuple46(b.token(COMMA),EXPRESSION())))),
						b.token(RPARENTHESIS), 
						b.optional(b.firstOf(
								f.newTuple47(b.token(EsqlNonReservedKeyword.IN), FIELD_REFERENCE()),
								f.newTriple4(b.token(EsqlNonReservedKeyword.EXTERNAL),b.token(EsqlNonReservedKeyword.SCHEMA), CALL_EXPRESSION())
						)),
						b.optional(f.newTuple48(b.token(EsqlNonReservedKeyword.INTO), FIELD_REFERENCE())),
						b.token(EsqlLegacyGrammar.EOS)
						
				));
	}

	public CaseStatementTreeImpl CASE_STATEMENT() {
		return b.<CaseStatementTreeImpl>nonterminal(Kind.CASE_STATEMENT)
				.is(f.caseStatement(b.token(EsqlReservedKeyword.CASE), b.firstOf(b.oneOrMore(WHEN_CLAUSE()), f.newTuple61(EXPRESSION(), b.oneOrMore(WHEN_CLAUSE()))), b.optional(f.newTuple60(b.token(EsqlNonReservedKeyword.ELSE), STATEMENTS())),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlReservedKeyword.CASE), b.token(EsqlLegacyGrammar.EOS)));
	}
	
	public WhenClauseTreeImpl WHEN_CLAUSE(){
		return b.<WhenClauseTreeImpl>nonterminal(Kind.WHEN_CLAUSE)
				.is(f.whenClause(b.token(EsqlReservedKeyword.WHEN), EXPRESSION(), b.token(EsqlNonReservedKeyword.THEN), STATEMENTS()));
	}

	public DeclareStatementTreeImpl DECLARE_STATEMENT() {
		return b.<DeclareStatementTreeImpl>nonterminal(Kind.DECLARE_STATEMENT)
				.is(f.declareStatement(
						b.token(EsqlNonReservedKeyword.DECLARE), BINDING_IDENTIFIER(),
						b.zeroOrMore(f.newTuple42(b.token(COMMA),BINDING_IDENTIFIER())),
						b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.SHARED),b.token(EsqlNonReservedKeyword.EXTERNAL))),
						b.firstOf(f.newTuple43(b.optional(b.token(EsqlNonReservedKeyword.CONSTANT)),  DATA_TYPE()),b.token(EsqlNonReservedKeyword.NAMESPACE), b.token(EsqlNonReservedKeyword.NAME)),
						b.optional(EXPRESSION()), b.token(EsqlLegacyGrammar.EOS)
				));

		// rule(declareStatement).is("DECLARE", NAME,
		// b.zeroOrMore(b.sequence(",", NAME)),
		// b.optional(b.firstOf("SHARED", "EXTERNAL")),
		// b.firstOf(b.sequence(b.optional("CONSTANT"), b.firstOf(dateLiteral,
		// timeLiteral, dataType)),
		// "NAMESPACE", "NAME"),
		// b.optional(expression));
	}

	public IfStatementTreeImpl IF_STATEMENT() {
		return b.<IfStatementTreeImpl>nonterminal(Kind.IF_STATEMENT).is(

				f.ifStatement(b.token(EsqlNonReservedKeyword.IF), EXPRESSION(), b.token(EsqlNonReservedKeyword.THEN),
						STATEMENTS(), b.zeroOrMore(ELSEIF_CLAUSE()), b.optional(ELSE_CLAUSE()),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.IF),
						b.token(EsqlLegacyGrammar.EOS)));
	}

	public ElseClauseTreeImpl ELSE_CLAUSE() {
		return b.<ElseClauseTreeImpl>nonterminal(Kind.ELSE_CLAUSE)
				.is(f.elseClause(b.token(EsqlNonReservedKeyword.ELSE), STATEMENTS()));
	}

	public ElseifClauseTreeImpl ELSEIF_CLAUSE() {
		return b.<ElseifClauseTreeImpl>nonterminal(Kind.ELSEIF_CLAUSE)
				.is(f.elseifClause(b.token(EsqlNonReservedKeyword.ELSEIF), EXPRESSION(),
						b.token(EsqlNonReservedKeyword.THEN), STATEMENTS()));
	}
	
	public IterateStatementTreeImpl ITERATE_STATEMENT(){
		return b.<IterateStatementTreeImpl>nonterminal(Kind.ITERATE_STATEMENT).is(f.iterateStatement(
				b.token(EsqlNonReservedKeyword.ITERATE), LABEL(), b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public LeaveStatementTreeImpl LEAVE_STATEMENT(){
		return b.<LeaveStatementTreeImpl>nonterminal(Kind.LEAVE_STATEMENT).is(f.leaveStatement(
				b.token(EsqlNonReservedKeyword.LEAVE), LABEL(), b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public LoopStatementTreeImpl LOOP_STATEMENT() {
		return b.<LoopStatementTreeImpl>nonterminal(Kind.LOOP_STATEMENT)
				.is(b.firstOf(LOOP_STATEMENT_WO_LABEL(), LOOP_STATEMENT_WITH_LABEL()));
	}

	public LoopStatementTreeImpl LOOP_STATEMENT_WO_LABEL() {
		return b.<LoopStatementTreeImpl>nonterminal()
				.is(f.loopStatementWoLabel(b.token(EsqlNonReservedKeyword.LOOP), STATEMENTS(),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.LOOP),
						b.token(EsqlLegacyGrammar.EOS)));
	}

	public LoopStatementTreeImpl LOOP_STATEMENT_WITH_LABEL() {
		return b.<LoopStatementTreeImpl>nonterminal()
				.is(f.loopStatementWithLabel(LABEL(), b.token(COLON), b.token(EsqlNonReservedKeyword.LOOP),
						STATEMENTS(), b.token(EsqlNonReservedKeyword.END),
						b.token(EsqlNonReservedKeyword.LOOP), b.optional(LABEL()), b.token(EsqlLegacyGrammar.EOS)

		));
	}
	
	public RepeatStatementTreeImpl REPEAT_STATEMENT() {
		return b.<RepeatStatementTreeImpl>nonterminal(Kind.REPEAT_STATEMENT)
				.is(b.firstOf(REPEAT_STATEMENT_WO_LABEL(), REPEAT_STATEMENT_WITH_LABEL()));
	}

	public RepeatStatementTreeImpl REPEAT_STATEMENT_WITH_LABEL() {
		return b.<RepeatStatementTreeImpl>nonterminal()
				.is(f.repeatStatementWithLabel(
						LABEL(), b.token(COLON), b.token(EsqlNonReservedKeyword.REPEAT), STATEMENTS(), 
						b.token(EsqlNonReservedKeyword.UNTIL), EXPRESSION(), b.token(EsqlNonReservedKeyword.END), 
						b.token(EsqlNonReservedKeyword.REPEAT), b.optional(LABEL()), b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public RepeatStatementTreeImpl REPEAT_STATEMENT_WO_LABEL() {
		return b.<RepeatStatementTreeImpl>nonterminal()
				.is(f.repeatStatementWoLabel(
						b.token(EsqlNonReservedKeyword.REPEAT), STATEMENTS(), 
						b.token(EsqlNonReservedKeyword.UNTIL), EXPRESSION(), b.token(EsqlNonReservedKeyword.END), 
						b.token(EsqlNonReservedKeyword.REPEAT), b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public ReturnStatementTreeImpl RETURN_STATEMENT() {
		return b.<ReturnStatementTreeImpl>nonterminal(Kind.RETURN_STATEMENT).is(
				f.returnStatement(
				b.token(EsqlNonReservedKeyword.RETURN), b.optional(EXPRESSION()), b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public VariableReferenceTree VARIABLE_REFERENCE(){
		return b.<VariableReferenceTree>nonterminal(Kind.VARIABLE_REFERENCE)
				.is(f.variableReference(b.firstOf(IDENTIFIER_REFERENCE(), FIELD_REFERENCE())));
	}
	
	public SetStatementTreeImpl SET_STATEMENT() {
		return b.<SetStatementTreeImpl>nonterminal(Kind.SET_STATEMENT).is(f.setStatement(
				b.token(EsqlNonReservedKeyword.SET), VARIABLE_REFERENCE(),
				b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.TYPE), b.token(EsqlNonReservedKeyword.NAMESPACE),
						b.token(EsqlNonReservedKeyword.NAME), b.token(EsqlNonReservedKeyword.VALUE))),
				b.token(EsqlPunctuator.EQUAL), EXPRESSION(), b.token(EsqlLegacyGrammar.EOS)));
	}
	
	public ThrowStatementTreeImpl THROW_STATEMENT() {
		return b.<ThrowStatementTreeImpl>nonterminal(Kind.THROW_STATEMENT).is(f.throwStatement(
				b.token(EsqlNonReservedKeyword.THROW), b.optional(b.token(EsqlNonReservedKeyword.USER)), b.token(EsqlNonReservedKeyword.EXCEPTION),
				b.optional(f.newTuple62(b.token(EsqlNonReservedKeyword.SEVERITY), EXPRESSION())),
				b.optional(f.newTuple63(b.token(EsqlNonReservedKeyword.CATALOG), EXPRESSION())), 
				b.optional(f.newTuple64(b.token(EsqlNonReservedKeyword.MESSAGE), EXPRESSION())),
				b.optional(f.newTuple65(b.token(EsqlNonReservedKeyword.VALUES), ARGUMENT_CLAUSE())),
				b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public WhileStatementTreeImpl WHILE_STATEMENT() {
		return b.<WhileStatementTreeImpl>nonterminal(Kind.WHILE_STATEMENT)
				.is(b.firstOf(WHILE_STATEMENT_WO_LABEL(), WHILE_STATEMENT_WITH_LABEL()));
	}

	public WhileStatementTreeImpl WHILE_STATEMENT_WITH_LABEL() {
		return b.<WhileStatementTreeImpl>nonterminal()
				.is(f.whileStatementWithLabel(
						LABEL(), b.token(COLON), b.token(EsqlNonReservedKeyword.WHILE), EXPRESSION(), 
						b.token(EsqlNonReservedKeyword.DO), STATEMENTS(), b.token(EsqlNonReservedKeyword.END), 
						b.token(EsqlNonReservedKeyword.WHILE), b.optional(LABEL()), b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public WhileStatementTreeImpl WHILE_STATEMENT_WO_LABEL() {
		return b.<WhileStatementTreeImpl>nonterminal()
				.is(f.whileStatementWoLabel(
						b.token(EsqlNonReservedKeyword.WHILE), EXPRESSION(), 
						b.token(EsqlNonReservedKeyword.DO), STATEMENTS(), b.token(EsqlNonReservedKeyword.END), 
						b.token(EsqlNonReservedKeyword.WHILE), b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public StatementTree PROPAGATE_STATEMENT() {
		return b.<PropagateStatementTreeImpl>nonterminal(Kind.PROPAGATE_STATEMENT)
				.is(f.propagateStatement(b.token(EsqlNonReservedKeyword.PROPAGATE),
						b.optional(f.newTuple23(b.token(EsqlNonReservedKeyword.TO),
								f.newTuple2(
										b.firstOf(b.token(EsqlNonReservedKeyword.TERMINAL),
												b.token(EsqlNonReservedKeyword.LABEL)),
										EXPRESSION()))),
						b.optional(MESSAGE_SOURCE()), b.optional(CONTROLS()), b.token(EsqlLegacyGrammar.EOS) ));

	}

	public MessageSourceTreeImpl MESSAGE_SOURCE() {
		return b.<MessageSourceTreeImpl>nonterminal(Kind.MESSAGE_SOURCE).is(f.messageSource(
				b.optional(f.newTuple16(b.token(EsqlNonReservedKeyword.ENVIRONMENT),
						EXPRESSION())),
				b.optional(
						f.newTuple12(b.token(EsqlNonReservedKeyword.MESSAGE), EXPRESSION())),
				b.optional(f.newTuple13(b.token(EsqlNonReservedKeyword.EXCEPTION),
						EXPRESSION()))));

	}

	public ControlsTreeImpl CONTROLS() {
		return b.<ControlsTreeImpl>nonterminal(Kind.CONTROLS)
				.is(f.controls(
						b.optional(f.newTuple26(b.token(EsqlNonReservedKeyword.FINALIZE),
								b.firstOf(b.token(EsqlNonReservedKeyword.DEFAULT),
										b.token(EsqlNonReservedKeyword.NONE)))),
						b.optional(f.newTuple28(b.token(EsqlNonReservedKeyword.DELETE), b.firstOf(
								b.token(EsqlNonReservedKeyword.DEFAULT), b.token(EsqlNonReservedKeyword.NONE))))));
	}

	public ExpressionTree ADDITIVE_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.additiveExpression)
				.is(f.newAdditive(MULTIPLICATIVE_EXPRESSION(),
						b.zeroOrMore(
								f.newTuple25(b.firstOf(b.token(EsqlPunctuator.PLUS), b.token(EsqlPunctuator.MINUS), b.token(EsqlPunctuator.CONCAT)),
										MULTIPLICATIVE_EXPRESSION()))));
	}

	public ExpressionTree MULTIPLICATIVE_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.multiplicativeExpression)
				.is(f.newMultiplicative(UNARY_EXPRESSION(),
						b.zeroOrMore(f.newTuple15(b.firstOf(b.token(EsqlPunctuator.STAR), b.token(EsqlPunctuator.DIV)),
								UNARY_EXPRESSION()))));
	}

	public ExpressionTree UNARY_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.unaryExpression)
				.is(b.firstOf(f.prefixExpression(b.firstOf(
						b.token(EsqlNonReservedKeyword.NOT),
						b.token(EsqlPunctuator.PLUS),
						b.token(EsqlPunctuator.MINUS)
						), UNARY_EXPRESSION()),
						LEFT_HAND_SIDE_EXPRESSION()));
	}

	public ExpressionTree LEFT_HAND_SIDE_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.leftHandSideExpression).is(b.firstOf(IN_EXPRESSION(), BETWEEN_EXPRESSION(), IS_EXPRESSION(), LIKE_EXPRESSION(), CALL_EXPRESSION()));
	}

	public ExpressionTree IN_EXPRESSION(){
		return b.<ExpressionTree>nonterminal(Kind.IN_EXPRESSION).is(f.inExpression(CALL_EXPRESSION(), b.optional(b.token(EsqlNonReservedKeyword.NOT)), b.token(EsqlNonReservedKeyword.IN), ARGUMENT_CLAUSE())
				);
	}

	public ExpressionTree IS_EXPRESSION(){
		return b.<ExpressionTree>nonterminal(Kind.IS_EXPRESSION).is(f.isExpression(CALL_EXPRESSION(), b.token(EsqlNonReservedKeyword.IS), b.optional(b.token(EsqlNonReservedKeyword.NOT)), 
				b.optional(b.firstOf(b.token(EsqlPunctuator.PLUS), b.token(EsqlPunctuator.MINUS))),
				b.firstOf(
				b.token(EsqlNonReservedKeyword.TRUE),
				b.token(EsqlNonReservedKeyword.FALSE),
				b.token(EsqlNonReservedKeyword.INF),
				b.token(EsqlNonReservedKeyword.INFINITY),
				b.token(EsqlNonReservedKeyword.NAN),
				b.token(EsqlNonReservedKeyword.NULL),
				b.token(EsqlNonReservedKeyword.NUM),
				b.token(EsqlNonReservedKeyword.NUMBER),
				b.token(EsqlNonReservedKeyword.UNKNOWN)
				
				))
				);
	}
	
	public ExpressionTree BETWEEN_EXPRESSION(){
		return b.<ExpressionTree>nonterminal(Kind.BETWEEN_EXPRESSION).is(f.betweenExpression(CALL_EXPRESSION(), b.optional(b.token(EsqlNonReservedKeyword.NOT)), 
				b.token(EsqlNonReservedKeyword.BETWEEN), b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.SYMMERTIC),b.token(EsqlNonReservedKeyword.ASYMMERTIC))), 
				CALL_EXPRESSION(), b.token(EsqlNonReservedKeyword.AND), CALL_EXPRESSION())
				);
	}
	
	public LikeExpressionTreeImpl LIKE_EXPRESSION(){
		return b.<LikeExpressionTreeImpl>nonterminal(Kind.LIKE_EXPRESSION).is (f.likeExpression(
				CALL_EXPRESSION(), b.optional(b.token(EsqlNonReservedKeyword.NOT)), b.token(EsqlNonReservedKeyword.LIKE),
				CALL_EXPRESSION(), b.optional(f.newTuple30(b.token(EsqlNonReservedKeyword.ESCAPE), CALL_EXPRESSION()))
		));
				
	}
	
	public ExpressionTree CALL_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(Kind.CALL_EXPRESSION).is(f.callExpression(
				b.firstOf(FUNCTION(), PRIMARY_EXPRESSION(), f.newTuple24(FIELD_REFERENCE(), ARGUMENT_CLAUSE()), VARIABLE_REFERENCE())));
	}

	public ParameterListTreeImpl ARGUMENT_CLAUSE() {
		return b.<ParameterListTreeImpl>nonterminal(Kind.ARGUMENTS)
				.is(f.argumentClause(b.token(EsqlPunctuator.LPARENTHESIS), b.optional(ARGUMENT_LIST()),
						b.token(EsqlPunctuator.RPARENTHESIS)));
	}

	public SeparatedList<Tree> ARGUMENT_LIST() {
		return b.<SeparatedList<Tree>>nonterminal()
				.is(f.argumentList(ARGUMENT(), b.zeroOrMore(f.newTuple17(b.token(EsqlPunctuator.COMMA), ARGUMENT()))));
	}

	public ExpressionTree ARGUMENT() {
		return b.<ExpressionTree>nonterminal().is(b.firstOf(EXPRESSION()));
	}

	public ExpressionTree RELATIONAL_EXPRESSION() {

		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.relationalExpression)
				.is(f.newRelational(ADDITIVE_EXPRESSION(),
						b.zeroOrMore(f.newTuple14(b.firstOf(b.token(EsqlPunctuator.LT), b.token(EsqlPunctuator.GT),
								b.token(EsqlPunctuator.LE), b.token(EsqlPunctuator.GE)

								), ADDITIVE_EXPRESSION()))));

		/*
		 * b.firstOf("<", ">", "<=", ">=", b.sequence("IS", b.optional("NOT")),
		 * b.sequence(b.optional("NOT"), "LIKE")), b.firstOf(additiveExpression,
		 * subtractiveExpression)))) .skipIfOneChild();
		 */

	}

	public ExpressionTree EQUALITY_EXPRESSION() {

		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.equalityExpression)
				.is(f.newEquality(RELATIONAL_EXPRESSION(),
						b.zeroOrMore(
								f.newTuple11(b.firstOf(b.token(EsqlPunctuator.EQUAL), b.token(EsqlPunctuator.NOTEQUAL)),
										RELATIONAL_EXPRESSION()))));

	}

	public ExpressionTree LOGICAL_AND_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(Kind.CONDITIONAL_AND).is(f.newConditionalAnd(EQUALITY_EXPRESSION(),
				b.zeroOrMore(f.newTuple6(b.token(EsqlNonReservedKeyword.AND), EQUALITY_EXPRESSION()))));
	}

	public ExpressionTree LOGICAL_OR_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(Kind.CONDITIONAL_OR).is(f.newConditionalOr(LOGICAL_AND_EXPRESSION(),
				b.zeroOrMore(f.newTuple1(b.token(EsqlNonReservedKeyword.OR), LOGICAL_AND_EXPRESSION()))));
	}

	public ExpressionTree EXPRESSION() {
		return b.<ExpressionTree>nonterminal(Kind.EXPRESSION).is(f.expression(LOGICAL_OR_EXPRESSION()));
	}
	public LiteralTreeImpl TIME_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.TIME_LITERAL)
				.is(f.timeLiteral(b.token(EsqlLegacyGrammar.TIME_LITERAL)));
	}

	public LiteralTreeImpl TIMESTAMP_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.TIMESTAMP_LITERAL)
				.is(f.timestampLiteral(b.token(EsqlLegacyGrammar.TIMESTAMP_LITERAL)));
	}

	public LiteralTreeImpl DATE_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.DATE_LITERAL)
				.is(f.dateLiteral(b.token(EsqlLegacyGrammar.DATE_LITERAL)));
	}

	public LiteralTreeImpl INTERVAL_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.INTERVAL_LITERAL)
				.is(f.intervalLiteral(b.token(EsqlLegacyGrammar.intervalLiteral)));
	}

	public LiteralTreeImpl HEX_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.HEX_LITERAL)
				.is(f.hexLiteral(b.token(EsqlLegacyGrammar.HEX_LITERAL)));
	}

	public LiteralTreeImpl NUMERIC_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.NUMERIC_LITERAL)
				.is(f.numericLiteral(b.token(EsqlLegacyGrammar.NUMERIC_LITERAL)));
	}

	public LiteralTreeImpl STRING_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.STRING_LITERAL)
				.is(f.stringLiteral(b.token(EsqlLegacyGrammar.STRING_LITERAL)));
	}

	public ExpressionTree PRIMARY_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.primaryExpression)
				.is(b.firstOf(INTERVAL_LITERAL(), LITERAL(), INTERVAL_EXPRESSION(),
						TIME_LITERAL(), DATE_LITERAL(), TIMESTAMP_LITERAL(), PARENTHESISED_EXPRESSION()));
	}

	public FieldReferenceTreeImpl FIELD_REFERENCE() {
		return b.<FieldReferenceTreeImpl>nonterminal(Kind.FIELD_REFERENCE)
				.is(f.fieldReference(b.firstOf(PATH_ELEMENT(), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME) ),
						b.zeroOrMore(f.newTuple21(b.token(EsqlPunctuator.DOT), PATH_ELEMENT()))));
	}

	public NamespaceTreeImpl NAMESPACE() {
		return b.<NamespaceTreeImpl>nonterminal(Kind.NAMESPACE).is(f.namespace(b.token(EsqlLegacyGrammar.IDENTIFIER_NAME)));
	}

	public PathElementTreeImpl PATH_ELEMENT() {
		return b.<PathElementTreeImpl>nonterminal(
				Kind.PATH_ELEMENT).is(
						f.finishPathElement(
								b.firstOf (
								f.pathElement(
										b.optional(PATH_ELEMENT_TYPE()),
										b.optional(PATH_ELEMENT_NAMESPACE()),
										PATH_ELEMENT_NAME(),
										b.optional(INDEX())
										
								),
								f.pathElement(INDEX()),
								f.pathElement(PATH_ELEMENT_TYPE())
								)
					));
	}
	
	public PathElementTypeTreeImpl PATH_ELEMENT_TYPE() {
		return b.<PathElementTypeTreeImpl>nonterminal(Kind.PATH_ELEMENT_TYPE).is(
				f.pathElementType(b.token(EsqlPunctuator.LPARENTHESIS),
					CALL_EXPRESSION(),	b.token(EsqlPunctuator.RPARENTHESIS))
				);	}
	
	public PathElementNamespaceTreeImpl PATH_ELEMENT_NAMESPACE(){
		return b.<PathElementNamespaceTreeImpl>nonterminal(Kind.PATH_ELEMENT_NAMESPACE)
				.is(f.pathElementNamespace(b.optional(b.firstOf(NAMESPACE(),
						f.newTriple2(b.token(EsqlPunctuator.LCURLYBRACE), EXPRESSION(),
								b.token(EsqlPunctuator.RCURLYBRACE)),
						b.token(EsqlPunctuator.STAR))), b.token(EsqlPunctuator.COLON)));
	}
	
	public PathElementNameTreeImpl PATH_ELEMENT_NAME(){
		return b.<PathElementNameTreeImpl>nonterminal(Kind.PATH_ELEMENT_NAME)
				.is(f.pathElementName(
						b.firstOf(b.token(EsqlLegacyGrammar.IDENTIFIER_NAME),
								f.newTriple3(b.token(EsqlPunctuator.LCURLYBRACE), CALL_EXPRESSION(),
										b.token(EsqlPunctuator.RCURLYBRACE)),
								b.token(EsqlPunctuator.STAR))
				));
	}

	public IndexTreeImpl INDEX() {
		return b.<IndexTreeImpl>nonterminal(Kind.INDEX)
				.is(f.index(b.token(EsqlPunctuator.LBRACKET),
						f.newTuple32(b.optional(b.firstOf(b.token(EsqlPunctuator.LT), b.token(EsqlPunctuator.GT))),
								b.optional(b.firstOf(EXPRESSION(), NUMERIC_LITERAL()))),
						b.token(EsqlPunctuator.RBRACKET)));
	}

	public IntervalExpressionTreeImpl INTERVAL_EXPRESSION() {

		return b.<IntervalExpressionTreeImpl>nonterminal(Kind.INTERVAL_EXPRESSION)
				.is(f.intervalExpression(b.token(EsqlPunctuator.LPARENTHESIS), ADDITIVE_EXPRESSION(),
						b.token(EsqlPunctuator.RPARENTHESIS), INTERVAL_QUALIFIER()));

	}

	public LiteralTreeImpl LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(EsqlLegacyGrammar.LITERAL)
				.is(b.firstOf(f.nullLiteral(b.token(EsqlNonReservedKeyword.NULL)),
						f.booleanLiteral(
								b.firstOf(b.token(EsqlNonReservedKeyword.TRUE), b.token(EsqlNonReservedKeyword.FALSE))),
						NUMERIC_LITERAL(), HEX_LITERAL(), STRING_LITERAL()));
	}

	public ParenthesisedExpressionTreeImpl PARENTHESISED_EXPRESSION() {
		return b.<ParenthesisedExpressionTreeImpl>nonterminal(Kind.PARENTHESISED_EXPRESSION)
				.is(f.parenthesisedExpression(b.token(EsqlPunctuator.LPARENTHESIS), EXPRESSION(),
						b.token(EsqlPunctuator.RPARENTHESIS)));
	}

	public DataTypeTreeImpl DATA_TYPE() {
		return b.<DataTypeTreeImpl>nonterminal(EsqlLegacyGrammar.dataType)
				.is(f.dataType(b.firstOf(b.token(EsqlNonReservedKeyword.BOOLEAN), b.token(EsqlNonReservedKeyword.INT),
						b.token(EsqlNonReservedKeyword.INTEGER), b.token(EsqlNonReservedKeyword.FLOAT),
						DECIMAL_DATA_TYPE(), b.token(EsqlNonReservedKeyword.DATE), b.token(EsqlNonReservedKeyword.TIME),
						b.token(EsqlNonReservedKeyword.TIMESTAMP), b.token(EsqlNonReservedKeyword.GMTTIME),
						b.token(EsqlNonReservedKeyword.GMTTIMESTAMP), INTERVAL_DATA_TYPE(),
						b.token(EsqlNonReservedKeyword.CHARACTER), b.token(EsqlNonReservedKeyword.CHAR),
						b.token(EsqlNonReservedKeyword.BLOB), b.token(EsqlNonReservedKeyword.BIT),
						b.token(EsqlNonReservedKeyword.ROW), f.newTuple33(b.token(EsqlNonReservedKeyword.REFERENCE),
								b.optional(b.token(EsqlNonReservedKeyword.TO))))));

	}

	public IntervalDataTypeTreeImpl INTERVAL_DATA_TYPE() {
		return b.<IntervalDataTypeTreeImpl>nonterminal(Kind.INTERVAL_DATA_TYPE)
				.is(f.intervalDataType(b.token(EsqlNonReservedKeyword.INTERVAL), b.optional(INTERVAL_QUALIFIER())));

	}

	public IntervalQualifierTreeImpl INTERVAL_QUALIFIER() {
		return b.<IntervalQualifierTreeImpl>nonterminal(EsqlLegacyGrammar.intervalQualifier)
				.is(f.intervalQualifier(b.firstOf(
						f.newTuple35(b.token(EsqlNonReservedKeyword.YEAR),
								b.optional(f.newTuple34(b.token(EsqlNonReservedKeyword.TO),
										b.token(EsqlNonReservedKeyword.MONTH)))),
						b.token(EsqlNonReservedKeyword.MONTH),
						f.newTuple37(b.token(EsqlNonReservedKeyword.DAY), b.optional(f.newTuple36(
								b.token(EsqlNonReservedKeyword.TO), b.firstOf(b.token(EsqlNonReservedKeyword.HOUR),
										b.token(EsqlNonReservedKeyword.MINUTE), b
												.token(EsqlNonReservedKeyword.SECOND))))),
						f.newTuple38(b.token(EsqlNonReservedKeyword.HOUR),
								b.optional(f.newTuple39(b.token(EsqlNonReservedKeyword.TO),
										b.firstOf(b.token(EsqlNonReservedKeyword.MINUTE),
												b.token(EsqlNonReservedKeyword.SECOND))))),
						f.newTuple41(b.token(EsqlNonReservedKeyword.MINUTE),
								b.optional(f.newTuple40(b.token(EsqlNonReservedKeyword.TO),
										b.token(EsqlNonReservedKeyword.SECOND)))),
						b.token(EsqlNonReservedKeyword.SECOND))));
	}

	public DecimalDataTypeTreeImpl DECIMAL_DATA_TYPE() {
		return b.<DecimalDataTypeTreeImpl>nonterminal(Kind.DECIMAL_DATA_TYPE)
				.is(f.decimalDataType(b.token(EsqlNonReservedKeyword.DECIMAL),
						b.optional(f.decimalSize(b.token(EsqlPunctuator.LPARENTHESIS),
								b.token(EsqlLegacyGrammar.NUMERIC_LITERAL), b.token(EsqlPunctuator.COMMA),
								b.token(EsqlLegacyGrammar.NUMERIC_LITERAL), b.token(EsqlPunctuator.RPARENTHESIS)))));
	}
	

	public AttachStatementTreeImpl ATTACH_STATEMENT() {
		return b.<AttachStatementTreeImpl>nonterminal(Kind.ATTACH_STATEMENT).is(f.attachStatement(
				b.token(EsqlNonReservedKeyword.ATTACH), FIELD_REFERENCE(), b.token(EsqlNonReservedKeyword.TO), FIELD_REFERENCE(),
						b.token(EsqlNonReservedKeyword.AS), 
						b.firstOf(
								b.token(EsqlNonReservedKeyword.FIRSTCHILD),
								b.token(EsqlNonReservedKeyword.LASTCHILD),
								b.token(EsqlNonReservedKeyword.PREVIOUSSIBLING),
								b.token(EsqlNonReservedKeyword.NEXTSIBLING)),
						b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public CreateStatementTreeImpl CREATE_STATEMENT()  {
		return b.<CreateStatementTreeImpl>nonterminal(Kind.CREATE_STATEMENT).is(f.createStatement(
				b.token(EsqlNonReservedKeyword.CREATE), b.firstOf(b.token(EsqlNonReservedKeyword.FIELD), 
						f.newTuple66(b.firstOf(b.token(EsqlNonReservedKeyword.PREVIOUSSIBLING),b.token(EsqlNonReservedKeyword.NEXTSIBLING),b.token(EsqlNonReservedKeyword.FIRSTCHILD),b.token(EsqlNonReservedKeyword.LASTCHILD)),b.token(EsqlNonReservedKeyword.OF))),
				FIELD_REFERENCE(),
				b.optional(f.newTuple67(b.token(EsqlNonReservedKeyword.AS), FIELD_REFERENCE())),
				b.optional(f.newTuple68(b.token(EsqlNonReservedKeyword.DOMAIN), CALL_EXPRESSION())),
				b.optional(b.firstOf(REPEAT_CLAUSE(), FROM_CLAUSE(), PARSE_CLAUSE(), VALUES_CLAUSE())),
				b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public RepeatClauseTreeImpl REPEAT_CLAUSE() {
		return b.<RepeatClauseTreeImpl>nonterminal(Kind.REPEAT_CLAUSE). is(f.repeatClause(
				b.token(EsqlNonReservedKeyword.REPEAT), 
				b.optional(f.newTuple69(b.token(EsqlNonReservedKeyword.VALUE), EXPRESSION()))
				));
	}
	
	public ValuesClauseTreeImpl VALUES_CLAUSE() {
		return b.<ValuesClauseTreeImpl>nonterminal(Kind.VALUES_CLAUSE). is(f.valuesClause(
				b.optional(f.newTuple70(b.token(EsqlNonReservedKeyword.IDENTITY), FIELD_REFERENCE())),
				b.optional(f.newTuple71(b.token(EsqlNonReservedKeyword.TYPE), EXPRESSION())),
				b.optional(f.newTuple72(b.token(EsqlNonReservedKeyword.NAMESPACE), EXPRESSION())),
				b.optional(f.newTuple73(b.token(EsqlNonReservedKeyword.NAME), EXPRESSION())),
				b.optional(f.newTuple74(b.token(EsqlNonReservedKeyword.VALUE), EXPRESSION()))
						
		));
	}

	public FromClauseTreeImpl FROM_CLAUSE() {
		return b.<FromClauseTreeImpl>nonterminal(Kind.FROM_CLAUSE). is(f.fromClause(
				b.token(EsqlReservedKeyword.FROM), FIELD_REFERENCE()
		));
	}
	
	public ParseClauseTreeImpl PARSE_CLAUSE(){
		return b.<ParseClauseTreeImpl>nonterminal(Kind.PARSE_CLAUSE). is(f.parseClause(
				b.token(EsqlNonReservedKeyword.PARSE), b.token(LPARENTHESIS),ARGUMENT_LIST(),
				b.zeroOrMore(f.newTuple75(b.firstOf(b.token(EsqlNonReservedKeyword.ENCODING), b.token(EsqlNonReservedKeyword.CCSID), 
						b.token(EsqlNonReservedKeyword.SET), b.token(EsqlNonReservedKeyword.TYPE), 
						b.token(EsqlNonReservedKeyword.FORMAT), b.token(EsqlNonReservedKeyword.OPTIONS)), 
						EXPRESSION()
				)),
				b.token(RPARENTHESIS)
				
		));
	}
	

	public DeleteStatementTreeImpl DELETE_STATEMENT() {
		return b.<DeleteStatementTreeImpl>nonterminal(Kind.DELETE_STATEMENT). is(f.deleteStatement(
				b.token(EsqlNonReservedKeyword.DELETE), 
				b.firstOf(b.token(EsqlNonReservedKeyword.FIELD), 
					f.newTuple76(b.firstOf(b.token(EsqlNonReservedKeyword.FIRSTCHILD), b.token(EsqlNonReservedKeyword.LASTCHILD), b.token(EsqlNonReservedKeyword.PREVIOUSSIBLING), b.token(EsqlNonReservedKeyword.NEXTSIBLING)), b.token(EsqlNonReservedKeyword.OF))
				), FIELD_REFERENCE(), b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public DetachStatementTreeImpl DETACH_STATEMENT() {
		return b.<DetachStatementTreeImpl>nonterminal(Kind.DETACH_STATEMENT).is(
				f.detachStatement(
				b.token(EsqlNonReservedKeyword.DETACH), FIELD_REFERENCE(), b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public ForStatementTreeImpl FOR_STATEMENT() {
		return b.<ForStatementTreeImpl>nonterminal(Kind.FOR_STATEMENT).is(f.forStatement(
				b.token(EsqlNonReservedKeyword.FOR), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME), b.token(EsqlNonReservedKeyword.AS),
				FIELD_REFERENCE(), b.token(EsqlNonReservedKeyword.DO), STATEMENTS(),b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.FOR),
				b.token(EsqlLegacyGrammar.EOS)
		));
	}
	public MoveStatementTreeImpl MOVE_STATEMENT() {
		return b.<MoveStatementTreeImpl>nonterminal(Kind.MOVE_STATEMENT).is(f.moveStatement(
				b.token(EsqlNonReservedKeyword.MOVE), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME),
				b.firstOf(f.newTuple77(b.token(EsqlNonReservedKeyword.TO), FIELD_REFERENCE()), b.token(EsqlNonReservedKeyword.PARENT),
						f.newTuple78(b.firstOf(b.token(EsqlNonReservedKeyword.FIRSTCHILD),b.token(EsqlNonReservedKeyword.LASTCHILD),
								b.token(EsqlNonReservedKeyword.PREVIOUSSIBLING),b.token(EsqlNonReservedKeyword.NEXTSIBLING)), NAME_CLAUSES())
						),
				b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public NameClausesTreeImpl NAME_CLAUSES(){
		return b.<NameClausesTreeImpl>nonterminal(Kind.NAME_CLAUSES).is(f.nameClauses(
				b.firstOf(f.newTriple5(b.token(EsqlNonReservedKeyword.REPEAT), b.optional(b.token(EsqlNonReservedKeyword.TYPE)), b.optional(b.token(EsqlNonReservedKeyword.NAME))),
						f.newTriple6(
								b.optional(f.newTuple80(b.token(EsqlNonReservedKeyword.TYPE), EXPRESSION())),
								b.optional(f.newTuple81(b.token(EsqlNonReservedKeyword.NAMESPACE), b.firstOf(b.token(EsqlPunctuator.STAR), EXPRESSION()))),
								b.optional(f.newTuple82(b.token(EsqlNonReservedKeyword.NAME), EXPRESSION()))),
						f.newTuple79(b.token(EsqlNonReservedKeyword.IDENTITY), PATH_ELEMENT())
				)
		));
	}
	
	public DeleteFromStatementTreeImpl DELETE_FROM_STATEMENT(){
		return b.<DeleteFromStatementTreeImpl>nonterminal(Kind.DELETE_FROM_STATEMENT).is (f.deleteFromStatement(
				b.token(EsqlNonReservedKeyword.DELETE),b.token(EsqlReservedKeyword.FROM),FIELD_REFERENCE(),
				b.optional(f.newTuple83(b.token(EsqlNonReservedKeyword.AS), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME))), //TODO use AliasedFieldReference
				b.optional(f.newTuple84(b.token(EsqlNonReservedKeyword.WHERE), EXPRESSION())),
				b.token(EsqlLegacyGrammar.EOS)
		));
	}
	public InsertStatementTreeImpl INSERT_STATEMENT(){
		return b.<InsertStatementTreeImpl>nonterminal(Kind.INSERT_STATEMENT).is (f.insertStatement(
				b.token(EsqlNonReservedKeyword.INSERT),b.token(EsqlNonReservedKeyword.INTO),FIELD_REFERENCE(),
				b.optional(ARGUMENT_CLAUSE()),b.token(EsqlNonReservedKeyword.VALUES), b.token(EsqlPunctuator.LPARENTHESIS),
				EXPRESSION(),b.zeroOrMore(f.newTuple85(b.token(EsqlPunctuator.COMMA), EXPRESSION())),
				b.token(EsqlPunctuator.RPARENTHESIS), b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public PassthruStatementTreeImpl PASSTHRU_STATEMENT(){
		return b.<PassthruStatementTreeImpl>nonterminal(Kind.PASSTHRU_STATEMENT). is(f.completePassthruStatement(
				b.token(EsqlNonReservedKeyword.PASSTHRU), b.firstOf(f.passthruExpressionList(
						ARGUMENT_CLAUSE()
				), f.passthruSingleExpression(
						EXPRESSION(), b.optional(f.newTuple86(b.token(EsqlNonReservedKeyword.TO), FIELD_REFERENCE())),
						b.optional(f.newTuple87(b.token(EsqlNonReservedKeyword.VALUES), ARGUMENT_CLAUSE()))
				)), b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public UpdateStatementTreeImpl UPDATE_STATEMENT(){
		return b.<UpdateStatementTreeImpl>nonterminal(Kind.UPDATE_STATEMENT).is(f.updateStatement(
				b.token(EsqlNonReservedKeyword.UPDATE), 
				FIELD_REFERENCE(), 
				b.optional(f.newTuple88(b.token(EsqlNonReservedKeyword.AS), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME))), //TODO use AliasedFieldReference
				b.token(EsqlNonReservedKeyword.SET), 
				SET_COLUMN(), b.zeroOrMore(f.newTuple89(b.token(EsqlPunctuator.COMMA), SET_COLUMN())),
				b.optional(f.newTuple90(b.token(EsqlNonReservedKeyword.WHERE), EXPRESSION())),
				b.token(EsqlLegacyGrammar.EOS)
		));
		
		
	}
	
	public SetColumnTreeImpl SET_COLUMN(){
		return b.<SetColumnTreeImpl>nonterminal(Kind.SET_COLUMN).is(f.setColumn(
				b.token(EsqlLegacyGrammar.IDENTIFIER_NAME), b.token(EsqlPunctuator.EQUAL), EXPRESSION()
				));
	}
	
	public DeclareHandlerStatementTreeImpl DECLARE_HANDLER_STATEMENT(){
		return b.<DeclareHandlerStatementTreeImpl>nonterminal(Kind.DECLARE_HANDLER_STATEMENT).is(f.declareHandlerStatement(
				b.token(EsqlNonReservedKeyword.DECLARE), b.firstOf(b.token(EsqlNonReservedKeyword.CONTINUE), b.token(EsqlNonReservedKeyword.EXIT)),
				b.token(EsqlNonReservedKeyword.HANDLER), b.token(EsqlNonReservedKeyword.FOR) , SQL_STATE(),
				b.zeroOrMore(f.newTuple91(b.token(EsqlPunctuator.COMMA), SQL_STATE())),STATEMENT()
		));
	}
	
	public SqlStateTreeImpl SQL_STATE(){
		return b.<SqlStateTreeImpl>nonterminal(Kind.SQL_STATE).is(f.finishSqlState(
			b.token(EsqlNonReservedKeyword.SQLSTATE), b.firstOf(
				f.sqlStateLike(b.token(EsqlNonReservedKeyword.LIKE), STRING_LITERAL(), b.optional(f.newTuple92(b.token(EsqlNonReservedKeyword.ESCAPE), STRING_LITERAL()))),
				f.sqlStateValue(b.optional(b.token(EsqlNonReservedKeyword.VALUE)), STRING_LITERAL()))
		));
	}
	
	public EvalStatementTreeImpl EVAL_STATEMENT(){
		return b.<EvalStatementTreeImpl>nonterminal(Kind.EVAL_STATEMENT).is(f.evalStatement(
				b.token(EsqlNonReservedKeyword.EVAL), b.token(LPARENTHESIS), EXPRESSION(), b.token(RPARENTHESIS),
				b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public LogStatementTreeImpl LOG_STATEMENT(){
		return b.<LogStatementTreeImpl>nonterminal(Kind.LOG_STATEMENT).is(f.logStatement(
				b.token(EsqlNonReservedKeyword.LOG), b.firstOf(b.token(EsqlNonReservedKeyword.EVENT), f.newTuple93(b.token(EsqlNonReservedKeyword.USER), b.token(EsqlNonReservedKeyword.TRACE))),
				b.optional(f.newTuple94(b.optional(b.token(EsqlNonReservedKeyword.FULL)), b.token(EsqlNonReservedKeyword.EXCEPTION))),
				b.optional(f.newTuple95(b.token(EsqlNonReservedKeyword.SEVERITY), EXPRESSION())),
				b.optional(f.newTuple96(b.token(EsqlNonReservedKeyword.CATALOG), EXPRESSION())),
				b.optional(f.newTuple97(b.token(EsqlNonReservedKeyword.MESSAGE), EXPRESSION())),
				b.optional(f.newTuple98(b.token(EsqlNonReservedKeyword.VALUES), ARGUMENT_CLAUSE())),
				b.token(EsqlLegacyGrammar.EOS)
		));
	}
	
	public ResignalStatementTreeImpl RESIGNAL_STATEMENT(){
		return b.<ResignalStatementTreeImpl>nonterminal(Kind.RESIGNAL_STATEMENT).is(f.resignalStatement(
				b.token(EsqlNonReservedKeyword.RESIGNAL), b.token(EsqlLegacyGrammar.EOS)
				));
	}
	
	public CastFunctionTreeImpl CAST_FUNCTION(){
		return b.<CastFunctionTreeImpl>nonterminal(Kind.CAST_FUNCTION).is(f.castFunction(
				b.token(EsqlNonReservedKeyword.CAST),b.token(EsqlPunctuator.LPARENTHESIS),
				ARGUMENT_LIST(), b.token(EsqlNonReservedKeyword.AS), DATA_TYPE(),
				b.zeroOrMore(f.newTuple109(b.firstOf(b.token(EsqlNonReservedKeyword.CCSID), 
						b.token(EsqlNonReservedKeyword.ENCODING), b.token(EsqlNonReservedKeyword.FORMAT),
						b.token(EsqlNonReservedKeyword.DEFAULT)), CALL_EXPRESSION())), b.token(EsqlPunctuator.RPARENTHESIS)
				
		));
	}
	
	public CaseFunctionTreeImpl CASE_FUNCTION(){
		return b.<CaseFunctionTreeImpl>nonterminal(Kind.CASE_FUNCTION).is(f.caseFunction(
				b.token(EsqlReservedKeyword.CASE), b.firstOf(b.oneOrMore(WHEN_CLAUSE_EXPRESSION()), f.newTuple111(CALL_EXPRESSION(), b.oneOrMore(WHEN_CLAUSE_EXPRESSION()))), 
				b.optional(f.newTuple110(b.token(EsqlNonReservedKeyword.ELSE), EXPRESSION())),
				b.token(EsqlNonReservedKeyword.END)
		));
	}
	
	public WhenClauseExpressionTreeImpl WHEN_CLAUSE_EXPRESSION(){
		return b.<WhenClauseExpressionTreeImpl>nonterminal(Kind.WHEN_CLAUSE_EXPRESSION).is(f.whenClauseExpression(
				b.token(EsqlReservedKeyword.WHEN), EXPRESSION(), b.token(EsqlNonReservedKeyword.THEN), EXPRESSION()
		));
	}
	
	public SelectFunctionTreeImpl SELECT_FUNCTION(){
		return b.<SelectFunctionTreeImpl>nonterminal(Kind.SELECT_FUNCTION).is(f.selectFunction(
				b.token(EsqlNonReservedKeyword.SELECT), SELECT_CLAUSE(), FROM_CLAUSE_EXPRESSION(), b.optional(WHERE_CLAUSE())
		));
	}

	public WhereClauseTreeImpl WHERE_CLAUSE() {
		return b.<WhereClauseTreeImpl>nonterminal(Kind.WHERE_CLAUSE).is(f.whereClause(
			b.token(EsqlNonReservedKeyword.WHERE), EXPRESSION()
		));
	}

	public SelectClauseTreeImpl SELECT_CLAUSE() {
		return b.<SelectClauseTreeImpl>nonterminal(Kind.SELECT_CLAUSE).is(f.finishSelectClause(
			b.firstOf(
				f.selectClauseAggregation(b.firstOf(b.token(EsqlNonReservedKeyword.COUNT),b.token(EsqlNonReservedKeyword.MAX),b.token(EsqlNonReservedKeyword.MIN),b.token(EsqlNonReservedKeyword.SUM)), b.token(EsqlPunctuator.LPARENTHESIS), CALL_EXPRESSION(), b.token(EsqlPunctuator.RPARENTHESIS)),
				f.selectClauseItem(b.token(EsqlReservedKeyword.ITEM), CALL_EXPRESSION()),
				f.selectClauseFields(ALIASED_EXPRESSION(), b.zeroOrMore(f.newTuple113(b.token(EsqlPunctuator.COMMA), ALIASED_EXPRESSION())))
			)
		));
	}

	public FromClauseExpressionTreeImpl FROM_CLAUSE_EXPRESSION() {
		return b.<FromClauseExpressionTreeImpl>nonterminal(Kind.FROM_CLAUSE_EXPRESSION). is(f.fromClauseExpression(
				b.token(EsqlReservedKeyword.FROM), ALIASED_FIELD_REFERENCE(), b.zeroOrMore(f.newTuple112(b.token(EsqlPunctuator.COMMA), ALIASED_FIELD_REFERENCE()))		));
	}
	
	public AliasedFieldReferenceTreeImpl ALIASED_FIELD_REFERENCE(){
		return b.<AliasedFieldReferenceTreeImpl>nonterminal(Kind.ALIASED_FIELD_REFERENCE).is(f.finishAliasFieldReference(
				b.firstOf(f.aliasFieldReference(FIELD_REFERENCE(), b.token(EsqlNonReservedKeyword.AS), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME)) , 
						f.aliasFieldReference(FIELD_REFERENCE())
		)));
	}
	
	public  RowConstructorFunctionTreeImpl ROW_CONSTRUCTOR_FUNCTION(){
		return b.<RowConstructorFunctionTreeImpl>nonterminal(Kind.ROW_CONSTRUCTOR_FUNCTION).is(f.rowConstructorFunction(
				b.token(EsqlNonReservedKeyword.ROW), b.token(EsqlPunctuator.LPARENTHESIS), ALIASED_EXPRESSION(), 
				b.zeroOrMore(f.newTuple114(b.token(EsqlPunctuator.COMMA), ALIASED_EXPRESSION())),
				b.token(EsqlPunctuator.RPARENTHESIS)
		));
	}
	
	public AliasedExpressionTreeImpl ALIASED_EXPRESSION(){
		return b.<AliasedExpressionTreeImpl>nonterminal(Kind.ALIASED_EXPRESSION).is(f.finishAliasedExpression(
				b.firstOf(f.aliasedExpression(EXPRESSION(), b.token(EsqlNonReservedKeyword.AS), b.token(EsqlLegacyGrammar.IDENTIFIER_NAME)) , 
						f.aliasedExpression(EXPRESSION())
		)));
	}
	
	public PassthruFunctionTreeImpl PASSTHRU_FUNCTION(){
		return b.<PassthruFunctionTreeImpl>nonterminal(Kind.PASSTHRU_FUNCTION).is(f.finishPassthruFunction(
				b.token(EsqlNonReservedKeyword.PASSTHRU), b.token(EsqlPunctuator.LPARENTHESIS),
				b.firstOf(
						f.passthruNewSyntax(CALL_EXPRESSION(), b.optional(f.newTuple115(b.token(EsqlNonReservedKeyword.TO), FIELD_REFERENCE())),
								b.optional(f.newTuple116(b.token(EsqlNonReservedKeyword.VALUES), ARGUMENT_CLAUSE()))), 
						f.passthruOldSyntax(ARGUMENT_LIST())
				), 
				b.token(EsqlPunctuator.RPARENTHESIS)
		));
	}

}
