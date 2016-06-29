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
package com.exxeta.iss.sonar.esql.api;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

public enum EsqlGrammar implements GrammarRuleKey {
	PROGRAM, SOURCEELEMENTS, EOS, LITERAL, BOOLEAN_LITERAL, NULL_LITERAL

	, listLiteral, dateLiteral, timeLiteral

	// , letterOrDigit
	, sourceElements, sourceElement, keyword, dataType, intervalDataType, intervalQualifier

	, statement, basicStatement, messageTreeManipulationStatement, databaseUpdateStatement, nodeInteractionStatement, propagateStatement, messageSource, controlElement, otherStatement, declareHandlerStatement, resignalStatement, sqlState, repeatStatement, logStatement, logOptions, loopStatement

	, createStatement, deleteStatement, detachStatement, forStatement, moveStatement, nameClause, repeatClause, valuesClauses, fromClause, parseClause, namesClauses, valueExpression, passthruExpression, insertExpression, updateExpression, iterateStatement

	, qualifier

	, function, complexFunction, caseFunction, simpleWhenClause, searchedWhenClause, fieldFunction, listFunction, asbitstreamFunction, bitStreamOptions, forFunction, castFunction, extractFunction, datetimeFunction, miscellaneousFunction, numericFunction, databaseStateFunction, stringManipulationFunction, overlayFunction, positionFunction, ucaseFunction, startswithFunction, substringFunction, translateFunction, trimFunction, theFunction, intervalLiteral

	, beginEndStatement, callStatement, caseStatement, whenClause, leaveStatement, setStatement, declareStatement, returnStatement, whileStatement, ifStatement, throwStatement

	//, betweenOperator, existsOperator, inOperator, isOperator, singularOperator

	, variableStatement, moduleStatements, moduleStatement

	, parameterList, parameter

	, fieldReference, pathElement, namespace

	, returnType, language, resultSet, routineBody

	, moduleDeclaration, moduleBody, routineDeclaration, qualificationIdentifier, primaryExpression, arrayLiteral, callExpression, expression, condition, leftHandSideExpression, unaryExpression, multiplicativeExpression, additiveExpression, subtractiveExpression, relationalExpression, relationalExpressionNoIn, equalityExpression, equalityExpressionNoIn, logicalAndExpression, logicalAndExpressionNoIn, logicalOrExpression, logicalOrExpressionNoIn/*, assignmentExpression, assignmentExpressionNoIn*/ 
	// , sqlExpression
	, selectFunction, rowConstructorFunction, selectClause, whereClause
	
	, passthruFunction

	, index

	, FIELD_NAME, FUNCTION_DECLARATION, PROCEDURE_DECLARATION, BROKER_SCHEMA_STATEMENT, attachStatement, NAME, ELSEIF_CLAUSE, ELSE_CLAUSE, LABEL, ROUND_FUNCTION;

	public static LexerfulGrammarBuilder create() {
		LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

		b.rule(EOS).is(b.firstOf(b.optional(";"), b.next(EOF)));
		lexical(b);
		expression(b);
		b.rule(LITERAL)
				.is(b.firstOf(NULL_LITERAL, BOOLEAN_LITERAL, EsqlTokenType.NUMBER, EsqlTokenType.HEX, EsqlTokenType.STRING));
		b.rule(NULL_LITERAL).is("NULL");
		b.rule(BOOLEAN_LITERAL).is(b.firstOf("TRUE", "FALSE"));
		functionsAndPrograms(b);
		b.setRootRule(PROGRAM);
		b.buildWithMemoizationOfMatchesForAllRules();
		return b;
	}

	/*
	 * public EsqlGrammarImpl() {
	 * 
	 * }
	 */

