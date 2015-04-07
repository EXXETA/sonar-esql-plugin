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

import static org.sonar.sslr.parser.GrammarOperators.endOfInput;
import static org.sonar.sslr.parser.GrammarOperators.firstOf;
import static org.sonar.sslr.parser.GrammarOperators.next;
import static org.sonar.sslr.parser.GrammarOperators.nextNot;
import static org.sonar.sslr.parser.GrammarOperators.oneOrMore;
import static org.sonar.sslr.parser.GrammarOperators.optional;
import static org.sonar.sslr.parser.GrammarOperators.regexp;
import static org.sonar.sslr.parser.GrammarOperators.sequence;
import static org.sonar.sslr.parser.GrammarOperators.token;
import static org.sonar.sslr.parser.GrammarOperators.zeroOrMore;
import static org.sonar.sslr.parser.GrammarOperators.nothing;




import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlKeyword;
import com.exxeta.iss.sonar.esql.api.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.api.EsqlTokenType;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;
import com.sonar.sslr.api.GenericTokenType;

public class EsqlGrammarImpl extends EsqlGrammar {
	public EsqlGrammarImpl() {
		eos.is(firstOf(optional(semi), next(eof)));
		lexical();
		expression();
		literal.is(firstOf(nullLiteral, booleanLiteral, numericLiteral, hexLiteral, stringLiteral));
		nullLiteral.is(nullKeyword);
		booleanLiteral.is(firstOf(trueKeyword, falseKeyword));
		functionsAndPrograms();
	}

