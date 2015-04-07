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

import org.sonar.sslr.parser.LexerlessGrammar;

import com.sonar.sslr.api.Rule;

public class EsqlGrammar extends LexerlessGrammar {

	public Rule stringLiteral;
	public Rule hexLiteral;
	public Rule numericLiteral;
	public Rule listLiteral;

	public Rule createKeyword;
	public Rule computeKeyword;
	public Rule moduleKeyword;
	public Rule databaseKeyword;
	public Rule filterKeyword;
	public Rule endKeyword;
	public Rule functionKeyword;
	public Rule procedureKeyword;
	public Rule inKeyword;
	public Rule outKeyword;
	public Rule inoutKeyword;
	public Rule nameKeyword;
	public Rule namespaceKeyword;
	public Rule constantKeyword;
	public Rule returnsKeyword;
	public Rule esqlKeyword;
	public Rule javaKeyword;
	public Rule dynamicKeyword;
	public Rule resultKeyword;
	public Rule setsKeyword;
	public Rule booleanKeyword;
	public Rule intKeyword;
	public Rule integerKeyword;
	public Rule floatKeyword;
	public Rule decimalKeyword;
	public Rule dateKeyword;
	public Rule timeKeyword;
	public Rule timestampKeyword;
	public Rule gmttimeKeyword;
	public Rule gmttimestampKeyword;
	public Rule intervalKeyword;
	public Rule charKeyword;
	public Rule characterKeyword;
	public Rule blobKeyword;
	public Rule bitKeyword;
	public Rule rowKeyword;
	public Rule externalKeyword;
	public Rule beginKeyword;
	public Rule notKeyword;
	public Rule atomicKeyword;
	public Rule callKeyword;
	public Rule intoKeyword;
	public Rule schemaKeyword;
	public Rule betweenKeyword;
	public Rule asymmetricKeyword;
	public Rule symmetricKeyword;
	public Rule existsKeyword;
	public Rule isKeyword;
	public Rule likeKeyword;
	public Rule escapeKeyword;
	public Rule singularKeyword;
	public Rule trueKeyword;
	public Rule falseKeyword;
	public Rule infKeyword;
	public Rule infinityKeyword;
	public Rule nanKeyword;
	public Rule nullKeyword;
	public Rule numKeyword;
	public Rule numberKeyword;
	public Rule unknownKeyword;
	public Rule andKeyword;
	public Rule fieldKeyword;
	public Rule previousSiblingKeyword;
	public Rule nextSiblingKeyword;
	public Rule firstChildKeyword;
	public Rule lastChildKeyword;
	public Rule ofKeyword;
	public Rule asKeyword;
	public Rule domainKeyword;
	public Rule repeatKeyword;
	public Rule valueKeyword;
	public Rule identityKeyword;
	public Rule typeKeyword;
	public Rule setKeyword;
	public Rule declareKeyword;
	public Rule sharedKeyword;
	public Rule referenceKeyword;
	public Rule toKeyword;
	public Rule returnKeyword;
	public Rule whileKeyword;
	public Rule doKeyword;
	public Rule orKeyword;
	public Rule ifKeyword;
	public Rule thenKeyword;
	public Rule elseKeyword;
	public Rule elseifKeyword;
	public Rule propagateKeyword;
	public Rule terminalKeyword;
	public Rule labelKeyword;
	public Rule environmentKeyword;
	public Rule messageKeyword;
	public Rule exceptionKeyword;
	public Rule finalizeKeyword;
	public Rule defaultKeyword;
	public Rule noneKeyword;
	public Rule deleteKeyword;
	public Rule castKeyword;
	public Rule ccsidKeyword;
	public Rule encodingKeyword;
	public Rule formatKeyword;
	public Rule forKeyword;
	public Rule upperKeyword;
	public Rule ucaseKeyword;
	public Rule startswithKeyword;
	public Rule substringKeyword;
	public Rule fromKeyword;
	public Rule beforeKeyword;
	public Rule afterKeyword;
	public Rule trimKeyword;
	public Rule bothKeyword;
	public Rule leadingKeyword;
	public Rule trailingKeyword;
	public Rule moveKeyword;
	public Rule parentKeyword;
	public Rule positionKeyword;
	public Rule throwKeyword;
	public Rule userKeyword;
	public Rule severityKeyword;
	public Rule catalogKeyword;
	public Rule valuesKeyword;
	public Rule asbitstreamKeyword;
	public Rule optionsKeyword;
	public Rule selectKeyword;
	public Rule whereKeyword;
	public Rule parseKeyword;
	public Rule languageKeyword;
	public Rule detachKeyword;
	public Rule passthruKeyword;
	public Rule insertKeyword;
	public Rule theKeyword;
	public Rule itemKeyword;
	public Rule minKeyword;
	public Rule maxKeyword;
	public Rule countKeyword;
	public Rule sumKeyword;
	public Rule updateKeyword;
	public Rule pathKeyword;
	public Rule brokerKeyword;
	public Rule caseKeyword;
	public Rule whenKeyword;
	public Rule continueKeyword;
	public Rule exitKeyword; 
	public Rule handlerKeyword;
	public Rule sqlstateKeyword;
	public Rule extractKeyword;
	public Rule placingKeyword;
	public Rule overlayKeyword;
	public Rule leaveKeyword;
	public Rule iterateKeyword;
	public Rule resignalKeyword;
	public Rule untilKeyword;
	public Rule logKeyword;
	public Rule eventKeyword;
	public Rule traceKeyword;
	public Rule fullKeyword;
	public Rule loopKeyword;
	
	
	