	/**
	 * A.1 Lexical
	 */
	private static void lexical(LexerfulGrammarBuilder b) {
		// eof.is(token(GenericTokenType.EOF, endOfInput())).skip();
		// b.rule(IDENTIFIER).is(b.nextNot(KEYWORD),
		// token(GenericTokenType.IDENTIFIER, regexp(EsqlLexer.IDENTIFIER)),
		// spacing);
		b.rule(FIELD_NAME).is(IDENTIFIER);
		// b.rule(SPACING).is(
		// regexp(EsqlLexer.WHITESPACE + "*+"),
		// zeroOrMore(token(GenericTokenType.COMMENT,
		// regexp(EsqlLexer.COMMENT)), regexp(EsqlLexer.WHITESPACE
		// + "*+"))).skip();
		// b.rule(keyword).is(
		// b.firstOf(
		//
		// "ALL", "ASYMETRIC", "BOTH", "CASE", "DISTINCT", "FROM", "ITEM",
		// "LEADING", "NOT", "SYMMETRIC",
		// "TRAILING", "WHEN"), nextNot(letterOrDigit));
		// b.rule(letterOrDigit).is(b.regexp("\\p{javaJavaIdentifierPart}"));
		// b.rule(numericLiteral).is(token(EsqlTokenType.NUMERIC_LITERAL,
		// regexp(EsqlLexer.NUMERIC_LITERAL)), SPACING);
		// b.rule(stringLiteral).is(token(GenericTokenType.LITERAL,
		// regexp(EsqlLexer.LITERAL)), SPACING);
		// b.rule(hexLiteral).is(token(GenericTokenType.LITERAL,
		// regexp(EsqlLexer.HEX_LITERAL)), SPACING);
		b.rule(listLiteral).is("(", LITERAL, b.zeroOrMore(b.sequence(",", LITERAL)), ")");
		b.rule(intervalLiteral).is("INTERVAL", LITERAL, intervalQualifier);
		b.rule(dateLiteral).is(EsqlTokenType.DATE);
		b.rule(timeLiteral).is(EsqlTokenType.TIME);

	}

	/**
	 * A.5 Functions and Programs
	 */
	private static void functionsAndPrograms(LexerfulGrammarBuilder b) {
		b.rule(PROGRAM).is(b.optional(BROKER_SCHEMA_STATEMENT),
				b.optional(b.sequence("PATH", fieldReference, b.zeroOrMore(",", fieldReference), b.optional(";"))), sourceElements, EOF);
		b.rule(BROKER_SCHEMA_STATEMENT).is(b.sequence("BROKER", "SCHEMA", fieldReference));
		b.rule(sourceElements).is(b.zeroOrMore(sourceElement));
		b.rule(sourceElement).is(b.firstOf(moduleDeclaration, routineDeclaration, declareStatement), EOS);

		b.rule(moduleDeclaration).is("CREATE", b.firstOf("COMPUTE", "DATABASE", "FILTER"), "MODULE", NAME,
				moduleBody, "END", "MODULE");
		b.rule(moduleBody).is(b.optional(moduleStatements));
		b.rule(moduleStatements).is(b.oneOrMore(b.sequence(moduleStatement, EOS)));
		b.rule(moduleStatement).is(b.firstOf(routineDeclaration, declareStatement));

		b.rule(routineDeclaration).is(b.firstOf(FUNCTION_DECLARATION, PROCEDURE_DECLARATION));
		b.rule(FUNCTION_DECLARATION).is("CREATE", "FUNCTION", NAME, "(", parameterList, ")",
				b.optional(returnType), b.optional(language), b.optional(resultSet), routineBody);
		b.rule(PROCEDURE_DECLARATION).is("CREATE", "PROCEDURE", NAME, "(", parameterList, ")",
				b.optional(returnType), b.optional(language), b.optional(resultSet), routineBody);
		b.rule(parameterList).is(b.optional(parameter, b.zeroOrMore(b.sequence(",", parameter))));
		b.rule(parameter).is(b.optional(b.firstOf("IN", "OUT", "INOUT")), expression,
				b.optional(b.firstOf("NAME", "NAMESPACE", b.sequence(b.optional("CONSTANT"), dataType))));
		b.rule(returnType).is("RETURNS", dataType);
		b.rule(language).is(b.sequence("LANGUAGE", b.firstOf("ESQL", "DATABASE", "JAVA")));
		b.rule(resultSet).is("DYNAMIC", "RESULT", "SETS", EsqlTokenType.NUMBER);
		b.rule(routineBody).is(b.firstOf(statement, b.sequence("EXTERNAL", "NAME", expression)));
		b.rule(NAME).is(IDENTIFIER);
		b.rule(LABEL).is(IDENTIFIER);
		datatypes(b);
		statements(b);
		functions(b);
	}