	private void keywords() {
		// Reserved words

		createKeyword.is(keyword("CREATE")).skip();
		computeKeyword.is(keyword("COMPUTE")).skip();
		moduleKeyword.is(keyword("MODULE")).skip();
		endKeyword.is(keyword("END")).skip();
		functionKeyword.is(keyword("FUNCTION")).skip();
		procedureKeyword.is(keyword("PROCEDURE")).skip();
		inKeyword.is(keyword("IN")).skip();
		outKeyword.is(keyword("OUT")).skip();
		inoutKeyword.is(keyword("INOUT")).skip();
		nameKeyword.is(keyword("NAME")).skip();
		namespaceKeyword.is(keyword("NAMESPACE")).skip();
		constantKeyword.is(keyword("CONSTANT")).skip();
		returnsKeyword.is(keyword("RETURNS")).skip();
		esqlKeyword.is(keyword("ESQL")).skip();
		javaKeyword.is(keyword("JAVA")).skip();
		databaseKeyword.is(keyword("DATABASE")).skip();
		dynamicKeyword.is(keyword("DYNAMIC")).skip();
		resultKeyword.is(keyword("RESULT")).skip();
		setsKeyword.is(keyword("SETS")).skip();
		booleanKeyword.is(keyword("BOOLEAN")).skip();
		intKeyword.is(keyword("INT")).skip();
		integerKeyword.is(keyword("INTEGER")).skip();
		floatKeyword.is(keyword("FLOAT")).skip();
		decimalKeyword.is(keyword("DECIMAL")).skip();
		dateKeyword.is(keyword("DATE")).skip();
		timeKeyword.is(keyword("TIME")).skip();
		timestampKeyword.is(keyword("TIMESTAMP")).skip();
		gmttimeKeyword.is(keyword("GMTTIME")).skip();
		gmttimestampKeyword.is(keyword("GMTTIMESTAMP")).skip();
		intervalKeyword.is(keyword("INTERVAL")).skip();
		charKeyword.is(keyword("CHAR")).skip();
		characterKeyword.is(keyword("CHARACTER")).skip();
		blobKeyword.is(keyword("BLOB")).skip();
		bitKeyword.is(keyword("BIT")).skip();
		rowKeyword.is(keyword("ROW")).skip();
		externalKeyword.is(keyword("EXTERNAL")).skip();
		beginKeyword.is(keyword("BEGIN")).skip();
		notKeyword.is(keyword("NOT")).skip();
		atomicKeyword.is(keyword("ATOMIC")).skip();
		callKeyword.is(keyword("CALL")).skip();
		intoKeyword.is(keyword("INTO")).skip();
		schemaKeyword.is(keyword("SCHEMA")).skip();
		betweenKeyword.is(keyword("BETWEEN")).skip();
		asymmetricKeyword.is(keyword("ASYMMETRIC")).skip();
		symmetricKeyword.is(keyword("SYMMETRIC")).skip();
		existsKeyword.is(keyword("EXISTS")).skip();
		isKeyword.is(keyword("IS")).skip();
		likeKeyword.is(keyword("LIKE")).skip();
		escapeKeyword.is(keyword("ESCAPE")).skip();
		singularKeyword.is(keyword("SINGULAR")).skip();
		trueKeyword.is(keyword("TRUE")).skip();
		falseKeyword.is(keyword("FALSE")).skip();
		infKeyword.is(keyword("INF")).skip();
		infinityKeyword.is(keyword("INFINITY")).skip();
		nanKeyword.is(keyword("NAN")).skip();
		nullKeyword.is(keyword("NULL")).skip();
		numKeyword.is(keyword("NUM")).skip();
		numberKeyword.is(keyword("NUMBER")).skip();
		unknownKeyword.is(keyword("UNKNOWN")).skip();
		andKeyword.is(keyword("AND")).skip();
		fieldKeyword.is(keyword("FIELD")).skip();
		previousSiblingKeyword.is(keyword("PREVIOUSSIBLING")).skip();
		nextSiblingKeyword.is(keyword("NEXTSIBLING")).skip();
		firstChildKeyword.is(keyword("FIRSTCHILD")).skip();
		lastChildKeyword.is(keyword("LASTCHILD")).skip();
		ofKeyword.is(keyword("OF")).skip();
		asKeyword.is(keyword("AS")).skip();
		domainKeyword.is(keyword("DOMAIN")).skip();
		repeatKeyword.is(keyword("REPEAT")).skip();
		valueKeyword.is(keyword("VALUE")).skip();
		identityKeyword.is(keyword("IDENTITY")).skip();
		typeKeyword.is(keyword("TYPE")).skip();
		setKeyword.is(keyword("SET")).skip();
		declareKeyword.is(keyword("DECLARE")).skip();
		sharedKeyword.is(keyword("SHARED")).skip();
		referenceKeyword.is(keyword("REFERENCE")).skip();
		toKeyword.is(keyword("TO")).skip();
		returnKeyword.is(keyword("RETURN")).skip();
		whileKeyword.is(keyword("WHILE")).skip();
		doKeyword.is(keyword("DO")).skip();
		orKeyword.is(keyword("OR")).skip();
		ifKeyword.is(keyword("IF")).skip();
		thenKeyword.is(keyword("THEN")).skip();
		elseKeyword.is(keyword("ELSE")).skip();
		elseifKeyword.is(keyword("ELSEIF")).skip();
		filterKeyword.is(keyword("FILTER")).skip();
		propagateKeyword.is(keyword("PROPAGATE")).skip();
		terminalKeyword.is(keyword("TERMINAL")).skip();
		labelKeyword.is(keyword("LABEL")).skip();
		finalizeKeyword.is(keyword("FINALIZE")).skip();
		defaultKeyword.is(keyword("DEFAULT")).skip();
		noneKeyword.is(keyword("NONE")).skip();
		deleteKeyword.is(keyword("DELETE")).skip();
		environmentKeyword.is(keyword("ENVIRONMENT")).skip();
		messageKeyword.is(keyword("MESSAGE")).skip();
		exceptionKeyword.is(keyword("EXCEPTION")).skip();
		castKeyword.is(keyword("CAST")).skip();
		ccsidKeyword.is(keyword("CCSID")).skip();
		encodingKeyword.is(keyword("ENCODING")).skip();
		formatKeyword.is(keyword("FORMAT")).skip();
		yearKeyword.is(keyword("YEAR")).skip();
		monthKeyword.is(keyword("MONTH")).skip();
		dayKeyword.is(keyword("DAY")).skip();
		hourKeyword.is(keyword("HOUR")).skip();
		minuteKeyword.is(keyword("MINUTE")).skip();
		secondKeyword.is(keyword("SECOND")).skip();
		dayofweekKeyword.is(keyword("DAYOFWEEK")).skip();
		monthsKeyword.is(keyword("MONTHS")).skip();
		quarterKeyword.is(keyword("QUARTER")).skip();
		quartersKeyword.is(keyword("QUARTERS")).skip();
		weeksKeyword.is(keyword("WEEKS")).skip();
		weekOfYearKeyword.is(keyword("WEEKOFYEAR")).skip();
		weekOfMonthKeyword.is(keyword("WEEKOFMONTH")).skip();
		isLeapYearKeyweord.is(keyword("ISLEAPYEAR")).skip();
		daysKeyword.is(keyword("DAYS")).skip();
		dayOfYearKeyword.is(keyword("DAYOFYEAR")).skip();

		forKeyword.is(keyword("FOR")).skip();
		upperKeyword.is(keyword("UPPER")).skip();
		ucaseKeyword.is(keyword("UCASE")).skip();
		startswithKeyword.is(keyword("STARTSWITH")).skip();
		substringKeyword.is(keyword("SUBSTRING")).skip();
		fromKeyword.is(keyword("FROM")).skip();
		beforeKeyword.is(keyword("BEFORE")).skip();
		afterKeyword.is(keyword("AFTER")).skip();
		trimKeyword.is(keyword("TRIM")).skip();
		bothKeyword.is(keyword("BOTH")).skip();
		leadingKeyword.is(keyword("LEADING")).skip();
		trailingKeyword.is(keyword("TRAILING")).skip();
		moveKeyword.is(keyword("MOVE")).skip();
		parentKeyword.is(keyword("PARENT")).skip();
		positionKeyword.is(keyword("POSITION")).skip();
		throwKeyword.is(keyword("THROW")).skip();
		userKeyword.is(keyword("USER")).skip();
		severityKeyword.is(keyword("SEVERITY")).skip();
		catalogKeyword.is(keyword("CATALOG")).skip();
		valuesKeyword.is(keyword("VALUES")).skip();
		asbitstreamKeyword.is(keyword("ASBITSTREAM")).skip();
		optionsKeyword.is(keyword("OPTIONS")).skip();
		selectKeyword.is(keyword("SELECT")).skip();
		whereKeyword.is(keyword("WHERE")).skip();
		parseKeyword.is(keyword("PARSE")).skip();
		languageKeyword.is(keyword("LANGUAGE")).skip();
		detachKeyword.is(keyword("DETACH")).skip();
		passthruKeyword.is(keyword("PASSTHRU")).skip();
		insertKeyword.is(keyword("INSERT")).skip();
		theKeyword.is(keyword("THE")).skip();
		itemKeyword.is(keyword("ITEM")).skip();
		minKeyword.is(keyword("MIN")).skip();
		maxKeyword.is(keyword("MAX")).skip();
		countKeyword.is(keyword("COUNT")).skip();
		sumKeyword.is(keyword("SUM")).skip();
		updateKeyword.is(keyword("UPDATE")).skip();
		pathKeyword.is(keyword("PATH")).skip();
		brokerKeyword.is(keyword("BROKER")).skip();
		caseKeyword.is(keyword("CASE")).skip();
		whenKeyword.is(keyword("WHEN")).skip();
		continueKeyword.is(keyword("CONTINUE")).skip();
		exitKeyword.is(keyword("EXIT")).skip();
		handlerKeyword.is(keyword("HANDLER")).skip();
		sqlstateKeyword.is(keyword("SQLSTATE")).skip();
		extractKeyword.is(keyword("EXTRACT")).skip();
		placingKeyword.is(keyword("PLACING")).skip();
		overlayKeyword.is(keyword("OVERLAY")).skip();
		leaveKeyword.is(keyword("LEAVE")).skip();
		iterateKeyword.is(keyword("ITERATE")).skip();
		resignalKeyword.is(keyword("RESIGNAL")).skip();
		untilKeyword.is(keyword("UNTIL")).skip();
		logKeyword.is(keyword("LOG")).skip();
		eventKeyword.is(keyword("EVENT")).skip();
		traceKeyword.is(keyword("TRACE")).skip();
		fullKeyword.is(keyword("FULL")).skip();
		loopKeyword.is(keyword("LOOP")).skip();
		
		
	}

