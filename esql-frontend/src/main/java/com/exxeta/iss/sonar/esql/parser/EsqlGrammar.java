/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ListFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
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
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ProgramTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ArrayLiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IntervalExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.LiteralTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.expression.ParenthesisedExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.BeginEndStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CallStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CaseStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ControlsTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateFunctionStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateModuleStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ElseClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ElseifClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ExternalRoutineBodyTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.IfStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.IterateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LabelTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LanguageTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LeaveStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LoopStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MessageSourceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ParameterTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.PropagateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RepeatStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ResultSetTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ReturnStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ReturnTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.RoutineBodyTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.SetStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ThrowStatementTreeImpl;
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
						b.token(EsqlNonReservedKeyword.FUNCTION), b.token(EsqlLegacyGrammar.IDENTIFIER),
						b.token(EsqlPunctuator.LPARENTHESIS), b.optional(PARAMETER()),
						b.zeroOrMore(f.newTuple22(b.token(EsqlPunctuator.COMMA), PARAMETER())),
						b.token(EsqlPunctuator.RPARENTHESIS), b.optional(RETURN_TYPE()), b.optional(LANGUAGE()),
						b.optional(RESULT_SET()), ROUTINE_BODY()

		));

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
				b.token(EsqlLegacyGrammar.expression),
				b.optional(b.firstOf(b.token(EsqlNonReservedKeyword.NAME), b.token(EsqlNonReservedKeyword.NAMESPACE),
						f.newTuple8(b.optional(b.token(EsqlNonReservedKeyword.CONSTANT)),
								b.token(EsqlLegacyGrammar.dataType))))));
	}

	public RoutineBodyTreeImpl ROUTINE_BODY() {
		return b.<RoutineBodyTreeImpl>nonterminal(Kind.ROUTINE_BODY)
				.is(f.routineBody(b.firstOf(STATEMENT(), EXTERNAL_ROUTINE_BODY())));
	}

	public ExternalRoutineBodyTreeImpl EXTERNAL_ROUTINE_BODY() {
		return b.<ExternalRoutineBodyTreeImpl>nonterminal(Kind.EXTERNAL_ROUTINE_BODY)
				.is(f.externalRoutineBody(b.token(EsqlNonReservedKeyword.EXTERNAL),
						b.token(EsqlNonReservedKeyword.NAME), b.token(EsqlLegacyGrammar.expression)));
	}

	public CreateProcedureStatementTree CREATE_PROCEDURE_STATEMENT() {
		return b.<CreateProcedureStatementTree>nonterminal(Kind.CREATE_PROCEDURE_STATEMENT)
				.is(f.createProcedureStatement(b.token(EsqlNonReservedKeyword.CREATE),
						b.token(EsqlNonReservedKeyword.PROCEDURE), b.token(EsqlLegacyGrammar.IDENTIFIER),
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
						b.token(EsqlNonReservedKeyword.MODULE), b.token(EsqlLegacyGrammar.IDENTIFIER),
						b.zeroOrMore(b.firstOf(ROUTINE_DECLARATION(), DECLARE_STATEMENT())),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.MODULE),
						b.token(EsqlLegacyGrammar.EOS)));

	}

	public RoutineDeclarationTree ROUTINE_DECLARATION() {
		return b.<RoutineDeclarationTree>nonterminal(Kind.ROUTINE_DECLARATION)
				.is(b.firstOf(CREATE_FUNCTION_STATEMENT(), CREATE_PROCEDURE_STATEMENT()));

	}

	public BrokerSchemaStatementTreeImpl BROKER_SCHEMA_STATEMENT() {
		return b.<BrokerSchemaStatementTreeImpl>nonterminal(EsqlLegacyGrammar.BROKER_SCHEMA_STATEMENT)
				.is(f.brokerSchema(b.token(EsqlNonReservedKeyword.BROKER), b.token(EsqlNonReservedKeyword.SCHEMA),
						SCHEMA_NAME()));

	}

	public PathClauseTree PATH_CLAUSE() {
		return b.<PathClauseTree>nonterminal(EsqlLegacyGrammar.PATH_CLAUSE)
				.is(f.pathClause(b.token(EsqlNonReservedKeyword.PATH), SCHEMA_NAME(),
						b.zeroOrMore(f.newTuple4(b.token(COMMA), SCHEMA_NAME()))));
	}

	public SchemaNameTree SCHEMA_NAME() {
		return b.<SchemaNameTree>nonterminal(Kind.SCHEMA_NAME).is(f.schemaName(b.token(EsqlLegacyGrammar.IDENTIFIER),
				b.zeroOrMore(f.newTuple5(b.token(DOT), b.token(EsqlLegacyGrammar.IDENTIFIER)))));
	}

	public StatementTree STATEMENT() {
		return b.<StatementTree>nonterminal(EsqlLegacyGrammar.statement).is(b.firstOf(
				BASIC_STATEMENT()/*
									 * , MESSAGE_TREE_MANIPULATION_STATEMENT()
									 */,
				NODE_INTERACTION_STATEMENT()/*
											 * , DATABASE_UPDATE_STATEMENT(),
											 * OTHER_STATEMENT()
											 */));
	}

	public FunctionTree FUNCTION() {
		return LIST_FUNCTION();
	}

	private ListFunctionTree LIST_FUNCTION() {
		return THE_FUNCTION();
	}

	private TheFunctionTree THE_FUNCTION() {
		return b.<TheFunctionTree>nonterminal(EsqlLegacyGrammar.function)
				.is(f.theFunction(b.token(EsqlNonReservedKeyword.THE), b.token(EsqlPunctuator.LPARENTHESIS),
						b.token(EsqlLegacyGrammar.expression), b.token(EsqlPunctuator.RPARENTHESIS)));
	}

	private StatementTree OTHER_STATEMENT() {
		// TODO Auto-generated method stub
		return null;
	}

	private StatementTree DATABASE_UPDATE_STATEMENT() {
		// TODO Auto-generated method stub
		return null;
	}

	private StatementTree NODE_INTERACTION_STATEMENT() {
		return PROPAGATE_STATEMENT();
	}

	private StatementTree MESSAGE_TREE_MANIPULATION_STATEMENT() {
		// TODO Auto-generated method stub
		return null;
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
						b.zeroOrMore(STATEMENT()), b.token(EsqlNonReservedKeyword.END),
						b.optional(LABEL()), b.token(EsqlLegacyGrammar.EOS)));
	}
	public LabelTreeImpl LABEL(){
		return b.<LabelTreeImpl>nonterminal(Kind.LABEL).is(f.label(b.token(EsqlLegacyGrammar.IDENTIFIER)));
	}
	
	public CallStatementTreeImpl CALL_STATEMENT() {
		return b.<CallStatementTreeImpl>nonterminal(Kind.CALL_STATEMENT)
				.is(f.callStatement(
						b.token(EsqlNonReservedKeyword.CALL), 
						b.optional(f.newTuple44(SCHEMA_NAME(),b.token(DOT))),
						b.token(EsqlLegacyGrammar.IDENTIFIER),
						b.token(LPARENTHESIS),b.optional(f.newTuple45(EXPRESSION(),b.zeroOrMore(f.newTuple46(b.token(COMMA),EXPRESSION())))),
						b.token(RPARENTHESIS), 
						b.optional(b.firstOf(
								f.newTuple47(b.token(EsqlNonReservedKeyword.IN), FIELD_REFERENCE()),
								f.newTriple4(b.token(EsqlNonReservedKeyword.EXTERNAL),b.token(EsqlNonReservedKeyword.SCHEMA), b.token(EsqlLegacyGrammar.IDENTIFIER))
						)),
						b.optional(f.newTuple48(b.token(EsqlNonReservedKeyword.INTO), FIELD_REFERENCE())),
						b.token(EsqlLegacyGrammar.EOS)
						
				));
	}

	public CaseStatementTreeImpl CASE_STATEMENT() {
		return b.<CaseStatementTreeImpl>nonterminal(Kind.CASE_STATEMENT)
				.is(f.caseStatement(b.token(EsqlReservedKeyword.CASE), b.firstOf(b.oneOrMore(WHEN_CLAUSE()), f.newTuple61(EXPRESSION(), b.oneOrMore(WHEN_CLAUSE()))), b.optional(f.newTuple60(b.token(EsqlNonReservedKeyword.ELSE), b.zeroOrMore(STATEMENT()))),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlReservedKeyword.CASE), b.token(EsqlLegacyGrammar.EOS)));
	}
	
	public WhenClauseTreeImpl WHEN_CLAUSE(){
		return b.<WhenClauseTreeImpl>nonterminal(Kind.WHEN_CLAUSE)
				.is(f.whenClause(b.token(EsqlReservedKeyword.WHEN), EXPRESSION(), b.token(EsqlNonReservedKeyword.THEN), b.zeroOrMore(STATEMENT())));
	}

	public DeclareStatementTreeImpl DECLARE_STATEMENT() {
		return b.<DeclareStatementTreeImpl>nonterminal(Kind.DECLARE_STATEMENT)
				.is(f.declareStatement(
						b.token(EsqlNonReservedKeyword.DECLARE), b.token(EsqlLegacyGrammar.IDENTIFIER),
						b.zeroOrMore(f.newTuple42(b.token(COMMA),b.token(EsqlLegacyGrammar.IDENTIFIER))),
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
						b.zeroOrMore(STATEMENT()), b.zeroOrMore(ELSEIF_CLAUSE()), b.optional(ELSE_CLAUSE()),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.IF),
						b.token(EsqlLegacyGrammar.EOS)));
	}

	public ElseClauseTreeImpl ELSE_CLAUSE() {
		return b.<ElseClauseTreeImpl>nonterminal(Kind.ELSE_CLAUSE)
				.is(f.elseClause(b.token(EsqlNonReservedKeyword.ELSE), b.zeroOrMore(STATEMENT())));
	}

	public ElseifClauseTreeImpl ELSEIF_CLAUSE() {
		return b.<ElseifClauseTreeImpl>nonterminal(Kind.ELSEIF_CLAUSE)
				.is(f.elseifClause(b.token(EsqlNonReservedKeyword.ELSEIF), EXPRESSION(),
						b.token(EsqlNonReservedKeyword.THEN), b.zeroOrMore(STATEMENT())));
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
				.is(f.loopStatementWoLabel(b.token(EsqlNonReservedKeyword.LOOP), b.zeroOrMore(STATEMENT()),
						b.token(EsqlNonReservedKeyword.END), b.token(EsqlNonReservedKeyword.LOOP),
						b.token(EsqlLegacyGrammar.EOS)));
	}

	public LoopStatementTreeImpl LOOP_STATEMENT_WITH_LABEL() {
		return b.<LoopStatementTreeImpl>nonterminal()
				.is(f.loopStatementWithLabel(LABEL(), b.token(COLON), b.token(EsqlNonReservedKeyword.LOOP),
						b.zeroOrMore(STATEMENT()), b.token(EsqlNonReservedKeyword.END),
						b.token(EsqlNonReservedKeyword.LOOP), LABEL(), b.token(EsqlLegacyGrammar.EOS)

		));
	}
	
	public RepeatStatementTreeImpl REPEAT_STATEMENT() {
		return b.<RepeatStatementTreeImpl>nonterminal(Kind.REPEAT_STATEMENT)
				.is(b.firstOf(REPEAT_STATEMENT_WO_LABEL(), REPEAT_STATEMENT_WITH_LABEL()));
	}

	public RepeatStatementTreeImpl REPEAT_STATEMENT_WITH_LABEL() {
		return b.<RepeatStatementTreeImpl>nonterminal()
				.is(f.repeatStatementWithLabel(
						LABEL(), b.token(COLON), b.token(EsqlNonReservedKeyword.REPEAT), b.zeroOrMore(STATEMENT()), 
						b.token(EsqlNonReservedKeyword.UNTIL), EXPRESSION(), b.token(EsqlNonReservedKeyword.END), 
						b.token(EsqlNonReservedKeyword.REPEAT), LABEL(), b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public RepeatStatementTreeImpl REPEAT_STATEMENT_WO_LABEL() {
		return b.<RepeatStatementTreeImpl>nonterminal()
				.is(f.repeatStatementWoLabel(
						b.token(EsqlNonReservedKeyword.REPEAT), b.zeroOrMore(STATEMENT()), 
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
	
	public SetStatementTreeImpl SET_STATEMENT() {
		return b.<SetStatementTreeImpl>nonterminal(Kind.SET_STATEMENT).is(f.setStatement(
				b.token(EsqlNonReservedKeyword.SET), FIELD_REFERENCE(),
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
						b.token(EsqlNonReservedKeyword.DO), b.zeroOrMore(STATEMENT()), b.token(EsqlNonReservedKeyword.END), 
						b.token(EsqlNonReservedKeyword.WHILE), LABEL(), b.token(EsqlLegacyGrammar.EOS)
						));
	}

	public WhileStatementTreeImpl WHILE_STATEMENT_WO_LABEL() {
		return b.<WhileStatementTreeImpl>nonterminal()
				.is(f.whileStatementWoLabel(
						b.token(EsqlNonReservedKeyword.WHILE), EXPRESSION(), 
						b.token(EsqlNonReservedKeyword.DO), b.zeroOrMore(STATEMENT()), b.token(EsqlNonReservedKeyword.END), 
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
						b.token(EsqlLegacyGrammar.expression))),
				b.optional(
						f.newTuple12(b.token(EsqlNonReservedKeyword.MESSAGE), b.token(EsqlLegacyGrammar.expression))),
				b.optional(f.newTuple13(b.token(EsqlNonReservedKeyword.EXCEPTION),
						b.token(EsqlLegacyGrammar.expression)))));

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
								f.newTuple25(b.firstOf(b.token(EsqlPunctuator.PLUS), b.token(EsqlPunctuator.MINUS)),
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
				.is(b.firstOf(f.prefixExpression(b.firstOf(b.token(EsqlNonReservedKeyword.NOT)), UNARY_EXPRESSION()),
						LEFT_HAND_SIDE_EXPRESSION()));
	}

	public ExpressionTree LEFT_HAND_SIDE_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.leftHandSideExpression).is(b.firstOf(IN_EXPRESSION(), CALL_EXPRESSION()));
	}

	public ExpressionTree IN_EXPRESSION(){
		return b.<ExpressionTree>nonterminal(Kind.IN_EXPRESSION).is(f.inExpression(FIELD_REFERENCE(), b.token(EsqlNonReservedKeyword.IN), ARGUMENT_LIST())
				);
	}
	
	public ExpressionTree CALL_EXPRESSION() {
		return b.<ExpressionTree>nonterminal(Kind.CALL_EXPRESSION).is(f.callExpression(
				b.firstOf(FUNCTION(), f.newTuple24(FIELD_REFERENCE(), ARGUMENT_CLAUSE()), FIELD_REFERENCE())));
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
								// f.newTuple20(b.optional(b.token(EsqlNonReservedKeyword.NOT)),
								// b.token(EsqlNonReservedKeyword.IN)),
								// f.newTuple21(b.optional(b.token(EsqlNonReservedKeyword.NOT)),
								// b.token(EsqlNonReservedKeyword.BETWEEN)),
								), ADDITIVE_EXPRESSION()))));

		// FIXME

		/*
		 * b.rule(relationalExpression) .is(b.firstOf(additiveExpression,
		 * subtractiveExpression), b .firstOf(
		 * b.sequence(b.firstOf(b.sequence("NOT", "IN"), "IN"), "(",
		 * b.firstOf(additiveExpression, subtractiveExpression),
		 * b.zeroOrMore(b.sequence( ",", b.firstOf(additiveExpression,
		 * subtractiveExpression))), ")"),
		 * b.sequence(b.firstOf(b.sequence("NOT", "BETWEEN"), "BETWEEN"),
		 * b.optional(b.firstOf("ASYMMETRIC", "SYMMETRIC")),
		 * b.firstOf(additiveExpression, subtractiveExpression), "AND", b
		 * .firstOf(additiveExpression, subtractiveExpression)), b.zeroOrMore(
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
		return b.<ExpressionTree>nonterminal(EsqlLegacyGrammar.expression).is(f.expression(LOGICAL_OR_EXPRESSION()));
	}

	public LiteralTreeImpl LIST_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.LIST_LITERAL)
				.is(f.listLiteral(b.token(EsqlLegacyGrammar.listLiteral)));
	}

	public LiteralTreeImpl TIME_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.TIME_LITERAL)
				.is(f.timeLiteral(b.token(EsqlLegacyGrammar.timeLiteral)));
	}

	public LiteralTreeImpl DATE_LITERAL() {
		return b.<LiteralTreeImpl>nonterminal(Kind.DATE_LITERAL)
				.is(f.dateLiteral(b.token(EsqlLegacyGrammar.dateLiteral)));
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
				.is(b.firstOf(INTERVAL_LITERAL(), LITERAL(), ARRAY_LITERAL(), INTERVAL_EXPRESSION(), LIST_LITERAL(),
						TIME_LITERAL(), DATE_LITERAL(), PARENTHESISED_EXPRESSION()));
	}

	public FieldReferenceTreeImpl FIELD_REFERENCE() {
		return b.<FieldReferenceTreeImpl>nonterminal(Kind.FIELD_REFERENCE)
				.is(f.fieldReference(b.firstOf(PRIMARY_EXPRESSION(), b.token(EsqlLegacyGrammar.IDENTIFIER)),
						b.zeroOrMore(f.newTuple21(b.token(EsqlPunctuator.DOT), PATH_ELEMENT()))));
	}

	public NamespaceTreeImpl NAMESPACE() {
		return b.<NamespaceTreeImpl>nonterminal(Kind.NAMESPACE).is(f.namespace(b.token(EsqlLegacyGrammar.IDENTIFIER)));
	}

	public PathElementTreeImpl PATH_ELEMENT() {
		return b.<PathElementTreeImpl>nonterminal(
				Kind.PATH_ELEMENT).is(
						f.pathElement(
								b.optional(f.newTriple1(b.token(EsqlPunctuator.LPARENTHESIS),
										f.newTuple30(PRIMARY_EXPRESSION(),
												b.zeroOrMore(f.newTuple20(b.token(EsqlPunctuator.DOT),
														PRIMARY_EXPRESSION()))),
										b.token(EsqlPunctuator.RPARENTHESIS))),
								b.optional(f.newTuple31(b.optional(b.firstOf(NAMESPACE(),
										f.newTriple2(b.token(EsqlPunctuator.LCURLYBRACE), EXPRESSION(),
												b.token(EsqlPunctuator.RCURLYBRACE)),
										b.token(EsqlPunctuator.STAR))), b.token(EsqlPunctuator.COLON))),
								b.firstOf(b.token(EsqlLegacyGrammar.IDENTIFIER),
										f.newTriple3(b.token(EsqlPunctuator.LCURLYBRACE), EXPRESSION(),
												b.token(EsqlPunctuator.RCURLYBRACE)),
										b.token(EsqlPunctuator.STAR)),
								b.optional(INDEX())));
	}

	public IndexTreeImpl INDEX() {
		return b.<IndexTreeImpl>nonterminal(Kind.INDEX)
				.is(f.index(b.token(EsqlPunctuator.LBRACKET),
						f.newTuple32(b.optional(b.firstOf(b.token(EsqlPunctuator.LT), b.token(EsqlPunctuator.GT))),
								b.optional(EXPRESSION())),
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

	public ArrayLiteralTreeImpl ARRAY_ELEMENT_LIST() {
		return b.<ArrayLiteralTreeImpl>nonterminal(EsqlLegacyGrammar.ELEMENT_LIST)
				.is(f.newArrayLiteralWithElements(b.zeroOrMore(b.token(EsqlPunctuator.COMMA)), EXPRESSION(),
						b.zeroOrMore(f.newTuple3(b.oneOrMore(b.token(EsqlPunctuator.COMMA)), EXPRESSION()))));
	}

	public ArrayLiteralTreeImpl ARRAY_LITERAL() {
		return b.<ArrayLiteralTreeImpl>nonterminal(Kind.ARRAY_LITERAL)
				.is(f.completeArrayLiteral(b.token(EsqlPunctuator.LBRACKET),
						b.optional(b.firstOf(ARRAY_ELEMENT_LIST(),
								f.newArrayLiteralWithElidedElements(b.oneOrMore(b.token(EsqlPunctuator.COMMA))))),
						b.token(EsqlPunctuator.RBRACKET)));
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
						f.newTuple37(b.token(EsqlNonReservedKeyword.DAY), f.newTuple36(
								b.token(EsqlNonReservedKeyword.TO), b.firstOf(b.token(EsqlNonReservedKeyword.HOUR),
										b.token(EsqlNonReservedKeyword.MINUTE), b
												.token(EsqlNonReservedKeyword.SECOND)))),
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
	

}