	private static void datatypes(LexerfulGrammarBuilder b) {
		b.rule(dataType).is(
				b.firstOf("BOOLEAN", "INT", "INTEGER", "FLOAT",
						b.sequence("DECIMAL", b.optional("(", EsqlTokenType.NUMBER, ",", EsqlTokenType.NUMBER, ")")),
						"DATE", "TIME", "TIMESTAMP", "GMTTIME", "GMTTIMESTAMP", intervalDataType, "CHAR", "CHARACTER",
						"BLOB", "BIT", "ROW", b.sequence("REFERENCE", b.optional("TO"))));
		b.rule(intervalDataType).is("INTERVAL", b.optional(intervalQualifier));
		b.rule(intervalQualifier).is(
				b.firstOf(b.sequence("YEAR", b.optional(b.sequence("TO", "MONTH"))), "MONTH",
						b.sequence("DAY", b.optional("TO", b.firstOf("HOUR", "MINUTE", "SECOND"))),
						b.sequence("HOUR", b.optional(b.sequence("TO", b.firstOf("MINUTE", "SECOND")))),
						b.sequence("MINUTE", b.optional(b.sequence("TO", "SECOND"))), "SECOND"));
	}

	private static void statements(LexerfulGrammarBuilder b) {
		b.rule(statement).is(
				b.firstOf(basicStatement, messageTreeManipulationStatement, nodeInteractionStatement,
						databaseUpdateStatement, otherStatement), EOS);
		basicStatements(b);
		messageTreeManipulationStatements(b);
		databaseUpdateStatements(b);
		nodeInteractionStatements(b);
		otherStatements(b);
	}

	private static void otherStatements(LexerfulGrammarBuilder b) {
		b.rule(otherStatement).is(b.firstOf(declareHandlerStatement, logStatement, resignalStatement));
		b.rule(declareHandlerStatement).is("DECLARE", b.firstOf("CONTINUE", "EXIT"), "HANDLER", "FOR", sqlState,
				b.zeroOrMore(",", sqlState), statement);
		b.rule(sqlState).is(
				"SQLSTATE",
				b.firstOf(b.sequence("LIKE", EsqlTokenType.STRING,
						b.optional(b.sequence("ESCAPE", EsqlTokenType.STRING))), b.sequence(b.optional("VALUE"),
						EsqlTokenType.STRING)));
		b.rule(logStatement).is("LOG", b.firstOf("EVENT", b.sequence("USER", "TRACE")),
				b.optional(b.sequence(b.optional("FULL"), "EXCEPTION")), b.optional(logOptions),
				b.optional("VALUES", "(", expression, b.zeroOrMore(b.sequence(",", expression)), ")"));
		b.rule(logOptions).is(
				b.sequence(b.optional(b.sequence("SEVERITY", expression)),
						b.optional(b.sequence("CATALOG", expression)), b.optional(b.sequence("MESSAGE", expression))));
		b.rule(resignalStatement).is("RESIGNAL");
	}

	private static void nodeInteractionStatements(LexerfulGrammarBuilder b) {
		b.rule(nodeInteractionStatement).is(propagateStatement);
		b.rule(propagateStatement).is("PROPAGATE",
				b.optional(b.sequence("TO", b.firstOf("TERMINAL", "LABEL"), expression)), b.optional(messageSource),
				b.optional(controlElement));
		b.rule(messageSource).is(b.optional(b.sequence("ENVIRONMENT", expression)),
				b.optional(b.sequence("MESSAGE", expression)), b.optional(b.sequence("EXCEPTION", expression)));
		b.rule(controlElement).is(b.optional(b.sequence("FINALIZE", b.firstOf("DEFAULT", "NONE"))),
				b.optional(b.sequence("DELETE", b.firstOf("DEFAULT", "NONE"))));
	}

	private static void databaseUpdateStatements(LexerfulGrammarBuilder b) {
		b.rule(databaseUpdateStatement).is(b.firstOf(insertExpression, passthruExpression, updateExpression)); //DELETE FROM is already covered by DELETE
		b.rule(updateExpression).is("UPDATE", expression, b.optional(b.sequence("AS", expression)), "SET",
				fieldReference, "=", expression, b.zeroOrMore(b.sequence(",", fieldReference, "=", expression)),
				b.optional(whereClause));
		b.rule(insertExpression).is("INSERT", "INTO", expression,
				b.optional("(", expression, b.zeroOrMore(",", expression), ")"), "VALUES", "(", expression,
				b.zeroOrMore(",", expression), ")");
		b.rule(passthruExpression).is(
				"PASSTHRU",
				b.firstOf(b.sequence("(", expression, b.zeroOrMore(",", expression), ")"), b.sequence(b.optional("("),expression,
						b.optional(b.sequence("TO", expression, ".", expression)),
						b.optional("VALUES", "(", expression, b.zeroOrMore(",", expression), ")"), b.optional(")"))));
	}