	private void punctuators() {

		lparenthesis.is(punctuator("(")).skip();
		rparenthesis.is(punctuator(")")).skip();
		lt.is(punctuator("<", nextNot(firstOf("=", ">")))).skip();
		gt.is(punctuator(">", nextNot("="))).skip();
		le.is(punctuator("<=")).skip();
		ge.is(punctuator(">=")).skip();
		equal.is(punctuator("=")).skip();
		notequal.is(punctuator("<>")).skip();
		plus.is(punctuator("+")).skip();
		minus.is(punctuator("-")).skip();
		start.is(punctuator("*")).skip();
		div.is(punctuator("/")).skip();
		concat.is(punctuator("||")).skip();
		comma.is(punctuator(",")).skip();
		colon.is(punctuator(":")).skip();
		semi.is(punctuator(";")).skip();
		dot.is(punctuator(".")).skip();
		lcurlybrace.is(punctuator("{")).skip();
		rcurlybrace.is(punctuator("}")).skip();
		lbracket.is(punctuator("[")).skip();
		rbracket.is(punctuator("]")).skip();
	}

	private Object keyword(String value) {
		for (EsqlKeyword tokenType : EsqlKeyword.values()) {
			if (value.equals(tokenType.getValue())) {
				return sequence(token(tokenType, value), nextNot(letterOrDigit), spacing);
			}
		}
		throw new IllegalStateException(value);
	}

	private Object punctuator(String value) {
		for (EsqlPunctuator tokenType : EsqlPunctuator.values()) {
			if (value.equals(tokenType.getValue())) {
				return sequence(token(tokenType, value), spacing);
			}
		}
		throw new IllegalStateException(value);
	}

	private Object punctuator(String value, Object element) {
		for (EsqlPunctuator tokenType : EsqlPunctuator.values()) {
			if (value.equals(tokenType.getValue())) {
				return sequence(token(tokenType, value), element, spacing);
			}
		}
		throw new IllegalStateException(value);
	}