	public Rule program;
	public Rule eof;
	public Rule spacing;
	public Rule letterOrDigit;
	public Rule sourceElements;
	public Rule sourceElement;
	public Rule identifier;
	public Rule fieldName;
	public Rule keyword;
	public Rule dataType;
	public Rule intervalDataType;
	public Rule intervalQualifier;

	public Rule yearKeyword;
	public Rule monthKeyword;
	public Rule dayKeyword;
	public Rule hourKeyword;
	public Rule minuteKeyword;
	public Rule secondKeyword;
	public Rule dayofweekKeyword;

	public Rule monthsKeyword;
	public Rule quarterKeyword;
	public Rule quartersKeyword;
	public Rule weeksKeyword;
	public Rule weekOfYearKeyword;
	public Rule weekOfMonthKeyword;
	public Rule isLeapYearKeyweord;
	public Rule daysKeyword;
	public Rule dayOfYearKeyword;

	public Rule statement;
	public Rule basicStatement;
	public Rule messageTreeManipulationStatement;
	public Rule databaseUpdateStatement;
	public Rule nodeInteractionStatement;
	public Rule propagateStatement;
	public Rule messageSource;
	public Rule controlElement;
	public Rule otherStatement;
	public Rule declareHandlerStatement;
	public Rule resignalStatement;
	public Rule sqlState;
	public Rule repeatStatement;
	public Rule logStatement;
	public Rule logOptions;
	public Rule loopStatement;

	public Rule createStatement;
	public Rule deleteStatement;
	public Rule detachStatement;
	public Rule forStatement;
	public Rule moveStatement;
	public Rule nameClause;
	public Rule repeatClause;
	public Rule valuesClauses;
	public Rule fromClause;
	public Rule parseClause;
	public Rule namesClauses;
	public Rule valueExpression;
	public Rule passthruExpression;
	public Rule insertExpression;
	public Rule updateExpression;
	public Rule iterateStatement;

	public Rule qualifier;

	public Rule function;
	public Rule complexFunction;
	public Rule caseFunction;
	public Rule simpleWhenClause;
	public Rule searchedWhenClause;
	public Rule fieldFunction;
	public Rule listFunction;
	public Rule asbitstreamFunction;
	public Rule bitStreamOptions;
	public Rule castFunction;
	public Rule extractFunction;
	public Rule datetimeFunction;
	public Rule miscellaneousFunction;
	public Rule numericFunction;
	public Rule databaseStateFunction;
	public Rule stringManipulationFunction;
	public Rule overlayFunction;
	public Rule positionFunction;
	public Rule ucaseFunction;
	public Rule startswithFunction;
	public Rule substringFunction;
	public Rule trimFunction;
	public Rule theFunction;
	public Rule intervalLiteral;
	
	public Rule beginEndStatement;
	public Rule callStatement;
	public Rule caseStatement;
	public Rule whenClause;
	public Rule leaveStatement;
	public Rule setStatement;
	public Rule declareStatement;
	public Rule returnStatement;
	public Rule whileStatement;
	public Rule ifStatement;
	public Rule throwStatement;
	
	public Rule betweenOperator;
	public Rule existsOperator;
	public Rule inOperator;
	public Rule isOperator;
	public Rule singularOperator;

	public Rule variableStatement;
	public Rule moduleStatements;
	public Rule moduleStatement;

	public Rule eos;
	public Rule semi;
	protected Rule colon;
	public Rule lparenthesis;
	public Rule rparenthesis;
	public Rule lt;
	public Rule gt;
	public Rule le;
	public Rule ge;
	public Rule equal;
	public Rule notequal;
	public Rule plus;
	public Rule minus;
	public Rule start;
	public Rule div;
	public Rule concat;
	public Rule comma;
	public Rule dot;
	public Rule lcurlybrace;
	public Rule rcurlybrace;
	public Rule lbracket;
	public Rule rbracket;
	
	
	public Rule parameterList;
	public Rule parameter;
	
	public Rule fieldReference;
	public Rule pathElement;
	public Rule namespace;

	public Rule returnType;
	public Rule language;
	public Rule resultSet;
	public Rule routineBody;

	public Rule moduleDeclaration;
	public Rule moduleBody;
	public Rule routineDeclaration;
	public Rule qualificationIdentifier;
	public Rule primaryExpression;
	public Rule arrayLiteral;
	public Rule callExpression;
	public Rule expression;
	public Rule condition;
	public Rule leftHandSideExpression;
	public Rule unaryExpression;
	public Rule multiplicativeExpression;
	public Rule additiveExpression;
	public Rule relationalExpression;
	public Rule relationalExpressionNoIn;
	public Rule equalityExpression;
	public Rule equalityExpressionNoIn;
	public Rule logicalAndExpression;
	public Rule logicalAndExpressionNoIn;
	public Rule logicalOrExpression;
	public Rule logicalOrExpressionNoIn;
	public Rule assignmentExpression;
	public Rule assignmentExpressionNoIn;
	public Rule assignmentOperator;
	
	//public Rule sqlExpression;
	public Rule selectFunction;
	public Rule selectClause;
	public Rule whereClause;
	
	public Rule index;
	
	public Rule literal;
	public Rule nullLiteral;
	public Rule booleanLiteral;

	@Override
	public Rule getRootRule() {
		return program;
	}

}