	private static void messageTreeManipulationStatements(LexerfulGrammarBuilder b) {
		b.rule(messageTreeManipulationStatement).is(
				b.firstOf(attachStatement, createStatement, deleteStatement, detachStatement, forStatement,
						moveStatement));
		b.rule(attachStatement).is("ATTACH", fieldReference, "TO", fieldReference, "AS",
				b.firstOf("PREVIOUSSIBLING", "NEXTSIBLING", "FIRSTCHILD", "LASTCHILD"));
		b.rule(createStatement).is("CREATE", qualificationIdentifier, fieldReference,
				b.optional(b.sequence("AS", IDENTIFIER)), b.optional(b.sequence("DOMAIN", expression)),
				b.optional(b.firstOf(repeatClause, valuesClauses, fromClause, parseClause)));//
		b.rule(qualificationIdentifier).is(
				b.firstOf("FIELD",
						b.sequence(b.firstOf("PREVIOUSSIBLING", "NEXTSIBLING", "FIRSTCHILD", "LASTCHILD"), "OF")));
		b.rule(repeatClause).is(b.sequence("REPEAT", b.optional("VALUE", expression)));
		b.rule(valuesClauses).is(b.firstOf(b.sequence(namesClauses, b.optional(valueExpression)), valueExpression));
		b.rule(namesClauses).is(
				b.firstOf(
						b.sequence("IDENTITY", pathElement),
						b.firstOf(
								b.sequence("TYPE", expression, b.optional("NAMESPACE", expression),
										b.optional("NAME", expression)),
								b.sequence("NAMESPACE", expression, b.optional("NAME", expression)),
								b.sequence("NAME", expression))));
		b.rule(valueExpression).is(b.sequence("VALUE", expression)); // TODO
		// optional(namesClause),optional(
		b.rule(fromClause).is(
				"FROM",
				fieldReference,
				b.optional(b.sequence("AS", IDENTIFIER),
						b.zeroOrMore(b.sequence(",", fieldReference, b.optional(b.sequence("AS", IDENTIFIER))))));
		b.rule(parseClause)
				.is("PARSE",
						"(",
						expression,
						b.zeroOrMore(b.sequence(",", expression)),
						b.zeroOrMore(b.sequence(b.firstOf("ENCODING", "CCSID", "SET", "TYPE", "FORMAT", "OPTIONS"),
								expression)), ")");
		b.rule(detachStatement).is("DETACH", fieldReference);
		b.rule(forStatement).is("FOR", IDENTIFIER, "AS", fieldReference, "DO", b.oneOrMore(statement), "END", "FOR");
		b.rule(moveStatement)
				.is("MOVE",
						fieldReference,
						b.firstOf(b.sequence("TO", fieldReference), "PARENT", b.sequence(
								b.firstOf("FIRSTCHILD", "LASTCHILD", "PREVIOUSSIBLING", "NEXTSIBLING"), nameClause)));
		b.rule(nameClause).is(
				b.firstOf(
						b.sequence("REPEAT", b.firstOf(b.sequence("TYPE", "NAME"), "TYPE", "NAME")),
						b.sequence(b.optional(b.sequence("TYPE", expression)),
								b.optional(b.sequence("NAMESPACE", b.firstOf("*", expression))),
								b.optional(b.sequence("NAME", expression))), b.sequence("IDENTITY", pathElement)));
		b.rule(deleteStatement).is(
				"DELETE",
				b.firstOf(b.sequence("FROM", fieldReference, b.optional(b.sequence("AS", expression)),
						b.optional(b.sequence("WHERE", expression))), b.sequence(
						b.firstOf("FIELD", b.sequence(
								b.firstOf("FIRSTCHILD", "LASTCHILD", "PREVIOUSSIBLING", "NEXTSIBLING"), "OF")),
						fieldReference)));
	}