	/**
	 * A.1 Lexical
	 */
	private void lexical() {
		eof.is(token(GenericTokenType.EOF, endOfInput())).skip();
		identifier.is(nextNot(keyword), token(GenericTokenType.IDENTIFIER, regexp(EsqlLexer.IDENTIFIER)), spacing);
		fieldName.is(token(GenericTokenType.IDENTIFIER, regexp(EsqlLexer.IDENTIFIER)), spacing);
		spacing.is(
				regexp(EsqlLexer.WHITESPACE + "*+"),
				zeroOrMore(token(GenericTokenType.COMMENT, regexp(EsqlLexer.COMMENT)), regexp(EsqlLexer.WHITESPACE
						+ "*+"))).skip();
		keyword.is(
				firstOf(

				"ALL", "ASYMETRIC", "BOTH", "CASE", "DISTINCT", "FROM", "ITEM", "LEADING", "NOT", "SYMMETRIC",
						"TRAILING", "WHEN"), nextNot(letterOrDigit));
		letterOrDigit.is(regexp("\\p{javaJavaIdentifierPart}"));
		numericLiteral.is(token(EsqlTokenType.NUMERIC_LITERAL, regexp(EsqlLexer.NUMERIC_LITERAL)), spacing);
		stringLiteral.is(token(GenericTokenType.LITERAL, regexp(EsqlLexer.LITERAL)), spacing);
		hexLiteral.is(token(GenericTokenType.LITERAL, regexp(EsqlLexer.HEX_LITERAL)), spacing);
		listLiteral.is(lparenthesis, literal, zeroOrMore(sequence(comma, literal)), rparenthesis);
		intervalLiteral.is(intervalKeyword, literal, intervalQualifier);
		punctuators();
		keywords();
	}

	/**
	 * A.5 Functions and Programs
	 */
	private void functionsAndPrograms() {
		program.is(spacing, optional(sequence(brokerKeyword, schemaKeyword, fieldReference)),
				optional(sequence(pathKeyword, fieldReference, zeroOrMore(comma, fieldReference))), sourceElements, eof);
		sourceElements.is(zeroOrMore(sourceElement));
		sourceElement.is(firstOf(moduleDeclaration, routineDeclaration, declareStatement), eos);

		moduleDeclaration.is(createKeyword, firstOf(computeKeyword, databaseKeyword, filterKeyword), moduleKeyword,
				identifier, moduleBody, endKeyword, moduleKeyword);
		moduleBody.is(optional(moduleStatements));
		moduleStatements.is(oneOrMore(sequence(moduleStatement, eos)));
		moduleStatement.is(firstOf(routineDeclaration, declareStatement));

		routineDeclaration
				.is(createKeyword, firstOf(functionKeyword, procedureKeyword), identifier, lparenthesis, parameterList,
						rparenthesis, optional(returnType), optional(language), optional(resultSet), routineBody);
		parameterList.is(optional(parameter, zeroOrMore(sequence(comma, parameter))));
		parameter.is(optional(firstOf(inKeyword, outKeyword, inoutKeyword)), expression,
				optional(firstOf(nameKeyword, namespaceKeyword, sequence(optional(constantKeyword), dataType))));
		returnType.is(returnsKeyword, dataType);
		language.is(sequence(languageKeyword, firstOf(esqlKeyword, databaseKeyword, javaKeyword)));
		resultSet.is(dynamicKeyword, resultKeyword, setsKeyword, numericLiteral);
		routineBody.is(firstOf(statement, sequence(externalKeyword, nameKeyword, expression)));
		datatypes();
		statements();
		functions();
	}

	private void datatypes() {
		dataType.is(firstOf(booleanKeyword, intKeyword, integerKeyword, floatKeyword,
				sequence(decimalKeyword, optional(lparenthesis, numericLiteral, comma, numericLiteral, rparenthesis)),
				dateKeyword, timeKeyword, timestampKeyword, gmttimeKeyword, gmttimestampKeyword, intervalDataType,
				charKeyword, characterKeyword, blobKeyword, bitKeyword, rowKeyword,
				sequence(referenceKeyword, optional(toKeyword))));
		intervalDataType.is(intervalKeyword, optional(intervalQualifier));
		intervalQualifier.is(firstOf(sequence(yearKeyword, optional(sequence(toKeyword, monthKeyword))), monthKeyword,
				sequence(dayKeyword, optional(toKeyword, firstOf(hourKeyword, minuteKeyword, secondKeyword))),
				sequence(hourKeyword, optional(sequence(toKeyword, firstOf(minuteKeyword, secondKeyword)))),
				sequence(minuteKeyword, optional(sequence(toKeyword, secondKeyword))), secondKeyword));
	}

	private void statements() {
		statement.is(
				firstOf(basicStatement, messageTreeManipulationStatement, nodeInteractionStatement,
						databaseUpdateStatement, otherStatement), eos);
		basicStatements();
		messageTreeManipulationStatements();
		databaseUpdateStatements();
		nodeInteractionStatements();
		otherStatements();
	}