	// http://www-01.ibm.com/support/knowledgecenter/SSMKHH_9.0.0/com.ibm.etools.mft.doc/ak04900_.htm?lang=en
	private static void basicStatements(LexerfulGrammarBuilder b) {
		b.rule(basicStatement).is(
				b.firstOf(beginEndStatement, callStatement, caseStatement, /* create */declareStatement, ifStatement,
						iterateStatement, leaveStatement, loopStatement, repeatStatement, returnStatement,
						setStatement, throwStatement, whileStatement));
		b.rule(beginEndStatement).is(
				b.sequence(b.optional(b.sequence(LABEL, ":")), "BEGIN", b.optional(b.optional("NOT"), "ATOMIC"),
						b.zeroOrMore(statement), "END", b.optional(IDENTIFIER)));
		b.rule(callStatement).is("CALL", b.zeroOrMore(b.sequence(IDENTIFIER, ".")), IDENTIFIER, "(", parameterList,
				")", b.optional(qualifier), b.optional(b.sequence("INTO", IDENTIFIER)));
		b.rule(qualifier).is(b.firstOf(b.sequence("IN", fieldReference), b.sequence("EXTERNAL", "SCHEMA", IDENTIFIER)));
		b.rule(declareStatement).is("DECLARE", NAME, b.zeroOrMore(b.sequence(",", NAME)),
				b.optional(b.firstOf("SHARED", "EXTERNAL")),
				b.firstOf(b.sequence(b.optional("CONSTANT"), b.firstOf(dateLiteral, timeLiteral, dataType)), "NAMESPACE", "NAME"), b.optional(expression));
		b.rule(setStatement).is("SET", fieldReference, b.optional(b.firstOf("TYPE","NAMESPACE", "NAME", "VALUE")), "=", expression);
		b.rule(returnStatement).is("RETURN", b.optional(logicalOrExpression));
		b.rule(ifStatement).is("IF", expression, "THEN", b.zeroOrMore(statement),
				b.zeroOrMore(ELSEIF_CLAUSE),
				b.optional(ELSE_CLAUSE), "END", "IF");
		b.rule(ELSEIF_CLAUSE).is(b.sequence("ELSEIF", condition, "THEN", b.zeroOrMore(statement)));
		b.rule(ELSE_CLAUSE).is("ELSE", b.zeroOrMore(statement));
		b.rule(loopStatement).is(
				b.firstOf(b.sequence("LOOP", b.zeroOrMore(statement), "END", "LOOP"),
						b.sequence(LABEL, ":", "LOOP", b.zeroOrMore(statement), "END", "LOOP", b.optional(IDENTIFIER))));
		b.rule(throwStatement).is("THROW", b.optional("USER"), "EXCEPTION",
				b.optional(b.sequence("SEVERITY", expression)), b.optional(b.sequence("CATALOG", expression)),
				b.optional(b.sequence("MESSAGE", expression)),
				b.optional(b.sequence("VALUES", "(", expression, b.zeroOrMore(",", expression), ")")));
		b.rule(whileStatement).is(
				b.sequence(b.firstOf("WHILE", b.sequence(LABEL, ":", "WHILE"/*
																				 * ,
																				 * b
																				 * .
																				 * optional
																				 * (
																				 * identifier
																				 * )
																				 */)), condition, "DO",
						b.zeroOrMore(statement), "END", "WHILE", b.optional(IDENTIFIER)));
		b.rule(namespace).is(IDENTIFIER, b.optional(b.sequence(".", IDENTIFIER)));
		b.rule(caseStatement).is("CASE", b.firstOf(b.oneOrMore(whenClause), b.sequence(fieldReference, b.oneOrMore(whenClause))), 
				b.optional(b.sequence("ELSE", b.zeroOrMore(statement))), "END", "CASE");
		b.rule(whenClause).is("WHEN", expression, "THEN", b.zeroOrMore(statement));
		b.rule(leaveStatement).is("LEAVE", LABEL);
		b.rule(iterateStatement).is("ITERATE", LABEL);
		b.rule(repeatStatement).is(b.optional(b.sequence(LABEL, ":")), "REPEAT", b.zeroOrMore(statement), "UNTIL",
				condition, "END", "REPEAT", b.optional(IDENTIFIER));
	}

	private static void expression(LexerfulGrammarBuilder b) {
		b.rule(primaryExpression).is(
				b.firstOf(intervalLiteral, LITERAL, IDENTIFIER, arrayLiteral,
						b.sequence("(",subtractiveExpression, ")", intervalQualifier), listLiteral, timeLiteral, dateLiteral,
						b.sequence("(", expression, ")")));
		b.rule(arrayLiteral).is("[", b.zeroOrMore(b.firstOf(",", expression)), "]");
		b.rule(fieldReference).is(primaryExpression, b.zeroOrMore(b.sequence(".", pathElement)));
		b.rule(pathElement).is(
				b.optional(b.sequence("(", primaryExpression, b.zeroOrMore(b.sequence(".", primaryExpression)), ")")),
				b.optional(b.sequence(b.optional(b.firstOf(namespace, b.sequence("{", expression, "}"), "*")), ":")),
				b.firstOf(b.sequence(b.firstOf(FIELD_NAME, b.sequence("{", expression, "}"), "*")
				,b.optional(index)),index) );
		b.rule(index).is("[", b.sequence(b.optional(b.firstOf("<", ">")), b.optional(expression)), "]");

		b.rule(callExpression)
				.is(b.firstOf(
						function,
						b.sequence(fieldReference, "(",
								b.optional(expression, b.zeroOrMore(b.sequence(",", expression))), ")"), fieldReference));

		b.rule(leftHandSideExpression).is(b.firstOf(callExpression, b.sequence("-", callExpression)));
		b.rule(unaryExpression).is(b.firstOf(b.sequence("NOT", unaryExpression),leftHandSideExpression));
		b.rule(multiplicativeExpression).is(unaryExpression, b.zeroOrMore(b.firstOf("*", "/"), leftHandSideExpression))
				.skipIfOneChild();
		b.rule(additiveExpression)
		.is(multiplicativeExpression, b.zeroOrMore(b.firstOf("+", "-", "||"), multiplicativeExpression))
		.skipIfOneChild();
		b.rule(subtractiveExpression)
		.is(multiplicativeExpression, "-", multiplicativeExpression)
		.skipIfOneChild();
		b.rule(relationalExpression)
				.is(b.firstOf(additiveExpression, subtractiveExpression),
						b.firstOf(
								b.sequence(b.firstOf(b.sequence("NOT", "IN"), "IN"), "(", b.firstOf(additiveExpression, subtractiveExpression),
										b.zeroOrMore(b.sequence(",", b.firstOf(additiveExpression, subtractiveExpression))), ")"),
										b.sequence(b.firstOf(b.sequence("NOT", "BETWEEN"), "BETWEEN"), b.optional(b.firstOf("ASYMMETRIC", "SYMMETRIC")), b.firstOf(additiveExpression, subtractiveExpression), "AND", b.firstOf(additiveExpression, subtractiveExpression)),
								b.zeroOrMore(
										b.firstOf("<", ">", "<=", ">=", b.sequence("IS", b.optional("NOT")),
												b.sequence(b.optional("NOT"), "LIKE")), b.firstOf(additiveExpression, subtractiveExpression))))
				.skipIfOneChild();
//		b.rule(relationalExpressionNoIn)
//				.is(additiveExpression,
//						b.zeroOrMore(
//								b.firstOf("<", ">", "<=", ">=", b.sequence("IS", b.optional("NOT")),
//										b.sequence(b.optional("NOT"), "LIKE")), additiveExpression)).skipIfOneChild();

		b.rule(equalityExpression).is(relationalExpression, b.zeroOrMore(b.firstOf("=", "<>"), relationalExpression))
				.skipIfOneChild();
//		b.rule(equalityExpressionNoIn)
//				.is(relationalExpressionNoIn, b.zeroOrMore(b.firstOf("=", "<>"), relationalExpressionNoIn))
//				.skipIfOneChild();

		b.rule(logicalAndExpression).is(equalityExpression, b.zeroOrMore("AND", equalityExpression)).skipIfOneChild();
//		b.rule(logicalAndExpressionNoIn).is(equalityExpressionNoIn, b.zeroOrMore("AND", equalityExpressionNoIn))
//				.skipIfOneChild();

		b.rule(logicalOrExpression).is(logicalAndExpression, b.zeroOrMore("OR", logicalAndExpression)).skipIfOneChild();
//		b.rule(logicalOrExpressionNoIn).is(logicalAndExpressionNoIn, b.zeroOrMore("OR", logicalAndExpressionNoIn))
//				.skipIfOneChild();

		/*b.rule(assignmentExpression)
				.is(b.firstOf(b.sequence(leftHandSideExpression,
						b.optional(b.firstOf("TYPE", "NAMESPACE", "NAME", "VALUE")), "=",
						assignmentExpression, b.optional(intervalQualifier)), logicalOrExpression)).skipIfOneChild();
		b.rule(assignmentExpressionNoIn)
				.is(b.firstOf(b.sequence(leftHandSideExpression, "=", assignmentExpressionNoIn),
						logicalOrExpressionNoIn)).skipIfOneChild();*/

		b.rule(expression).is(logicalOrExpression);
		b.rule(condition).is(expression);

	}