	private void otherStatements() {
		otherStatement.is(firstOf(declareHandlerStatement, logStatement, resignalStatement));// TODO
		declareHandlerStatement.is(declareKeyword, firstOf(continueKeyword, exitKeyword), handlerKeyword, forKeyword,
				sqlState, zeroOrMore(comma, sqlState), statement);
		sqlState.is(
				sqlstateKeyword,
				firstOf(sequence(likeKeyword, stringLiteral, optional(sequence(escapeKeyword, stringLiteral))),
						sequence(optional(valueKeyword), stringLiteral)));
		logStatement.is(logKeyword, firstOf(eventKeyword, sequence(userKeyword, traceKeyword)), optional(sequence(optional(fullKeyword), exceptionKeyword)), optional(logOptions), optional(valuesKeyword, lparenthesis, expression, zeroOrMore(sequence(comma, expression)), rparenthesis ));
		logOptions.is(sequence(optional(sequence(severityKeyword, expression)), optional(sequence(catalogKeyword, expression)), optional(sequence(messageKeyword, expression))));
		resignalStatement.is(resignalKeyword);
	}

	private void nodeInteractionStatements() {
		nodeInteractionStatement.is(propagateStatement);
		propagateStatement.is(propagateKeyword,
				optional(sequence(toKeyword, firstOf(terminalKeyword, labelKeyword), expression)),
				optional(messageSource), optional(controlElement));
		messageSource.is(optional(sequence(environmentKeyword, expression)),
				optional(sequence(messageKeyword, expression)), optional(sequence(exceptionKeyword, expression)));
		controlElement.is(optional(sequence(finalizeKeyword, firstOf(defaultKeyword, noneKeyword))),
				optional(sequence(deleteKeyword, firstOf(defaultKeyword, noneKeyword))));
	}

	private void databaseUpdateStatements() {
		databaseUpdateStatement.is(firstOf(passthruExpression, insertExpression, updateExpression));// TODO
		updateExpression.is(updateKeyword, expression, optional(sequence(asKeyword, expression)), setKeyword,
				fieldReference, equal, expression, zeroOrMore(sequence(comma, fieldReference, equal, expression)),
				optional(whereClause));
		insertExpression.is(insertKeyword, intoKeyword, expression,
				optional(lparenthesis, expression, zeroOrMore(comma, expression), rparenthesis), valuesKeyword,
				lparenthesis, expression, zeroOrMore(comma, expression), rparenthesis);
		passthruExpression
				.is(passthruKeyword,
						firstOf(sequence(lparenthesis, expression, zeroOrMore(comma, expression), rparenthesis),
								expression,
								optional(sequence(toKeyword, expression, dot, expression)),
								optional(valuesKeyword, lparenthesis, expression, zeroOrMore(comma, expression),
										rparenthesis)));
	}

	private void messageTreeManipulationStatements() {
		messageTreeManipulationStatement.is(firstOf(createStatement, deleteStatement, detachStatement, forStatement,
				moveStatement));
		createStatement.is(createKeyword, qualificationIdentifier, fieldReference,
				optional(sequence(asKeyword, identifier)), optional(sequence(domainKeyword, expression)),
				optional(firstOf(repeatClause, valuesClauses, fromClause, parseClause)));//
		qualificationIdentifier.is(firstOf(
				fieldKeyword,
				sequence(firstOf(previousSiblingKeyword, nextSiblingKeyword, firstChildKeyword, lastChildKeyword),
						ofKeyword)));
		repeatClause.is(sequence(repeatKeyword, optional(valueKeyword, identifier)));
		valuesClauses.is(firstOf(sequence(namesClauses, optional(valueExpression)), valueExpression));
		namesClauses.is(firstOf(
				sequence(identityKeyword, identifier),
				firstOf(sequence(typeKeyword, expression, optional(namespaceKeyword, expression),
						optional(nameKeyword, expression)),
						sequence(namespaceKeyword, expression, optional(nameKeyword, expression)),
						sequence(nameKeyword, expression))));
		valueExpression.is(sequence(valueKeyword, expression)); // TODO
																// optional(namesClause),optional(
		fromClause.is(
				fromKeyword,
				fieldReference,
				optional(sequence(asKeyword, identifier),
						zeroOrMore(sequence(comma, fieldReference, optional(sequence(asKeyword, identifier))))));
		parseClause.is(
				parseKeyword,
				lparenthesis,
				expression,
				zeroOrMore(sequence(comma,expression)),
				zeroOrMore(sequence(
						firstOf(encodingKeyword, ccsidKeyword, setKeyword, typeKeyword, formatKeyword, optionsKeyword),
						expression)),
				rparenthesis);
		detachStatement.is(detachKeyword, fieldReference);
		forStatement.is(forKeyword, identifier, asKeyword, fieldReference, doKeyword, oneOrMore(statement), endKeyword,
				forKeyword);
		moveStatement
				.is(moveKeyword,
						fieldReference,
						firstOf(sequence(toKeyword, fieldReference),
								parentKeyword,
								sequence(
										firstOf(firstChildKeyword, lastChildKeyword, previousSiblingKeyword,
												nextSiblingKeyword), nameClause)));
		nameClause.is(firstOf(
				sequence(repeatKeyword, firstOf(sequence(typeKeyword, nameKeyword), typeKeyword, nameKeyword)),
				sequence(optional(sequence(typeKeyword, expression)),
						optional(sequence(namespaceKeyword, firstOf(start, expression))),
						optional(sequence(nameKeyword, expression))), sequence(identityKeyword, pathElement)));
		deleteStatement.is(
				deleteKeyword,
				firstOf(sequence(fromKeyword, fieldReference, optional(sequence(asKeyword, expression)),
						optional(sequence(whereKeyword, expression))),
						sequence(
								firstOf(fieldKeyword,
										sequence(
												firstOf(firstChildKeyword, lastChildKeyword, previousSiblingKeyword,
														nextSiblingKeyword), ofKeyword)), fieldReference)));
	}