	/**
	 * Functions as defined by the IBM documentation:
	 * http://publib.boulder.ibm.com
	 * /infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak19550_.htm
	 */
	private static void functions(LexerfulGrammarBuilder b) {
		b.rule(function).is(
				b.firstOf(databaseStateFunction, datetimeFunction, numericFunction, stringManipulationFunction,
						fieldFunction, listFunction, complexFunction, miscellaneousFunction));
		b.rule(numericFunction).is(ROUND_FUNCTION);
		b.rule(databaseStateFunction).is(b.nothing());
		b.rule(miscellaneousFunction).is(passthruFunction);
		// intervalFunction
		databaseStateFunction(b);
		datetimeFunctions(b);
		numericFunctions(b);
		listFunctions(b);
		fieldFunctions(b);
		complexFunctions(b);
		stringManipulationFunctions(b);
		miscellaneousFunctions(b);

	}

	/**
	 * Datetime functions:
	 * http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0
	 * /topic/com.ibm.etools.mft.doc/ak05410_.htm EXTRACT:
	 * http://publib.boulder.
	 * ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools
	 * .mft.doc/ak05420_.htm CURRENT_DATE:
	 * http://publib.boulder.ibm.com/infocenter
	 * /wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05430_.htm CURRENT_TIME:
	 * http ://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.
	 * etools .mft.doc/ak05440_.htm CURRENT_TIMESTAMP:
	 * http://publib.boulder.ibm.com/infocenter
	 * /wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05450_.htm
	 * CURRENT_GMTDATE:
	 * http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic
	 * /com.ibm.etools.mft.doc/ak05460_.htm CURRENT_GMTTIME:
	 * http://publib.boulder
	 * .ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools
	 * .mft.doc/ak05470_.htm CURRENT_GMTTIMESTAMP:
	 * http://publib.boulder.ibm.com/
	 * infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05480_.htm
	 * LOCAL_TIMEZONE:
	 * http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic
	 * /com.ibm.etools.mft.doc/ak05490_.htm
	 */
	private static void datetimeFunctions(LexerfulGrammarBuilder b) {
		b.rule(datetimeFunction).is(extractFunction);
		b.rule(extractFunction).is(
				"EXTRACT",
				"(",
				b.firstOf("DAYS", "DAYOFYEAR", "DAYOFWEEK", "YEAR", "MONTH", "DAY", "HOUR", "MINUTE", "SECOND",
						"MONTHS", "QUARTER", "QUARTERS", "WEEKS", "WEEKOFYEAR", "WEEKOFMONTH", "ISLEAPYEAR"), "FROM",
				expression, ")");
		// The other function do not need to be specified.
	}
	
	private static void numericFunctions(LexerfulGrammarBuilder b) {
		b.rule(ROUND_FUNCTION).is("ROUND", "(", expression, ",", expression, 
				b.optional(b.sequence("MODE", b.firstOf("ROUND_UP", "ROUND_DOWN", "ROUND_CEILING", 
						"ROUND_FLOOR", "ROUND_HALF_UP", "ROUND_HALF_EVEN", "ROUND_HALF_DOWN"))), ")");
	}

	/**
	 * Database state functions:
	 * http://publib.boulder.ibm.com/infocenter/wmbhelp
	 * /v6r1m0/topic/com.ibm.etools.mft.doc/ak05850_.htm SQLCODE:
	 * http://publib.boulder
	 * .ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools
	 * .mft.doc/ak17960_.htm SQLERRORTEXT:
	 * http://publib.boulder.ibm.com/infocenter
	 * /wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak17970_.htm SQLNATIVEERROR:
	 * http ://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.
	 * etools .mft.doc/ak17980_.htm SQLSTATE:
	 * http://publib.boulder.ibm.com/infocenter/wmbhelp
	 * /v6r1m0/topic/com.ibm.etools.mft.doc/ak17990_.htm
	 */
	private static void databaseStateFunction(LexerfulGrammarBuilder b) {

	}

	/**
	 * Grammar of the listFunctions:
	 * 
	 */
	private static void listFunctions(LexerfulGrammarBuilder b) {
		b.rule(listFunction).is(theFunction);
		b.rule(theFunction).is("THE", "(", expression, ")");
	}

	private static void fieldFunctions(LexerfulGrammarBuilder b) {
		b.rule(fieldFunction).is(b.firstOf(asbitstreamFunction, forFunction));
		b.rule(asbitstreamFunction).is("ASBITSTREAM", "(", fieldReference, b.zeroOrMore(bitStreamOptions), ")");
		b.rule(bitStreamOptions).is(b.firstOf("OPTIONS", "ENCODING", "CCSID", "SET", "TYPE", "FORMAT"), expression);
		b.rule(forFunction).is("FOR", b.optional(b.firstOf("ALL", "ANY", "SOME")), fieldReference, b.optional("AS", IDENTIFIER), b.zeroOrMore(",", fieldReference, b.optional("AS", IDENTIFIER)), "(", expression ,")");
	}