	private void basicStatements() {
		basicStatement.is(firstOf(beginEndStatement, callStatement, caseStatement, declareStatement, ifStatement, 
				iterateStatement, leaveStatement, loopStatement, repeatStatement, returnStatement, setStatement,
				throwStatement, whileStatement));
		beginEndStatement
				.is(sequence(optional(sequence(identifier, colon)), beginKeyword,
						optional(optional(notKeyword), atomicKeyword), zeroOrMore(statement), endKeyword,
						optional(identifier)));
		callStatement.is(callKeyword, zeroOrMore(sequence(identifier,dot)),identifier, lparenthesis, parameterList, rparenthesis,
				optional(qualifier), optional(sequence(intoKeyword, identifier)));
		qualifier
				.is(firstOf(sequence(inKeyword, fieldReference), sequence(externalKeyword, schemaKeyword, identifier)));
		declareStatement.is(declareKeyword, identifier, zeroOrMore(sequence(comma, identifier)),
				optional(firstOf(sharedKeyword, externalKeyword)),
				firstOf(sequence(optional(constantKeyword), dataType), namespaceKeyword, nameKeyword),
				optional(expression));
		setStatement.is(setKeyword, assignmentExpression);
		returnStatement.is(returnKeyword, optional(logicalOrExpression));
		ifStatement.is(ifKeyword, condition, thenKeyword, zeroOrMore(statement),
				zeroOrMore(sequence(elseifKeyword, condition, thenKeyword, zeroOrMore(statement))),
				optional(elseKeyword, zeroOrMore(statement)), endKeyword, ifKeyword);
		loopStatement.is(firstOf(sequence(loopKeyword, zeroOrMore(statement), endKeyword, loopKeyword),sequence(identifier, colon, loopKeyword, zeroOrMore(statement), endKeyword, loopKeyword, identifier)));
		throwStatement
				.is(throwKeyword,
						optional(userKeyword),
						exceptionKeyword,
						optional(sequence(severityKeyword, expression)),
						optional(sequence(catalogKeyword, expression)),
						optional(sequence(messageKeyword, expression)),
						optional(sequence(valuesKeyword, lparenthesis, expression, zeroOrMore(comma, expression),
								rparenthesis)));
		whileStatement.is(sequence(
				firstOf(whileKeyword, sequence(identifier, colon, whileKeyword/*, optional(identifier)*/)), condition,
				doKeyword, zeroOrMore(statement), endKeyword, whileKeyword, optional(identifier)));
		condition.is(expression);
		namespace.is(identifier, optional(sequence(dot, identifier)));
		caseStatement.is(caseKeyword, optional(expression), oneOrMore(whenClause),
				optional(sequence(elseKeyword, zeroOrMore(statement))), endKeyword, caseKeyword);
		whenClause.is(whenKeyword, expression, thenKeyword, zeroOrMore(statement));
		leaveStatement.is(leaveKeyword, identifier);
		iterateStatement.is(iterateKeyword, identifier);
		repeatStatement.is(optional(sequence(identifier, colon)), repeatKeyword, zeroOrMore(statement), untilKeyword, condition, endKeyword, repeatKeyword, optional(identifier));
	}

	private void expression() {
		primaryExpression.is(firstOf(intervalLiteral, literal, identifier, arrayLiteral, listLiteral,
				sequence(lparenthesis, expression, rparenthesis)));
		arrayLiteral.is(lbracket, zeroOrMore(firstOf(comma, assignmentExpression)), rbracket);
		fieldReference.is(primaryExpression, zeroOrMore(sequence(dot, pathElement)));
		pathElement.is(
				optional(sequence(lparenthesis, primaryExpression, zeroOrMore(sequence(dot, primaryExpression)),
						rparenthesis)),
				optional(sequence(optional(firstOf(namespace, sequence(lcurlybrace, expression, rcurlybrace), start)), colon)),
				optional(firstOf(primaryExpression, sequence(lcurlybrace, expression, rcurlybrace), start, fieldName)),
				optional(index));
		index.is(lbracket, sequence(optional(firstOf(lt, gt)), optional(expression)), rbracket);

		callExpression.is(firstOf(
				function,
				sequence(fieldReference, lparenthesis, optional(expression, zeroOrMore(sequence(comma, expression))),
						rparenthesis), fieldReference));

		leftHandSideExpression.is(firstOf(callExpression, sequence(minus, callExpression)));
		unaryExpression.is(firstOf(leftHandSideExpression, sequence(notKeyword, unaryExpression)));
		multiplicativeExpression.is(unaryExpression, zeroOrMore(firstOf(start, div), leftHandSideExpression))
				.skipIfOneChild();
		additiveExpression.is(multiplicativeExpression,
				zeroOrMore(firstOf(plus, minus, concat), multiplicativeExpression)).skipIfOneChild();
		relationalExpression.is(
				additiveExpression,firstOf(sequence(optional(notKeyword), inKeyword, lparenthesis,additiveExpression,zeroOrMore(sequence(comma,additiveExpression)),rparenthesis),
				zeroOrMore(
						firstOf(lt, gt, le, ge, sequence(isKeyword, optional(notKeyword)), 
								sequence(optional(notKeyword), likeKeyword)), additiveExpression))
				).skipIfOneChild();
		relationalExpressionNoIn.is(
				additiveExpression,
				zeroOrMore(
						firstOf(lt, gt, le, ge, sequence(isKeyword, optional(notKeyword)),
								sequence(optional(notKeyword), likeKeyword)), additiveExpression)
				).skipIfOneChild();

		equalityExpression.is(relationalExpression, zeroOrMore(firstOf(equal, notequal), relationalExpression))
				.skipIfOneChild();
		equalityExpressionNoIn.is(relationalExpressionNoIn,
				zeroOrMore(firstOf(equal, notequal), relationalExpressionNoIn)).skipIfOneChild();

		logicalAndExpression.is(equalityExpression, zeroOrMore(andKeyword, equalityExpression)).skipIfOneChild();
		logicalAndExpressionNoIn.is(equalityExpressionNoIn, zeroOrMore(andKeyword, equalityExpressionNoIn))
				.skipIfOneChild();

		logicalOrExpression.is(logicalAndExpression, zeroOrMore(orKeyword, logicalAndExpression)).skipIfOneChild();
		logicalOrExpressionNoIn.is(logicalAndExpressionNoIn, zeroOrMore(orKeyword, logicalAndExpressionNoIn))
				.skipIfOneChild();

		assignmentExpression.is(
				firstOf(sequence(leftHandSideExpression,
						optional(firstOf(typeKeyword, namespaceKeyword, nameKeyword, valueKeyword)),
						assignmentOperator, assignmentExpression, optional(intervalQualifier)), logicalOrExpression)).skipIfOneChild();
		assignmentExpressionNoIn.is(
				firstOf(sequence(leftHandSideExpression, assignmentOperator, assignmentExpressionNoIn),
						logicalOrExpressionNoIn)).skipIfOneChild();

		assignmentOperator.is(firstOf(equal));

		expression.is(firstOf(assignmentExpression));
	}

	/**
	 * Functions as defined by the IBM documentation:
	 * http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak19550_.htm
	 */
	private void functions() {
		function.is(firstOf(databaseStateFunction, datetimeFunction, numericFunction, stringManipulationFunction, fieldFunction, listFunction, complexFunction, miscellaneousFunction));
		numericFunction.is(nothing());
		databaseStateFunction.is(nothing());
		miscellaneousFunction.is(nothing());
		//intervalFunction
		databaseStateFunction();
		datetimeFunctions();
		listFunctions();
		fieldFunctions();
		complexFunctions();
		stringManipulationFunctions();

	}

	/**
	 * Datetime functions: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05410_.htm
	 * EXTRACT: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05420_.htm
	 * CURRENT_DATE: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05430_.htm
	 * CURRENT_TIME: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05440_.htm
	 * CURRENT_TIMESTAMP: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05450_.htm
	 * CURRENT_GMTDATE: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05460_.htm
	 * CURRENT_GMTTIME: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05470_.htm
	 * CURRENT_GMTTIMESTAMP: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05480_.htm
	 * LOCAL_TIMEZONE: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05490_.htm
	 */
	private void datetimeFunctions() {
		datetimeFunction.is(firstOf(extractFunction));
		extractFunction.is(extractKeyword, lparenthesis, firstOf(daysKeyword, dayOfYearKeyword, dayofweekKeyword, yearKeyword, monthKeyword, dayKeyword, hourKeyword, minuteKeyword, secondKeyword,  monthsKeyword, quarterKeyword, quartersKeyword, weeksKeyword, weekOfYearKeyword, weekOfMonthKeyword, isLeapYearKeyweord), fromKeyword, expression, rparenthesis);
		//The other function do not need to be specified.
	}