	private static void stringManipulationFunctions(LexerfulGrammarBuilder b) {
		b.rule(stringManipulationFunction).is(
				b.firstOf(/*
						 * containsFunction, endswithFunction, leftFunction,
						 * lengthFunction, lowerFunction, lcaseFunction,
						 * "<"rimFunction,
						 */overlayFunction, positionFunction, /*
															 * replaceFunction,
															 * replicateFunction
															 * , rightFunction
															 * rtrimFunction,
															 * spaceFunction,
															 */startswithFunction, substringFunction, translateFunction
																									  ,
																									 trimFunction, /*
																													 * upperFunction
																													 * ,
																													 */
						ucaseFunction));
		b.rule(overlayFunction).is("OVERLAY", "(", expression, "PLACING", expression, "FROM", expression,
				b.optional(b.sequence("FOR", expression)), ")");
		b.rule(positionFunction).is("POSITION", "(", expression, "IN", expression,
				b.optional(b.sequence("FROM", expression)), b.optional(b.sequence("REPEAT", expression)), ")");
		b.rule(trimFunction).is(
				"TRIM",
				"(",
				b.optional(b.sequence(b.firstOf(
						b.sequence(b.firstOf("BOTH", "LEADING", "TRAILING"), b.optional(expression)), expression),
						"FROM")), expression, ")");
		b.rule(ucaseFunction).is(b.firstOf("UPPER", "UCASE"), "(", fieldReference, ")");
		b.rule(startswithFunction).is("STARTSWITH", "(", expression, ",", expression, ")");
		b.rule(substringFunction).is("SUBSTRING", "(", expression, b.firstOf("FROM", "BEFORE", "AFTER"), expression,
				b.optional(b.sequence("FOR", expression)), ")");
		b.rule(translateFunction).is("TRANSLATE", "(",expression,",",expression,b.optional(expression),")");
	}

	private static void complexFunctions(LexerfulGrammarBuilder b) {
		b.rule(complexFunction).is(b.firstOf(castFunction, caseFunction, selectFunction, rowConstructorFunction));
		b.rule(castFunction).is("CAST", "(", expression, b.zeroOrMore(",", expression), "AS", dataType,
				b.optional(b.sequence("CCSID", expression)), b.optional(b.sequence("ENCODING", expression)),
				b.optional(b.sequence("FORMAT", expression)), b.optional(b.sequence("DEFAULT", expression)), ")");
		b.rule(caseFunction).is("CASE", b.firstOf(searchedWhenClause, simpleWhenClause),
				b.optional(b.sequence("ELSE", b.firstOf("NULL", expression))), "END");
		b.rule(simpleWhenClause).is(expression,
				b.oneOrMore(b.sequence("WHEN", expression, "THEN", b.firstOf(expression, "NULL"))));
		b.rule(searchedWhenClause).is(
				b.oneOrMore(b.sequence("WHEN", condition, "THEN", b.firstOf(expression, "NULL"))));
		b.rule(selectFunction).is("SELECT", selectClause, fromClause, b.optional(whereClause));
		b.rule(selectClause).is(
				b.firstOf(
						b.sequence("ITEM", expression),
						b.sequence(b.firstOf("COUNT", "MIN", "MAX", "SUM"), "(", b.firstOf(expression, "*"), ")"),
						b.sequence(
								b.firstOf(expression, "*"),
								b.optional(b.firstOf(b.sequence("AS", fieldReference), "INSERT")),
								b.zeroOrMore(",", b.firstOf(expression, "*"),
										b.optional(b.firstOf(b.sequence("AS", fieldReference), "INSERT"))))));
		b.rule(whereClause).is("WHERE", expression);
		b.rule(rowConstructorFunction).is("ROW", "(", expression , b.optional("AS", fieldReference), b.zeroOrMore(",", expression , b.optional("AS", fieldReference)), ")");
	}
	private static void miscellaneousFunctions(LexerfulGrammarBuilder b) {
		b.rule(passthruFunction).is("PASSTHRU", "(", expression, b.optional("TO", expression, ".", expression), "VALUES", "(", expression,b.zeroOrMore(",", expression), ")", ")");
	}
}