	/**
	 * Database state functions: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak05850_.htm
	 * SQLCODE: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak17960_.htm
	 * SQLERRORTEXT: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak17970_.htm
	 * SQLNATIVEERROR: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak17980_.htm
	 * SQLSTATE: http://publib.boulder.ibm.com/infocenter/wmbhelp/v6r1m0/topic/com.ibm.etools.mft.doc/ak17990_.htm
	 */
	private void databaseStateFunction() {
		
		
	}

	/**
	 * Grammar of the listFunctions:
	 *  
	 */
	private void listFunctions() {
		listFunction.is(firstOf(theFunction));
		theFunction.is(theKeyword, lparenthesis, expression, rparenthesis);
	}

	private void fieldFunctions() {
		fieldFunction.is(firstOf(asbitstreamFunction));
		asbitstreamFunction.is(asbitstreamKeyword, lparenthesis, fieldReference, zeroOrMore(bitStreamOptions),
				rparenthesis);
		bitStreamOptions.is(
				firstOf(optionsKeyword, encodingKeyword, ccsidKeyword, setKeyword, typeKeyword, formatKeyword),
				expression);
	}

	private void stringManipulationFunctions() {
		stringManipulationFunction.is(firstOf(/*containsFunction, endswithFunction, leftFunction, lengthFunction, lowerFunction, lcaseFunction,
				ltrimFunction,*/ overlayFunction, positionFunction, /*replaceFunction, replicateFunction, rightFunction rtrimFunction,
				spaceFunction,*/ startswithFunction, substringFunction, /*translateFunction,*/ trimFunction, /*upperFunction,*/ ucaseFunction
				));
		overlayFunction.is(overlayKeyword, lparenthesis, expression, placingKeyword, expression, fromKeyword, expression, optional(sequence(forKeyword, expression)),rparenthesis);
		positionFunction.is(positionKeyword, lparenthesis, expression, inKeyword, expression,
				optional(sequence(fromKeyword, expression)), optional(sequence(repeatKeyword, expression)),
				rparenthesis);
		trimFunction.is(
				trimKeyword,
				lparenthesis,
				optional(sequence(
						firstOf(sequence(firstOf(bothKeyword, leadingKeyword, trailingKeyword), optional(expression)),
								expression), fromKeyword)), expression, rparenthesis);
		ucaseFunction.is(firstOf(upperKeyword, ucaseKeyword), lparenthesis, fieldReference, rparenthesis);
		startswithFunction.is(startswithKeyword, lparenthesis, expression, comma, expression, rparenthesis);
		substringFunction.is(substringKeyword, lparenthesis, expression,
				firstOf(fromKeyword, beforeKeyword, afterKeyword), expression,
				optional(sequence(forKeyword, expression)), rparenthesis);
	}

	private void complexFunctions() {
		complexFunction.is(firstOf(castFunction, caseFunction, selectFunction));
		castFunction.is(castKeyword, lparenthesis, expression, zeroOrMore(comma, expression), asKeyword, dataType,
				optional(sequence(ccsidKeyword, expression)), optional(sequence(encodingKeyword, expression)),
				optional(sequence(formatKeyword, expression)), optional(sequence(defaultKeyword, expression)),
				rparenthesis);
		caseFunction.is(caseKeyword, firstOf(simpleWhenClause, searchedWhenClause), optional(sequence(elseKeyword, firstOf(nullKeyword, expression))), endKeyword);
		simpleWhenClause.is(expression, oneOrMore(sequence(whenKeyword, expression, thenKeyword, firstOf(expression, nullKeyword))));
		searchedWhenClause.is(oneOrMore(sequence(whenKeyword, expression, thenKeyword,firstOf(expression, nullKeyword) )));
		selectFunction.is(selectKeyword, selectClause, fromClause, optional(whereClause));
		selectClause
				.is(firstOf(
						sequence(itemKeyword, expression),
						sequence(firstOf("COUNT", "MIN", "MAX", "SUM"), lparenthesis, firstOf(expression, start),
								rparenthesis),
						sequence(
								firstOf(expression, start),
								optional(
										firstOf(sequence(asKeyword, fieldReference), insertKeyword)),
										zeroOrMore(comma, firstOf(expression, start),
												optional(firstOf(sequence(asKeyword, fieldReference), insertKeyword))))));
		whereClause.is(whereKeyword, expression);
	}
}
